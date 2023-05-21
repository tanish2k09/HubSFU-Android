package com.daisysoft.mysfu.data.model

data class EventInfo(
    val title: String,
    val date: String,
    val banner: Int,
    val location: String? = null,
    val schedule: String,
    val department: String,
    val presenter: String
)
