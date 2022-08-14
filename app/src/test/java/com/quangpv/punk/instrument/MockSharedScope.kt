package com.quangpv.punk.instrument

import com.quangpv.punk.helper.ShareViewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlin.coroutines.CoroutineContext

class MockSharedScope : ShareViewModelScope {
    override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.IO
}