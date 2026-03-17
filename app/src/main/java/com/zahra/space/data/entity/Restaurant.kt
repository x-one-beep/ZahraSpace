package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant")
data class Restaurant(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val level: Int = 1,
    val experience: Int = 0,
    val balance: Int = 1000,
    val reputation: Int = 50,
    val employeeCount: Int = 0,
    val tables: Int = 4,
    val completedOrders: Int = 0,
    val totalCustomers: Int = 0
)
