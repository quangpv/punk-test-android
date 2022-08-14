package com.quangpv.punk.feature.usecase

import android.support.core.livedata.post
import android.support.di.Inject
import androidx.lifecycle.MutableLiveData
import com.quangpv.punk.helper.TextFormatter
import com.quangpv.punk.model.request.BeerQuerySource
import com.quangpv.punk.model.ui.IDateRange
import java.util.*

@Inject
class FilterBeerByIntervalCase(
    private val query: BeerQuerySource,
    private val textFormatter: TextFormatter,
) {

    val dateRange = MutableLiveData(createDateRange(query))

    fun setFromDate(it: Date) {
        query.setFrom(it)
        dateRange.post(createDateRange(query))
        query.requestRefresh()
    }

    fun setToDate(it: Date) {
        val from = query.getFrom()
        if (from != null) {
            if (!from.before(it)) error("To date should not be before from date")
        }
        query.setTo(it)
        dateRange.post(createDateRange(query))
        if (from != null) query.requestRefresh()
    }

    private fun createDateRange(query: BeerQuerySource): IDateRange {
        return object : IDateRange {
            override val from: String = query.getFrom()
                ?.let { textFormatter.formatRequestDate(it) } ?: "DATE FROM"

            override val to: String = query.getTo()
                ?.let { textFormatter.formatRequestDate(it) } ?: "DATE TO"
        }
    }
}