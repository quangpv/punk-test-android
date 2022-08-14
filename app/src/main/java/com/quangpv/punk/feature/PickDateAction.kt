package com.quangpv.punk.feature

import android.view.View
import by.dzmitry_lakisau.month_year_picker_dialog.MonthYearPickerDialog
import com.quangpv.punk.R
import java.util.*

class PickDateAction(private val function: (Date) -> Unit) : View.OnClickListener {
    override fun onClick(v: View?) {
        val context = v?.context ?: return

        MonthYearPickerDialog.Builder(
            context,
            R.style.Style_MonthYearPickerDialog_Red,
            selectedYear = 2022,
            onDateSetListener = { year, month ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.DATE, 0)
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, month)
                function(calendar.time)
            }
        )
            .setMinYear(2010)
            .setMode(MonthYearPickerDialog.Mode.MONTH_AND_YEAR)
            .build()
            .show()
    }

}
