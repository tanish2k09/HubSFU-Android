package com.daisysoft.mysfu.data.constants

import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.data.model.CourseInfo

sealed class ClubEventsData {
    companion object {
        val events = arrayListOf(
            CourseInfo(
                "SFU Peak",
                "Photography session",
                "9:00am - 10:30am",
                "SUB 3000",
                "Tony Stark",
                R.drawable.camera,
                wqb = "Monday - 22"
            ),
            CourseInfo(
                "SFU Surge",
                "StormHacks Hackathon",
                "9:00am - 10:30am",
                "SUB 3000",
                "Elon Musk",
                R.drawable.code,
                wqb = "Tuesday - 23"
            )
        )
    }
}
