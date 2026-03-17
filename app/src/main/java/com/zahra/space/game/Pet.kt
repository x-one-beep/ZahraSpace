package com.zahra.space.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class PetState(
    val name: String = "Luna",
    val type: String = "cat",
    val hunger: Int = 50,
    val happiness: Int = 50,
    val cleanliness: Int = 50,
    val energy: Int = 80,
    val level: Int = 1,
    val experience: Int = 0,
    val isSick: Boolean = false,
    val lastFed: Long = System.currentTimeMillis(),
    val lastPlayed: Long = System.currentTimeMillis(),
    val lastCleaned: Long = System.currentTimeMillis()
)

class Pet {
    private val _state = MutableStateFlow(PetState())
    val state: StateFlow<PetState> = _state.asStateFlow()
    
    // Feed the pet
    fun feed() {
        val current = _state.value
        _state.value = current.copy(
            hunger = (current.hunger - 20).coerceIn(0, 100),
            happiness = (current.happiness + 5).coerceIn(0, 100),
            energy = (current.energy + 10).coerceIn(0, 100),
            lastFed = System.currentTimeMillis()
        )
        addExperience(5)
        checkHealth()
    }
    
    // Play with pet
    fun play() {
        val current = _state.value
        _state.value = current.copy(
            happiness = (current.happiness + 20).coerceIn(0, 100),
            hunger = (current.hunger + 10).coerceIn(0, 100),
            energy = (current.energy - 10).coerceIn(0, 100),
            lastPlayed = System.currentTimeMillis()
        )
        addExperience(10)
        checkHealth()
    }
    
    // Clean the pet
    fun clean() {
        val current = _state.value
        _state.value = current.copy(
            cleanliness = 100,
            happiness = (current.happiness + 5).coerceIn(0, 100),
            lastCleaned = System.currentTimeMillis()
        )
        addExperience(3)
        checkHealth()
    }
    
    // Put pet to sleep
    fun sleep() {
        val current = _state.value
        _state.value = current.copy(
            energy = 100,
            happiness = (current.happiness + 5).coerceIn(0, 100)
        )
    }
    
    // Add experience and check level up
    private fun addExperience(amount: Int) {
        val current = _state.value
        val newExp = current.experience + amount
        val newLevel = (newExp / 100) + 1
        
        _state.value = current.copy(
            experience = newExp,
            level = newLevel.coerceIn(1, 10)
        )
    }
    
    // Check if pet is sick
    private fun checkHealth() {
        val current = _state.value
        val isSick = current.hunger > 80 || current.happiness < 20 || current.cleanliness < 20
        _state.value = current.copy(isSick = isSick)
    }
    
    // Update pet stats over time
    fun updateTimeElapsed(hours: Int) {
        val current = _state.value
        _state.value = current.copy(
            hunger = (current.hunger + hours * 2).coerceIn(0, 100),
            happiness = (current.happiness - hours).coerceIn(0, 100),
            cleanliness = (current.cleanliness - hours).coerceIn(0, 100),
            energy = (current.energy - hours).coerceIn(0, 100)
        )
        checkHealth()
    }
    
    // Get pet status description
    fun getStatus(): String {
        val current = _state.value
        return when {
            current.isSick -> "🤒 Sakit"
            current.hunger > 70 -> "🍖 Lapar"
            current.happiness < 30 -> "😢 Sedih"
            current.cleanliness < 30 -> "🧼 Kotor"
            current.energy < 30 -> "😴 Ngantuk"
            else -> "😊 Bahagia"
        }
    }
    
    // Get evolution stage based on level
    fun getEvolutionStage(): String {
        return when (_state.value.level) {
            in 1..2 -> "Bayi"
            in 3..4 -> "Anak-anak"
            in 5..6 -> "Remaja"
            in 7..8 -> "Dewasa"
            else -> "Legenda"
        }
    }
}
