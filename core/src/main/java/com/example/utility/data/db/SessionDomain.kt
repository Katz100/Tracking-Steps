package com.example.utility.data.db

import java.util.Date

data class SessionDomain (
    val id: Int? = null,
    val stepsCompleted: Int,
    val startTime: Date?,
    val endTime: Date?,
)