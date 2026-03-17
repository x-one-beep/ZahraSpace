package com.zahra.space.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zahra.space.data.dao.PetDao
import com.zahra.space.data.dao.UserDao
import com.zahra.space.data.entity.Pet
import com.zahra.space.data.entity.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val userDao: UserDao,
    private val petDao: PetDao
) : ViewModel() {
    
    private var installDate: Long = System.currentTimeMillis()
    
    fun setInstallDate(date: Long) {
        installDate = date
    }
    
    fun saveUserData(
        name: String,
        age: Int,
        avatar: String,
        petType: String,
        installDate: Long
    ) {
        viewModelScope.launch {
            // Create user
            val user = User(
                id = "1",
                name = name,
                age = age,
                avatar = avatar,
                petType = petType,
                petName = when (petType) {
                    "cat" -> "Luna"
                    "rabbit" -> "Miya"
                    else -> "Mochi"
                },
                installDate = installDate,
                lastActive = System.currentTimeMillis(),
                hasCompletedOnboarding = true,
                totalPoints = 0,
                streak = 0,
                imanLevel = 50
            )
            userDao.insert(user)
            
            // Create pet
            val pet = Pet(
                name = when (petType) {
                    "cat" -> "Luna"
                    "rabbit" -> "Miya"
                    else -> "Mochi"
                },
                type = petType,
                hunger = 50,
                happiness = 50,
                cleanliness = 50,
                energy = 80
            )
            petDao.insert(pet)
        }
    }
}
