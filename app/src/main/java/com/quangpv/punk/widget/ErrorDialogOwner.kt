package com.quangpv.punk.widget

import android.app.AlertDialog
import android.support.core.view.ViewScopeOwner

interface ErrorDialogOwner : ViewScopeOwner {
    val errorDialog: IErrorDialog
        get() = viewScope.getOr("error:dialog") {

            object : IErrorDialog {
                override fun show(error: Throwable) {
                    AlertDialog.Builder(viewScope.context)
                        .setTitle("Error")
                        .setMessage(error.message ?: "Unknown")
                        .create().show()
                }

            }
        }
}

interface IErrorDialog {
    fun show(error: Throwable)
}