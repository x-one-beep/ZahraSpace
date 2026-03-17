package com.zahra.space.data.dao

import androidx.room.*
import com.zahra.space.data.entity.FitnessTarget
import kotlinx.coroutines.flow.Flow

@Dao
interface FitnessDao {
    @Query("SELECT * FROM fitness_targets WHERE isActive = 1")
    fun getActiveTargets(): Flow<List<FitnessTarget>>
    
    @Query("SELECT * FROM fitness_targets WHERE id = :id")
    fun getTarget(id: Long): Flow<FitnessTarget?>
    
    @Insert
    suspend fun insert(target: FitnessTarget): Long
    
    @Update
    suspend fun update(target: FitnessTarget)
}
