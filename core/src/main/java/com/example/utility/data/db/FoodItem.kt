package com.example.utility.data.db

import java.util.Date

data class FoodItem (
    val id: Int? = null,
    val foodName: String,
    val calories: Int,
    val dateAdded: Date? = null
)