package com.zahra.space.data.dao

import androidx.room.*
import com.zahra.space.data.entity.MonthlyLetter
import kotlinx.coroutines.flow.Flow

@Dao
interface MonthlyLetterDao {
    @Query("SELECT * FROM monthly_letters ORDER BY monthNumber ASC")
    fun getAllLetters(): Flow<List<MonthlyLetter>>
    
    @Query("SELECT * FROM monthly_letters WHERE monthNumber = :monthNumber")
    fun getLetter(monthNumber: Int): Flow<MonthlyLetter?>
    
    @Query("SELECT * FROM monthly_letters WHERE isRead = 0 ORDER BY monthNumber ASC LIMIT 1")
    fun getUnreadLetter(): Flow<MonthlyLetter?>
    
    @Insert
    suspend fun insert(letter: MonthlyLetter)
    
    @Update
    suspend fun update(letter: MonthlyLetter)
}
