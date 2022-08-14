package com.quangpv.punk.widget.pagination

open class PagedList<T>(
    val pageSize: Int,
    private val mList: ArrayList<T> = ArrayList(),
) : List<T> by mList {
    private var mCanFetchMore: Boolean = true
    private var mTransaction: Transaction = Transaction()
    private var mCallback: Callback? = null

    val canFetchMore: Boolean get() = mCanFetchMore
    val isLocked: Boolean get() = mTransaction.isLocked
    val currentPage get() = mTransaction.list.size / pageSize
    val nextPage get() = currentPage + 1

    fun setCallback(callback: Callback?) {
        mCallback = callback
    }

    fun addMore(mores: List<T>): PagedList<T> {
        requireLock()
        if (mores.isEmpty()) {
            mCanFetchMore = false
            return this
        }
        mTransaction.list.addAll(mores)
        return this
    }

    fun clear() {
        requireLock()
        mCanFetchMore = true
        mTransaction.list.clear()
    }

    private fun requireLock() {
        if (!isLocked) error("Please lock before add more")
    }

    fun lock(shouldLoading: Boolean) {
        mTransaction.begin(shouldLoading)
    }

    inline fun withLock(shouldLoading: Boolean = true, block: () -> Unit) {
        if (isLocked) return
        lock(shouldLoading)
        block()
        unlock()
    }

    fun unlock() {
        mTransaction.end()
    }

    interface Callback {
        fun onLoadingChanged(loading: Boolean)
        fun onDatasetChanged()
    }

    private inner class Transaction {
        private var mShouldLoading: Boolean = true
        private var mLocked = false
        val list = ArrayList<T>()
        val isLocked: Boolean get() = mLocked

        fun begin(shouldLoading: Boolean) {
            mShouldLoading = shouldLoading
            synchronized(this) { mLocked = true }
            if (mShouldLoading) mCallback?.onLoadingChanged(true)
        }

        fun end() {
            synchronized(this) { mLocked = false }
            mList.clear()
            mList.addAll(list)
            if (mShouldLoading) mCallback?.onLoadingChanged(false)
            mCallback?.onDatasetChanged()
            mShouldLoading = true
        }
    }
}