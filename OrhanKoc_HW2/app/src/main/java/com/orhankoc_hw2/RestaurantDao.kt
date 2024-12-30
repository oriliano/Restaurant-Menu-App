package com.orhankoc_hw2

import com.orhankoc_hw2.RestaurantEntity
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Delete
import androidx.room.OnConflictStrategy
import androidx.room.Update

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurant_table")
    suspend fun getAllRestaurants(): List<RestaurantEntity>

    @Insert
    suspend fun insertRestaurant(restaurant: RestaurantEntity)

    @Update
    suspend fun updateRestaurant(restaurant: RestaurantEntity)

    @Delete
    suspend fun deleteRestaurant(restaurant: RestaurantEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllRestaurants(restaurants: List<RestaurantEntity>)
    @Query("DELETE FROM restaurant_table")
    suspend fun deleteAllRestaurants()


}
