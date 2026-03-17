package com.zahra.space.data.dao

import androidx.room.Dao
import androidx.room.Query
import com.zahra.space.data.entity.Dzikir
import kotlinx.coroutines.flow.Flow

@Dao
interface DzikirDao {
    @Query("SELECT * FROM dzikir WHERE category = :category")
    fun getDzikirByCategory(category: String): Flow<List<Dzikir>>
    
    @Query("SELECT * FROM dzikir WHERE id = :id")
    fun getDzikir(id: Long): Flow<Dzikir?>
    
    @Query("SELECT * FROM dzikir WHERE isFavorite = 1")
    fun getFavoriteDzikir(): Flow<List<Dzikir>>
}
