package com.example.easy_life.ui.model

import com.example.easy_life.R

enum class StatusType (
    val statusName: String,
    val icon: Int
) {
    DONE(
        statusName = "DONE",
        icon = R.drawable.done
    ),

    ONGOING(
        statusName = "ONGOING",
        icon = R.drawable.ongoing
    ),

    MISSED(
        statusName = "MISSED",
        icon = R.drawable.not_done
    )
}