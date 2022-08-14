package com.quangpv.punk

import android.app.Application
import android.support.core.savedstate.SavedStateHandlerFactory
import android.support.di.LifecycleLookup
import android.support.di.ShareScope
import android.support.di.dependencies
import com.quangpv.punk.app.appModule

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        dependencies {
            modules(appModule)

            factory(shareIn = ShareScope.FragmentOrActivity) {
                val owner = (this as LifecycleLookup).owner
                SavedStateHandlerFactory(owner).create()
            }
        }
    }
}