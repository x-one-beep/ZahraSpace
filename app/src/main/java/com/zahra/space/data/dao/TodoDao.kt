package com.zahra.space.data.dao

import androidx.room.*
import com.zahra.space.data.entity.Todo
import kotlinx.coroutines.flow.Flow

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos WHERE isCompleted = 0 ORDER BY endDate ASC")
    fun getActiveTodos(): Flow<List<Todo>>
    
    @Query("SELECT * FROM todos WHERE isCompleted = 1 ORDER BY endDate DESC")
    fun getCompletedTodos(): Flow<List<Todo>>
    
    @Query("SELECT * FROM todos WHERE id = :id")
    fun getTodo(id: Long): Flow<Todo?>
    
    @Insert
    suspend fun insert(todo: Todo): Long
    
    @Update
    suspend fun update(todo: Todo)
    
    @Delete
    suspend fun delete(todo: Todo)
}
