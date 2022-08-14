package com.quangpv.punk.feature.usecase

import com.quangpv.punk.datasource.BeerApi
import com.quangpv.punk.helper.ShareViewModelScope
import com.quangpv.punk.helper.network.MockAsync
import com.quangpv.punk.instrument.LiveDataRule
import com.quangpv.punk.instrument.MockSharedScope
import com.quangpv.punk.model.dto.BeerDTO
import com.quangpv.punk.model.factory.BeerFactory
import com.quangpv.punk.model.request.BeerQuerySource
import com.quangpv.punk.model.request.BeerQuerySourceImpl
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.anyMap
import org.mockito.kotlin.*

class FetchBeersCaseTest {
    @get:Rule
    val liveDataRule = LiveDataRule()

    private lateinit var sharedScope: ShareViewModelScope
    private lateinit var query: BeerQuerySource
    private lateinit var beerFactory: BeerFactory
    private lateinit var beerApi: BeerApi
    private lateinit var fetcher: FetchBeersCase
    private lateinit var dataMocks: List<BeerDTO>

    @Before
    fun before() {
        beerApi = mock()
        beerFactory = mock()
        query = spy(BeerQuerySourceImpl(mock(), mock()))
        sharedScope = MockSharedScope()
        fetcher = FetchBeersCase(beerApi, beerFactory, query, sharedScope)

        dataMocks = listOf(BeerDTO(), BeerDTO(), BeerDTO())
        whenever(beerApi.getList(any())).thenReturn(spy(MockAsync(dataMocks)))
        whenever(beerFactory.create(any())).thenReturn(mock())
    }

    @After
    fun after() {
        sharedScope.cancel()
    }

    @Test
    fun `result should contains only data of first page after fetch`() {
        runBlocking {
            fetcher()
            fetcher()
            val value = fetcher.result.value!!
            assert(value.size == dataMocks.size)
        }
    }

    @Test
    fun `result should have more data of next page after fetch more`() {
        runBlocking {
            fetcher()
            fetcher.fetchMore()
            val value = fetcher.result.value!!
            assert(value.size == dataMocks.size * 2)
        }
    }

    @Test
    fun `the list should be refreshed when try refresh in case the previous data is not available`() {
        runBlocking {
            assert(fetcher.result.value.isNullOrEmpty())
            fetcher.tryRefresh()
            verify(beerApi.getList(anyMap()), times(1)).await()
            assert(!fetcher.result.value.isNullOrEmpty())
        }
    }

    @Test
    fun `the list should be refresh when query refresh event received`() {
        runBlocking {
            assert(fetcher.result.value.isNullOrEmpty())
            query.requestRefresh()
            delay(1000)
            verify(beerApi.getList(anyMap()), times(1)).await()
            assert(!fetcher.result.value.isNullOrEmpty())
        }
    }
}