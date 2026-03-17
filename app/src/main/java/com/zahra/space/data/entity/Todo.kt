package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val category: String,
    val targetDays: Int,
    val dailyTarget: String,
    val startDate: Long,
    val endDate: Long,
    val currentProgress: Int = 0,
    val isCompleted: Boolean = false,
    val filosofi: String? = null,
    val tutorial: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)
