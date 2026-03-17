package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "monthly_letters")
data class MonthlyLetter(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val monthNumber: Int,
    val title: String,
    val content: String,
    val isRead: Boolean = false,
    val sentDate: Long,
    val createdAt: Long = System.currentTimeMillis()
)
