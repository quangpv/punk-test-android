package com.quangpv.punk.feature

import android.support.core.event.LiveDataStatusOwner
import android.support.core.event.LoadingEvent
import android.support.core.event.WindowStatusOwner
import android.support.core.livedata.LoadingLiveData
import android.support.viewmodel.launch
import androidx.lifecycle.ViewModel
import com.quangpv.punk.feature.usecase.FetchBeersCase
import com.quangpv.punk.feature.usecase.FilterBeerByIntervalCase
import java.util.*

class MainViewModel(
    private val fetchBeersCase: FetchBeersCase,
    private val filterBeerByIntervalCase: FilterBeerByIntervalCase,
) : ViewModel(),
    WindowStatusOwner by LiveDataStatusOwner() {

    val dateRange = filterBeerByIntervalCase.dateRange

    val refreshLoading: LoadingEvent = LoadingLiveData()
    val viewLoading: LoadingEvent = LoadingLiveData()
    val beers = fetchBeersCase.result

    private val tryRefreshLoading
        get() = if (beers.value.isNullOrEmpty()) viewLoading
        else refreshLoading

    fun setFromDate(it: Date) = launch(error = error) {
        filterBeerByIntervalCase.setFromDate(it)
    }

    fun setToDate(it: Date) = launch(error = error) {
        filterBeerByIntervalCase.setToDate(it)
    }

    fun tryRefresh() = launch(tryRefreshLoading, error) {
        fetchBeersCase.tryRefresh()
    }

    fun refresh() = launch(refreshLoading, error) {
        fetchBeersCase()
    }

    fun fetchNext() = launch(error = error) {
        fetchBeersCase.fetchMore()
    }
}