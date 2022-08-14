package com.quangpv.punk.widget.pagination

import android.annotation.SuppressLint
import android.support.core.view.IHolder
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quangpv.punk.extension.activity
import com.quangpv.punk.extension.findFragment

@Suppress("unchecked_cast")
abstract class PaginationAdapter<T> : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        const val TYPE_LOADING = -100
    }

    private var mRecyclerView: RecyclerView? = null
    private var isLoading: Boolean = false
    private var mItems: PagedList<T>? = null
    private var mLoadMoreAction = LoadMoreAction()

    private var isEmpty: Boolean? = null
    private var mLifecycleOwner: LifecycleOwner? = null

    var onEmptyChangedListener: (Boolean) -> Unit = {}
    var onLoadMoreListener: () -> Unit = {}


    private val lifecycleOwner: LifecycleOwner?
        get() {
            if (mLifecycleOwner != null) return mLifecycleOwner
            mLifecycleOwner = mRecyclerView?.findFragment()?.viewLifecycleOwner
                ?: mRecyclerView?.context?.activity
            return mLifecycleOwner
        }

    @SuppressLint("NotifyDataSetChanged")
    private val mPagedListener = object : PagedList.Callback {
        override fun onLoadingChanged(loading: Boolean) {
            if (isLoading == loading) return
            isLoading = loading
            if (isLoading) {
                findLoadingHolder()?.setLoading(isLoading)
            }
        }

        override fun onDatasetChanged() {
            lifecycleOwner?.lifecycleScope?.launchWhenResumed {
                updateChange()
            }
        }
    }

    private fun findLoadingHolder(): LoadingHolder? {
        val view = mRecyclerView ?: return null
        for (i in 0 until view.childCount) {
            val viewHolder = view.findContainingViewHolder(view.getChildAt(i))
            if (viewHolder is LoadingHolder) {
                return viewHolder
            }
        }
        return null
    }

    fun submit(items: PagedList<T>? = null) {
        mItems?.setCallback(null)
        mItems = items
        items?.setCallback(mPagedListener)
        updateChange()
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateChange() {
        notifyDataSetChanged()
        val isListEmpty = mItems?.isEmpty() ?: true
        if (isEmpty == isListEmpty) return
        isEmpty = isListEmpty
        onEmptyChangedListener(isListEmpty)
    }

    private inner class LoadMoreAction : RecyclerView.OnScrollListener() {

        private val threshold = 3
        private val isFetching: Boolean get() = mItems?.isLocked ?: false
        private val canFetchMore: Boolean
            get() = (mItems?.canFetchMore ?: false)
                    && (lifecycleOwner?.lifecycle?.currentState?.isAtLeast(Lifecycle.State.RESUMED)
                ?: false)

        private var mLastFetch = 0L

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as? LinearLayoutManager ?: return
            val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
            val lastPosition = (mItems?.size ?: 0) - 1

            if (lastVisiblePosition == 0) return

            if (lastVisiblePosition + threshold >= lastPosition) {
                if (isFetching || !canFetchMore) return
                lifecycleOwner?.lifecycleScope?.launchWhenResumed {
                    doFetchMore()
                }
            }
        }

        private fun doFetchMore() {
            val now = System.currentTimeMillis()
            if (now - mLastFetch <= 500) return
            onLoadMoreListener()
            mLastFetch = System.currentTimeMillis()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerView.addOnScrollListener(mLoadMoreAction)
        mItems?.setCallback(mPagedListener)
        mRecyclerView = recyclerView
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerView.removeOnScrollListener(mLoadMoreAction)
        mItems?.setCallback(null)
        mRecyclerView = null
        mLifecycleOwner = null
    }

    override fun getItemViewType(position: Int): Int {
        if (position == itemCount - 1) return TYPE_LOADING
        return super.getItemViewType(position)
    }

    override fun getItemId(position: Int): Long {
        if (getItemViewType(position) == TYPE_LOADING) return TYPE_LOADING.toLong()
        return super.getItemId(position)
    }

    final override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder {
        if (viewType == TYPE_LOADING) {
            return onCreateLoadingHolder(parent)
        }
        return onCreateItemHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is IHolder<*>) {
            mItems?.get(position)?.also {
                (holder as IHolder<T>).bindIfNeeded(it)
            }
        } else if (holder is LoadingHolder) {
            holder.setLoading(isLoading)
        }
    }

    protected abstract fun onCreateItemHolder(
        parent: ViewGroup,
        viewType: Int,
    ): RecyclerView.ViewHolder

    protected abstract fun onCreateLoadingHolder(parent: ViewGroup): LoadingHolder

    override fun getItemCount(): Int {
        if (mItems == null || mItems!!.size == 0) return 0
        return mItems!!.size + 1
    }

    open class LoadingHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        open fun setLoading(isLoading: Boolean) {
            val loading = if (isLoading && adapterPosition == 0) false
            else isLoading

            itemView.layoutParams.height = if (loading) ViewGroup.LayoutParams.WRAP_CONTENT else 0
            itemView.visibility = if (loading) View.VISIBLE else View.GONE
        }
    }
}
