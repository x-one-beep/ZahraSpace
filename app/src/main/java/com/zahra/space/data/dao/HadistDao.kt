package com.zahra.space.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.zahra.space.data.entity.Hadist
import kotlinx.coroutines.flow.Flow

@Dao
interface HadistDao {
    @Query("SELECT * FROM hadist WHERE book = :book ORDER BY hadistNumber")
    fun getHadistByBook(book: String): Flow<List<Hadist>>
    
    @Query("SELECT * FROM hadist WHERE id = :id")
    fun getHadist(id: Long): Flow<Hadist?>
    
    @Query("SELECT * FROM hadist WHERE topic = :topic")
    fun getHadistByTopic(topic: String): Flow<List<Hadist>>
    
    @Query("SELECT * FROM hadist WHERE grade = :grade")
    fun getHadistByGrade(grade: String): Flow<List<Hadist>>
    
    @Query("SELECT * FROM hadist WHERE translation LIKE '%' || :keyword || '%'")
    fun searchHadist(keyword: String): Flow<List<Hadist>>
}
