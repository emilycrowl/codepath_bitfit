package com.example.bitfit

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.bitfit.models.CaloriesDao
import com.example.bitfit.models.CaloriesEntity

@Database(entities = [CaloriesEntity::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    abstract fun caloriesDao(): CaloriesDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java, "Calories-db2"
            ).build()
    }
}