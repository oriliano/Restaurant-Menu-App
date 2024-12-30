package com.orhankoc_hw2
import com.orhankoc_hw2.AppDatabase





import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


object DatabaseModule {
    private var INSTANCE: AppDatabase? = null

    fun provideDatabase(context: Context): AppDatabase {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "restaurant_database"
            )
                .addMigrations(AppDatabase.MIGRATION_1_2) // Migration ekleniyor
                .fallbackToDestructiveMigration() // Migration tanımlanmadıysa sıfırlar
                .build()
        }
        return INSTANCE!!
    }
}
