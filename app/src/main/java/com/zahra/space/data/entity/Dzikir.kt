package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dzikir")
data class Dzikir(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: String,
    val arabicText: String,
    val latinText: String,
    val translation: String,
    val fadhilah: String,
    val defaultCount: Int,
    val pointsPerTap: Int,
    val reference: String,
    val isFavorite: Boolean = false
)
