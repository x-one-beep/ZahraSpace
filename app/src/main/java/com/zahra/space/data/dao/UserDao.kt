package com.zahra.space.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.zahra.space.data.entity.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {
    @Query("SELECT * FROM users WHERE id = '1'")
    fun getUser(): Flow<User>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: User)
    
    @Update
    suspend fun update(user: User)
    
    @Query("UPDATE users SET totalPoints = totalPoints + :points WHERE id = '1'")
    suspend fun addPoints(points: Int)
    
    @Query("UPDATE users SET streak = streak + 1 WHERE id = '1'")
    suspend fun incrementStreak()
    
    @Query("UPDATE users SET streak = 0 WHERE id = '1'")
    suspend fun resetStreak()
    
    @Query("UPDATE users SET imanLevel = imanLevel + :change WHERE id = '1'")
    suspend fun updateIman(change: Int)
    
    @Query("UPDATE users SET lastActive = :timestamp WHERE id = '1'")
    suspend fun updateLastActive(timestamp: Long)
    
    @Query("UPDATE users SET privacyMode = :enabled WHERE id = '1'")
    suspend fun setPrivacyMode(enabled: Boolean)
    
    @Query("UPDATE users SET notificationsEnabled = :enabled WHERE id = '1'")
    suspend fun setNotifications(enabled: Boolean)
}
