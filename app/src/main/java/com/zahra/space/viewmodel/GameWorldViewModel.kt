package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.UserDao
import com.zahra.space.data.dao.HiddenMessageDao
import com.zahra.space.data.dao.RestaurantDao
import com.zahra.space.game.Restaurant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GameWorldViewModel @Inject constructor(
    private val userDao: UserDao,
    private val hiddenMessageDao: HiddenMessageDao,
    private val restaurantDao: RestaurantDao
) : ViewModel() {
    
    private val _balance = MutableStateFlow(0)
    val balance: StateFlow<Int> = _balance
    
    private val _gameTime = MutableStateFlow(0L)
    val gameTime: StateFlow<Long> = _gameTime
    
    private val _weather = MutableStateFlow("Cerah")
    val weather: StateFlow<String> = _weather
    
    private val _currentPrayer = MutableStateFlow("")
    val currentPrayer: StateFlow<String> = _currentPrayer
    
    private val _showPrayerNotif = MutableStateFlow(false)
    val showPrayerNotif: StateFlow<Boolean> = _showPrayerNotif
    
    private val _hiddenMessagesCount = MutableStateFlow(0)
    val hiddenMessagesCount: StateFlow<Int> = _hiddenMessagesCount
    
    private val _totalMessages = MutableStateFlow(0)
    val totalMessages: StateFlow<Int> = _totalMessages
    
    private val _restaurant = MutableStateFlow(Restaurant().state.value)
    val restaurant: StateFlow<Restaurant.RestaurantState> = _restaurant
    
    init {
        loadUserBalance()
        loadHiddenMessagesCount()
        startGameTime()
        startWeather()
        loadRestaurant()
    }
    
    private fun loadUserBalance() {
        viewModelScope.launch {
            userDao.getUser().collect { user ->
                _balance.value = user.totalPoints.toInt()
            }
        }
    }
    
    private fun loadHiddenMessagesCount() {
        viewModelScope.launch {
            hiddenMessageDao.getFoundCount().collect { count ->
                _hiddenMessagesCount.value = count
            }
        }
        // Total messages would be loaded from database
        _totalMessages.value = 1000
    }
    
    private fun startGameTime() {
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(60000) // 1 real minute
                _gameTime.value += 3600000 // 1 game hour
                
                // Check prayer times (every 6 game hours)
                val gameHour = (_gameTime.value / 3600000) % 24
                if (gameHour.toInt() % 6 == 0 && !_showPrayerNotif.value) {
                    _currentPrayer.value = when (gameHour.toInt()) {
                        6 -> "Subuh"
                        12 -> "Dzuhur"
                        18 -> "Ashar"
                        0 -> "Maghrib"
                        5 -> "Isya"
                        else -> ""
                    }
                    if (_currentPrayer.value.isNotEmpty()) {
                        _showPrayerNotif.value = true
                    }
                }
            }
        }
    }
    
    private fun startWeather() {
        val weathers = listOf("Cerah", "Berawan", "Hujan Ringan", "Hujan Deras", "Mendung")
        viewModelScope.launch {
            while (true) {
                kotlinx.coroutines.delay(300000) // Change every 5 real minutes
                _weather.value = weathers.random()
            }
        }
    }
    
    private fun loadRestaurant() {
        viewModelScope.launch {
            restaurantDao.getRestaurant().collect { restaurant ->
                if (restaurant != null) {
                    _restaurant.value = Restaurant.RestaurantState(
                        level = restaurant.level,
                        experience = restaurant.experience,
                        balance = restaurant.balance,
                        reputation = restaurant.reputation,
                        employeeCount = restaurant.employeeCount,
                        tables = restaurant.tables,
                        completedOrders = restaurant.completedOrders,
                        totalCustomers = restaurant.totalCustomers
                    )
                }
            }
        }
    }
    
    fun findHiddenMessage(location: String) {
        viewModelScope.launch {
            hiddenMessageDao.getMessagesByLocation(location).collect { messages ->
                val unreadMessages = messages.filter { !it.isFound }
                if (unreadMessages.isNotEmpty()) {
                    val message = unreadMessages.random()
                    hiddenMessageDao.update(message.copy(isFound = true))
                    userDao.addPoints(message.pointsReward)
                }
            }
        }
    }
    
    fun dismissPrayerNotif() {
        _showPrayerNotif.value = false
    }
    
    fun teleportToMosque() {
        // Update user points for praying
        viewModelScope.launch {
            userDao.addPoints(50)
            userDao.updateIman(5)
        }
        _showPrayerNotif.value = false
    }
    
    fun updateBalance(amount: Int) {
        viewModelScope.launch {
            userDao.addPoints(amount)
            _balance.value += amount
        }
    }
}
