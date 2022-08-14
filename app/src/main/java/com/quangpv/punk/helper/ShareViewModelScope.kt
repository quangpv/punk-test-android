package com.quangpv.punk.helper

import android.support.di.InjectBy
import android.support.di.Injectable
import android.support.di.ShareScope
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

@InjectBy(ShareViewModelScopeImpl::class, ShareScope.FragmentOrActivity)
interface ShareViewModelScope : CoroutineScope, Injectable

class ShareViewModelScopeImpl : ShareViewModelScope, CoroutineScope, AutoCloseable {
    override val coroutineContext: CoroutineContext =
        SupervisorJob() + Dispatchers.Main.immediate + CoroutineExceptionHandler { _, _ -> }

    override fun close() {
        cancel()
    }
}