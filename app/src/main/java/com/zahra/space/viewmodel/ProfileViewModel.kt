package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.UserDao
import com.zahra.space.data.dao.HiddenMessageDao
import com.zahra.space.data.dao.MonthlyLetterDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao,
    private val hiddenMessageDao: HiddenMessageDao,
    private val monthlyLetterDao: MonthlyLetterDao
) : ViewModel() {
    
    private val _userName = MutableStateFlow("Zahra")
    val userName: StateFlow<String> = _userName
    
    private val _userAge = MutableStateFlow(25)
    val userAge: StateFlow<Int> = _userAge
    
    private val _totalPoints = MutableStateFlow(0L)
    val totalPoints: StateFlow<Long> = _totalPoints
    
    private val _streak = MutableStateFlow(0)
    val streak: StateFlow<Int> = _streak
    
    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level
    
    private val _imanLevel = MutableStateFlow(50)
    val imanLevel: StateFlow<Int> = _imanLevel
    
    private val _daysSinceInstall = MutableStateFlow(0)
    val daysSinceInstall: StateFlow<Int> = _daysSinceInstall
    
    private val _hiddenMessagesFound = MutableStateFlow(0)
    val hiddenMessagesFound: StateFlow<Int> = _hiddenMessagesFound
    
    private val _lettersRead = MutableStateFlow(0)
    val lettersRead: StateFlow<Int> = _lettersRead
    
    private val _doaSent = MutableStateFlow(0)
    val doaSent: StateFlow<Int> = _doaSent
    
    init {
        loadUserData()
        loadStats()
    }
    
    private fun loadUserData() {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _userName.value = user.name.ifEmpty { "Zahra" }
                _userAge.value = user.age
                _totalPoints.value = user.totalPoints
                _streak.value = user.streak
                _imanLevel.value = user.imanLevel
                
                // Calculate level based on points
                val levelCalc = (user.totalPoints / 10000).toInt() + 1
                _level.value = levelCalc.coerceIn(1, 100)
                
                // Calculate days since install
                val days = TimeUnit.MILLISECONDS.toDays(
                    System.currentTimeMillis() - user.installDate
                ).toInt()
                _daysSinceInstall.value = days.coerceAtLeast(0)
                
                // Calculate doa sent (1 doa per day since install)
                _doaSent.value = days.coerceAtLeast(0) * 100
            }
        }
    }
    
    private fun loadStats() {
        viewModelScope.launch {
            hiddenMessageDao.getFoundCount().collect { count ->
                _hiddenMessagesFound.value = count
            }
        }
        
        viewModelScope.launch {
            monthlyLetterDao.getAllLetters().collect { letters ->
                _lettersRead.value = letters.count { it.isRead }
            }
        }
    }
}
