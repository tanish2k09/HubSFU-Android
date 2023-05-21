package com.daisysoft.mysfu.data.constants

import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.data.model.CourseInfo

sealed class CourseData {
    companion object {
        val courses = arrayListOf(
            CourseInfo(
                "CMPT 225",
                "Data Structure and Algorithms",
                "12:30pm - 2:20pm",
                "ASB 9620",
                "Toby Donaldson",
                R.drawable.terminal,
                "Quantitative"
            ),
            CourseInfo(
                "PSYC 388",
                "Biological Rhythm & Sleep",
                "2:30pm - 4:20pm",
                "AQ 3052",
                "Ralph Mistlberger",
                R.drawable.night
            ),
            CourseInfo(
                "PSYC 383",
                "Psychopharmacology",
                "4:30pm - 6:20pm",
                "AQ 3056",
                "Diana Lim",
                R.drawable.night
            ),
        )
    }
}