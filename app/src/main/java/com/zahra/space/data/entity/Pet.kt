package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pets")
data class Pet(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val userId: String = "1",
    val name: String,
    val type: String,
    val level: Int = 1,
    val experience: Int = 0,
    val hunger: Int = 50,
    val happiness: Int = 50,
    val cleanliness: Int = 50,
    val energy: Int = 80,
    val isSick: Boolean = false,
    val lastFed: Long = System.currentTimeMillis(),
    val lastPlayed: Long = System.currentTimeMillis(),
    val lastCleaned: Long = System.currentTimeMillis(),
    val accessories: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis()
)
