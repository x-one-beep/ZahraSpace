package com.zahra.space.data.dao

import androidx.room.*
import com.zahra.space.data.entity.DailyChecklist
import kotlinx.coroutines.flow.Flow

@Dao
interface DailyChecklistDao {
    @Query("SELECT * FROM daily_checklist WHERE date = :date")
    fun getChecklist(date: String): Flow<DailyChecklist?>
    
    @Query("SELECT * FROM daily_checklist ORDER BY date DESC LIMIT 30")
    fun getLast30Days(): Flow<List<DailyChecklist>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(checklist: DailyChecklist)
    
    @Update
    suspend fun update(checklist: DailyChecklist)
    
    @Query("SELECT SUM(totalPoints) FROM daily_checklist WHERE date >= :startDate")
    suspend fun getTotalPointsSince(startDate: String): Int
}
