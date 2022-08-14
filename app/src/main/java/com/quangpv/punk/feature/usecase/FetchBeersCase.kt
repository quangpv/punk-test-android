package com.quangpv.punk.feature.usecase

import android.support.di.Inject
import androidx.lifecycle.MutableLiveData
import com.quangpv.punk.datasource.BeerApi
import com.quangpv.punk.helper.ShareViewModelScope
import com.quangpv.punk.model.factory.BeerFactory
import com.quangpv.punk.model.request.BeerQuerySource
import com.quangpv.punk.model.ui.IBeer
import com.quangpv.punk.widget.pagination.PagedList
import kotlinx.coroutines.launch

@Inject
class FetchBeersCase(
    private val beerApi: BeerApi,
    private val beerFactory: BeerFactory,
    private val query: BeerQuerySource,
    private val shareViewModelScope: ShareViewModelScope,
) {
    private val pagedList = PagedList<IBeer>(10)
    val result = MutableLiveData(pagedList)

    init {
        shareViewModelScope.launch {
            query.onRequestRefresh.collect {
                invoke()
            }
        }
    }

    suspend operator fun invoke() = pagedList.withLock {
        pagedList.clear()
        doFetch()
    }

    suspend fun fetchMore() = pagedList.withLock {
        doFetch()
    }

    private suspend fun doFetch() {
        val data = beerApi.getList(query.build(pagedList.nextPage, pagedList.pageSize))
            .await()
            .map { beerFactory.create(it) }
        pagedList.addMore(data)
    }

    suspend fun tryRefresh() {
        val shouldRefresh = !pagedList.isLocked || pagedList.isEmpty()
        if (!shouldRefresh) return
        invoke()
    }
}