package com.zahra.space.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User(
    @PrimaryKey val id: String = "1",
    val name: String = "",
    val age: Int = 0,
    val avatar: String = "👩",
    val petType: String = "cat",
    val petName: String = "Luna",
    val theme: String = "light",
    val totalPoints: Long = 0,
    val streak: Int = 0,
    val imanLevel: Int = 50,
    val installDate: Long = System.currentTimeMillis(),
    val lastActive: Long = System.currentTimeMillis(),
    val hasCompletedOnboarding: Boolean = false,
    val privacyMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val createdAt: Long = System.currentTimeMillis()
)
