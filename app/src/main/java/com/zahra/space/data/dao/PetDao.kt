package com.zahra.space.data.dao

import androidx.room.*
import com.zahra.space.data.entity.Pet
import kotlinx.coroutines.flow.Flow

@Dao
interface PetDao {
    @Query("SELECT * FROM pets WHERE userId = '1'")
    fun getPets(): Flow<List<Pet>>
    
    @Query("SELECT * FROM pets WHERE id = :id")
    fun getPet(id: Long): Flow<Pet?>
    
    @Insert
    suspend fun insert(pet: Pet): Long
    
    @Update
    suspend fun update(pet: Pet)
    
    @Query("UPDATE pets SET hunger = hunger - :amount WHERE id = :id")
    suspend fun decreaseHunger(id: Long, amount: Int)
    
    @Query("UPDATE pets SET happiness = happiness + :amount WHERE id = :id")
    suspend fun increaseHappiness(id: Long, amount: Int)
    
    @Query("UPDATE pets SET cleanliness = 100 WHERE id = :id")
    suspend fun cleanPet(id: Long)
    
    @Query("UPDATE pets SET experience = experience + :amount WHERE id = :id")
    suspend fun addExperience(id: Long, amount: Int)
}
