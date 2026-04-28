package com.example.utility.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "session")
data class SessionEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "steps_completed")
    val stepsCompleted: Int,

    @ColumnInfo(name = "start_time")
    val startTime: Date?,

    @ColumnInfo(name = "end_time")
    val endTime: Date?,
)