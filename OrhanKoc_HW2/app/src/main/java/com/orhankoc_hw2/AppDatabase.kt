package com.orhankoc_hw2


import com.orhankoc_hw2.RestaurantEntity
import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.Database
import androidx.room.RoomDatabase

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [RestaurantEntity::class], version = 2)
abstract class AppDatabase : RoomDatabase() {
    abstract fun restaurantDao(): RestaurantDao

    companion object {
        // Migration tanımı: Version 1'den 2'ye geçiş
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Eğer tablo eksikse oluşturulur
                database.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS restaurant_table (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        companyName TEXT NOT NULL,
                        description TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }
    }
}