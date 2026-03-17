package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hidden_messages")
data class HiddenMessage(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val content: String,
    val location: String,
    val isFound: Boolean = false,
    val pointsReward: Int = 10,
    val createdAt: Long = System.currentTimeMillis()
)
