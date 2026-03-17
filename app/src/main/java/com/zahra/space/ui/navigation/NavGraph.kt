package com.zahra.space.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.dialog
import com.zahra.space.ui.screens.splash.SplashScreen
import com.zahra.space.ui.screens.onboarding.OnboardingScreen
import com.zahra.space.ui.screens.dashboard.DashboardScreen
import com.zahra.space.ui.screens.quran.QuranHomeScreen
import com.zahra.space.ui.screens.quran.QuranReadScreen
import com.zahra.space.ui.screens.quran.QuranHafalanScreen
import com.zahra.space.ui.screens.dzikir.DzikirHomeScreen
import com.zahra.space.ui.screens.dzikir.DzikirCounterScreen
import com.zahra.space.ui.screens.checklist.ChecklistScreen
import com.zahra.space.ui.screens.todo.TodoHomeScreen
import com.zahra.space.ui.screens.todo.TodoDetailScreen
import com.zahra.space.ui.screens.todo.TodoCreateScreen
import com.zahra.space.ui.screens.fitness.FitnessHomeScreen
import com.zahra.space.ui.screens.fitness.FitnessTargetScreen
import com.zahra.space.ui.screens.fitness.FitnessLogScreen
import com.zahra.space.ui.screens.pet.PetHomeScreen
import com.zahra.space.ui.screens.pet.PetDetailScreen
import com.zahra.space.ui.screens.pet.PetShopScreen
import com.zahra.space.ui.screens.game.GameHomeScreen
import com.zahra.space.ui.screens.game.GameWorldScreen
import com.zahra.space.ui.screens.profile.ProfileScreen
import com.zahra.space.ui.screens.profile.SettingsScreen

@Composable
fun NavGraph(
    navController: NavHostController,
    startDestination: String = Screen.Splash.route
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onTimeout = {
                    navController.navigate(Screen.Onboarding.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Onboarding.route) {
            OnboardingScreen(
                onComplete = {
                    navController.navigate(Screen.Dashboard.route) {
                        popUpTo(Screen.Onboarding.route) { inclusive = true }
                    }
                }
            )
        }
        
        composable(Screen.Dashboard.route) {
            DashboardScreen(
                onNavigateToQuran = { navController.navigate(Screen.Quran.route) },
                onNavigateToDzikir = { navController.navigate(Screen.Dzikir.route) },
                onNavigateToChecklist = { navController.navigate(Screen.Checklist.route) },
                onNavigateToTodo = { navController.navigate(Screen.Todo.route) },
                onNavigateToFitness = { navController.navigate(Screen.Fitness.route) },
                onNavigateToPet = { navController.navigate(Screen.Pet.route) },
                onNavigateToGame = { navController.navigate(Screen.Game.route) },
                onNavigateToProfile = { navController.navigate(Screen.Profile.route) },
                onNavigateToGameWorld = { navController.navigate(Screen.GameWorld.route) }
            )
        }
        
        composable(Screen.Quran.route) {
            QuranHomeScreen(
                onNavigateToRead = { surahId, ayat ->
                    navController.navigate("quran_read/$surahId/$ayat")
                },
                onNavigateToHafalan = { surahId ->
                    navController.navigate("quran_hafalan/$surahId")
                }
            )
        }
        
        composable("quran_read/{surahId}/{ayat}") { backStackEntry ->
            val surahId = backStackEntry.arguments?.getString("surahId")?.toIntOrNull() ?: 1
            val ayat = backStackEntry.arguments?.getString("ayat")?.toIntOrNull() ?: 1
            QuranReadScreen(surahId = surahId, ayatNumber = ayat)
        }
        
        composable("quran_hafalan/{surahId}") { backStackEntry ->
            val surahId = backStackEntry.arguments?.getString("surahId")?.toIntOrNull() ?: 1
            QuranHafalanScreen(surahId = surahId)
        }
        
        composable(Screen.Dzikir.route) {
            DzikirHomeScreen(
                onNavigateToCounter = { dzikirId ->
                    navController.navigate("dzikir_counter/$dzikirId")
                }
            )
        }
        
        composable("dzikir_counter/{dzikirId}") { backStackEntry ->
            val dzikirId = backStackEntry.arguments?.getString("dzikirId")?.toLongOrNull() ?: 1
            DzikirCounterScreen(dzikirId = dzikirId)
        }
        
        composable(Screen.Checklist.route) {
            ChecklistScreen()
        }
        
        composable(Screen.Todo.route) {
            TodoHomeScreen(
                onNavigateToDetail = { todoId ->
                    navController.navigate("todo_detail/$todoId")
                },
                onNavigateToCreate = {
                    navController.navigate("todo_create")
                }
            )
        }
        
        composable("todo_detail/{todoId}") { backStackEntry ->
            val todoId = backStackEntry.arguments?.getString("todoId")?.toLongOrNull() ?: 0
            TodoDetailScreen(todoId = todoId)
        }
        
        composable("todo_create") {
            TodoCreateScreen(
                onSave = {
                    navController.popBackStack()
                }
            )
        }
        
        composable(Screen.Fitness.route) {
            FitnessHomeScreen(
                onNavigateToTarget = { targetId ->
                    navController.navigate("fitness_target/$targetId")
                },
                onNavigateToLog = {
                    navController.navigate("fitness_log")
                }
            )
        }
        
        composable("fitness_target/{targetId}") { backStackEntry ->
            val targetId = backStackEntry.arguments?.getString("targetId")?.toLongOrNull() ?: 0
            FitnessTargetScreen(targetId = targetId)
        }
        
        composable("fitness_log") {
            FitnessLogScreen()
        }
        
        composable(Screen.Pet.route) {
            PetHomeScreen(
                onNavigateToDetail = { petId ->
                    navController.navigate("pet_detail/$petId")
                },
                onNavigateToShop = {
                    navController.navigate("pet_shop")
                }
            )
        }
        
        composable("pet_detail/{petId}") { backStackEntry ->
            val petId = backStackEntry.arguments?.getString("petId")?.toLongOrNull() ?: 0
            PetDetailScreen(petId = petId)
        }
        
        composable("pet_shop") {
            PetShopScreen()
        }
        
        composable(Screen.Game.route) {
            GameHomeScreen(
                onNavigateToGameWorld = { navController.navigate(Screen.GameWorld.route) }
            )
        }
        
        composable(Screen.GameWorld.route) {
            GameWorldScreen()
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(
                onNavigateToSettings = { navController.navigate(Screen.Settings.route) }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen()
        }
    }
}
