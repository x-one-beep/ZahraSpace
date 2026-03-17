package com.zahra.space.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zahra.space.data.entity.Restaurant
import kotlinx.coroutines.flow.Flow

@Dao
interface RestaurantDao {
    @Query("SELECT * FROM restaurant LIMIT 1")
    fun getRestaurant(): Flow<Restaurant?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(restaurant: Restaurant)
    
    @Update
    suspend fun update(restaurant: Restaurant)
    
    @Query("UPDATE restaurant SET balance = balance + :amount")
    suspend fun addBalance(amount: Int)
    
    @Query("UPDATE restaurant SET experience = experience + :amount")
    suspend fun addExperience(amount: Int)
    
    @Query("UPDATE restaurant SET completedOrders = completedOrders + 1")
    suspend fun incrementOrders()
}
