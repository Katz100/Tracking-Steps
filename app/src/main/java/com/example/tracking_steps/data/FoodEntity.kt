package com.example.tracking_steps.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "food_item")
data class FoodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "food_name")
    val foodName: String?,

    @ColumnInfo(name = "calories")
    val calories: Int?,

    @ColumnInfo(name = "date_added")
    val dateAdded: Date?,
)