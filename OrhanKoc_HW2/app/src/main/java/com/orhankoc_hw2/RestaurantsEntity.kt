package com.orhankoc_hw2

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey




@Entity(tableName = "restaurant_table")
data class RestaurantEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "companyName") val companyName: String,
    @ColumnInfo(name = "description") val description: String
)