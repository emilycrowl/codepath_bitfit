package com.example.bitfit.models

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CaloriesDao {
    @Query("SELECT * FROM calories_table")
    fun getAll(): Flow<List<CaloriesEntity>>

    @Insert
    fun insertAll(foods: List<CaloriesEntity>)

    @Insert
    fun insert(food: CaloriesEntity)

    @Query("DELETE FROM calories_table")
    fun deleteAll()
}