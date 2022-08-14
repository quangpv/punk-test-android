package com.quangpv.punk.model.request

import android.support.core.savedstate.SavedStateHandler
import android.support.di.InjectBy
import android.support.di.Injectable
import android.support.di.ShareScope
import com.quangpv.punk.helper.TextFormatter
import com.quangpv.punk.helper.testing.OpenForTesting
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import java.util.*

@InjectBy(BeerQuerySourceImpl::class, ShareScope.Activity)
interface BeerQuerySource : Injectable {
    val onRequestRefresh: Flow<Any>

    fun setTo(date: Date)
    fun setFrom(date: Date)
    fun requestRefresh()
    fun build(page: Int, pageSize: Int): Map<String, Any>
    fun getTo(): Date?
    fun getFrom(): Date?
}

@OpenForTesting
class BeerQuerySourceImpl(
    private val textFormatter: TextFormatter,
    private val savedStateHandler: SavedStateHandler, // This use for viewModel is destroyed and re-created new.
) : BeerQuerySource {
    private var mFrom: Date? = savedStateHandler.get("from")
    private var mTo: Date? = savedStateHandler.get("to")

    override val onRequestRefresh = MutableSharedFlow<Any>(0, 1)

    override fun getFrom(): Date? {
        return mFrom
    }

    override fun getTo(): Date? {
        return mTo
    }

    override fun setTo(date: Date) {
        mTo = date
        savedStateHandler.set("to", mTo)
    }

    override fun setFrom(date: Date) {
        mFrom = date
        savedStateHandler.set("from", mFrom)
    }

    override fun requestRefresh() {
        onRequestRefresh.tryEmit(Any())
    }

    override fun build(page: Int, pageSize: Int): Map<String, Any> {
        val body = hashMapOf<String, Any>(
            "page" to page,
            "per_page" to pageSize,
        )
        if (mTo != null) body["brewed_before"] = textFormatter.formatRequestDate(mTo!!)
        if (mFrom != null) body["brewed_after"] = textFormatter.formatRequestDate(mFrom!!)
        return body
    }
}