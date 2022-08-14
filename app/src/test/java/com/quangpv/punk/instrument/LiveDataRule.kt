package com.quangpv.punk.instrument

import androidx.arch.core.executor.ArchTaskExecutor
import androidx.arch.core.executor.TaskExecutor
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class LiveDataRule : TestWatcher() {
    private val executor = object : TaskExecutor() {
        override fun executeOnDiskIO(runnable: Runnable) {
            runnable.run()
        }

        override fun postToMainThread(runnable: Runnable) {
            runnable.run()
        }

        override fun isMainThread(): Boolean {
            return true
        }
    }

    override fun starting(description: Description) {
        super.starting(description)
        ArchTaskExecutor.getInstance().setDelegate(executor)
    }

    override fun finished(description: Description) {
        super.finished(description)
        ArchTaskExecutor.getInstance().setDelegate(null)
    }
}