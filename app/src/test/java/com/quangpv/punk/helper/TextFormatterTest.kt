package com.quangpv.punk.helper

import org.junit.Test
import java.util.*

class TextFormatterTest {

    @Test
    fun `the request date should be format like MM-yyyy`() {
        val formatter = TextFormatter()
        val date = Calendar.getInstance().apply {
            set(Calendar.DATE, 0)
            set(Calendar.YEAR, 2022)
            set(Calendar.MONTH, 12)
        }.time
        assert(formatter.formatRequestDate(date) == "12-2022")
    }
}