package com.quangpv.punk.feature.usecase

import com.quangpv.punk.helper.TextFormatter
import com.quangpv.punk.instrument.LiveDataRule
import com.quangpv.punk.model.request.BeerQuerySource
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.*
import java.time.Instant
import java.util.*

class FilterBeerByIntervalCaseTest {

    @get:Rule
    val liveDataRule = LiveDataRule()

    private lateinit var filter: FilterBeerByIntervalCase
    private lateinit var textFormatter: TextFormatter
    private lateinit var query: BeerQuerySource

    @Before
    fun before() {
        query = mock()
        textFormatter = spy()
        filter = FilterBeerByIntervalCase(query, textFormatter)
    }

    @Test
    fun `request refresh list after set from date`() {
        filter.setFromDate(mock())
        verify(query, times(1)).setFrom(any())
        verify(query, times(1)).requestRefresh()
    }

    @Test
    fun `to date should not before from date`() {
        whenever(query.getFrom()).thenReturn(Date.from(Instant.now()))

        Assert.assertThrows(IllegalStateException::class.java) {
            filter.setToDate(Calendar.getInstance().apply {
                set(Calendar.YEAR, 2021)
            }.time)
        }
    }

    @Test
    fun `set to date still be accepted if from date is not set yet, but don't refresh list`() {
        whenever(query.getFrom()).thenReturn(null)
        filter.setToDate(Date.from(Instant.now()))
        verify(query, times(1)).setTo(any())
        verify(query, never()).requestRefresh()
    }

    @Test
    fun `request refresh list after both of from and to is set`() {
        filter.setFromDate(Calendar.getInstance().apply {
            set(Calendar.YEAR, 2021)
        }.time)
        filter.setToDate(Date.from(Instant.now()))

        verify(query, times(1)).requestRefresh()
    }

}