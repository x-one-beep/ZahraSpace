package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hadist")
data class Hadist(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val arabicText: String,
    val translation: String,
    val narrator: String,
    val book: String,
    val hadistNumber: Int,
    val grade: String,
    val topic: String = "",
    val explanation: String = ""
)
