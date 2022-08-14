package com.quangpv.punk.helper

import android.support.di.Inject
import android.support.di.ShareScope
import com.quangpv.punk.helper.testing.OpenForTesting
import java.text.SimpleDateFormat
import java.util.*

@OpenForTesting
@Inject(ShareScope.Singleton)
class TextFormatter {
    fun formatRequestDate(date: Date): String {
        return SimpleDateFormat("MM-yyyy", Locale.getDefault()).format(date)
    }
}