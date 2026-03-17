package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.UserDao
import com.zahra.space.data.dao.PetDao
import com.zahra.space.data.dao.DailyChecklistDao
import com.zahra.space.game.Pet
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userDao: UserDao,
    private val petDao: PetDao,
    private val checklistDao: DailyChecklistDao
) : ViewModel() {
    
    private val _userName = MutableStateFlow("")
    val userName: StateFlow<String> = _userName
    
    private val _totalPoints = MutableStateFlow(0L)
    val totalPoints: StateFlow<Long> = _totalPoints
    
    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak
    
    private val _imanLevel = MutableStateFlow(50)
    val imanLevel: StateFlow<Int> = _imanLevel
    
    private val _todayChecklist = MutableStateFlow<Map<String, Boolean>>(emptyMap())
    val todayChecklist: StateFlow<Map<String, Boolean>> = _todayChecklist
    
    private val _petStatus = MutableStateFlow(Pet().state.value)
    val petStatus: StateFlow<Pet.PetState> = _petStatus
    
    private val _calendarColors = MutableStateFlow<Map<String, String>>(emptyMap())
    val calendarColors: StateFlow<Map<String, String>> = _calendarColors
    
    private val _greeting = MutableStateFlow("")
    val greeting: StateFlow<String> = _greeting
    
    private val _currentDate = MutableStateFlow("")
    val currentDate: StateFlow<String> = _currentDate
    
    private val _currentTime = MutableStateFlow("")
    val currentTime: StateFlow<String> = _currentTime
    
    private val _haidStatus = MutableStateFlow("")
    val haidStatus: StateFlow<String> = _haidStatus
    
    init {
        loadUserData()
        updateTime()
        updateGreeting()
        loadTodayChecklist()
        loadPetStatus()
        loadCalendarColors()
    }
    
    private fun loadUserData() {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _userName.value = user.name.ifEmpty { "Zahra" }
                _totalPoints.value = user.totalPoints
                _streak.value = user.streak
                _imanLevel.value = user.imanLevel
            }
        }
    }
    
    private fun updateTime() {
        viewModelScope.launch {
            while (true) {
                val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
                val timeFormat = SimpleDateFormat("HH:mm", Locale("id"))
                _currentDate.value = dateFormat.format(Date())
                _currentTime.value = timeFormat.format(Date())
                kotlinx.coroutines.delay(1000)
            }
        }
    }
    
    private fun updateGreeting() {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        _greeting.value = when (hour) {
            in 0..10 -> "Selamat Pagi"
            in 11..14 -> "Selamat Siang"
            in 15..17 -> "Selamat Sore"
            else -> "Selamat Malam"
        }
    }
    
    private fun loadTodayChecklist() {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale("id")).format(Date())
            checklistDao.getChecklist(today).collect { checklist ->
                if (checklist != null) {
                    _todayChecklist.value = mapOf(
                        "subuh" to checklist.sholatSubuh,
                        "dzuhur" to checklist.sholatDzuhur,
                        "ashar" to checklist.sholatAshar,
                        "maghrib" to checklist.sholatMaghrib,
                        "isya" to checklist.sholatIsya,
                        "dhuha" to checklist.sholatDhuha
                    )
                }
            }
        }
    }
    
    private fun loadPetStatus() {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    val pet = pets.first()
                    _petStatus.value = Pet.PetState(
                        name = pet.name,
                        type = pet.type,
                        hunger = pet.hunger,
                        happiness = pet.happiness,
                        cleanliness = pet.cleanliness,
                        energy = pet.energy,
                        level = pet.level,
                        experience = pet.experience,
                        isSick = pet.isSick
                    )
                }
            }
        }
    }
    
    private fun loadCalendarColors() {
        viewModelScope.launch {
            checklistDao.getLast30Days().collect { checklists ->
                val colors = mutableMapOf<String, String>()
                checklists.forEach { checklist ->
                    val color = when {
                        checklist.totalPoints >= 90 -> "green"
                        checklist.totalPoints >= 70 -> "yellow"
                        else -> "red"
                    }
                    colors[checklist.date] = color
                }
                _calendarColors.value = colors
            }
        }
    }
    
    fun togglePrayer(prayer: String) {
        viewModelScope.launch {
            val today = SimpleDateFormat("yyyy-MM-dd", Locale("id")).format(Date())
            checklistDao.getChecklist(today).collect { checklist ->
                val updated = checklist?.let {
                    when (prayer) {
                        "subuh" -> it.copy(sholatSubuh = !it.sholatSubuh)
                        "dzuhur" -> it.copy(sholatDzuhur = !it.sholatDzuhur)
                        "ashar" -> it.copy(sholatAshar = !it.sholatAshar)
                        "maghrib" -> it.copy(sholatMaghrib = !it.sholatMaghrib)
                        "isya" -> it.copy(sholatIsya = !it.sholatIsya)
                        "dhuha" -> it.copy(sholatDhuha = !it.sholatDhuha)
                        else -> it
                    }
                } ?: DailyChecklist(date = today)
                
                checklistDao.insert(updated)
                
                // Add points and iman
                if (updated.totalPoints > (checklist?.totalPoints ?: 0)) {
                    userDao.addPoints(10)
                    userDao.updateIman(1)
                }
            }
        }
    }
    
    fun feedPet() {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    val pet = pets.first()
                    petDao.decreaseHunger(pet.id, 20)
                    petDao.increaseHappiness(pet.id, 5)
                    userDao.addPoints(5)
                }
            }
        }
    }
    
    fun playWithPet() {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    val pet = pets.first()
                    petDao.increaseHappiness(pet.id, 20)
                    userDao.addPoints(10)
                }
            }
        }
    }
    
    fun cleanPet() {
        viewModelScope.launch {
            petDao.getPets().collect { pets ->
                if (pets.isNotEmpty()) {
                    val pet = pets.first()
                    petDao.cleanPet(pet.id)
                    userDao.addPoints(5)
                }
            }
        }
    }
}
