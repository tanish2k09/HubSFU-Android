package com.daisysoft.mysfu.data.constants

import com.daisysoft.mysfu.R
import com.daisysoft.mysfu.data.model.EventInfo

sealed class EventData {
    companion object {
        val events = arrayListOf(
            EventInfo(
                "Asian Heritage Month",
                "Sunday - May 21, 2023",
                R.drawable.event_1,
                "",
                "All day Yearly (on 10 dates from May 1)",
                "Arts & Culture",
                "Presented by Equity, Diversity & Inclusion"
            ),
            EventInfo(
                "Into The Little Hill",
                "Sunday - May 21, 2023",
                R.drawable.event_2,
                "SFU GoldCorp Centre for the Arts",
                "All day",
                "Arts & Culture",
                "Presented by Woodward's Cultural Programs"
            )
        )
    }
}
