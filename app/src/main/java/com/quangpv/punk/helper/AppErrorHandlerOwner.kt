package com.quangpv.punk.helper

import android.support.core.view.ViewScopeOwner
import com.quangpv.punk.widget.ErrorDialogOwner

interface AppErrorHandlerOwner : ViewScopeOwner, ErrorDialogOwner {
    val errorHandler: ErrorHandler
        get() = viewScope.getOr("app:error:handler") {
            ErrorHandlerImpl(this)
        }
}

interface ErrorHandler {
    fun handle(e: Throwable)
}

class ErrorHandlerImpl(
    private val errorDialogOwner: ErrorDialogOwner,
) : ErrorHandler {
    override fun handle(e: Throwable) {
        errorDialogOwner.errorDialog.show(e)
    }
}