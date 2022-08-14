package com.quangpv.punk.app

import android.support.core.event.WindowStatusOwner
import android.support.core.extensions.LifecycleSubscriberExt
import android.support.viewmodel.ViewModelRegistrable
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.quangpv.punk.helper.AppErrorHandlerOwner

abstract class AppActivity(contentLayoutId: Int) : AppCompatActivity(contentLayoutId),
    ViewModelRegistrable,
    LifecycleSubscriberExt,
    AppErrorHandlerOwner {

    val self get() = this

    override fun onRegistryViewModel(viewModel: ViewModel) {
        if (viewModel is WindowStatusOwner) {
            viewModel.error.bind { errorHandler.handle(it) }
        }
    }
}
