package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fitness_targets")
data class FitnessTarget(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val type: String,
    val startDate: Long,
    val endDate: Long,
    val startValue: Double,
    val targetValue: Double,
    val currentValue: Double,
    val dailyCalorieTarget: Int,
    val dailyProteinTarget: Double,
    val dailyCarbsTarget: Double,
    val dailyFatTarget: Double,
    val weeklyWorkoutDays: Int,
    val isActive: Boolean = true
)
