package com.example.utility.data.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert

@Dao
interface SessionDao {
    @Insert
    fun insertAll(vararg sessionEntity: SessionEntity)

    @Delete
    fun deleteSession(sessionEntity: SessionEntity)
}