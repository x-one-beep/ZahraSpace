package com.zahra.space.game

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class Ingredient(
    val name: String,
    val price: Int,
    var quantity: Int
)

data class Recipe(
    val id: Int,
    val name: String,
    val description: String,
    val ingredients: Map<String, Int>,
    val basePrice: Int,
    val cookingTime: Int, // in seconds
    val experienceReward: Int,
    val levelRequired: Int
)

data class Order(
    val id: Int,
    val recipeId: Int,
    val customerName: String,
    val timeLimit: Int, // in seconds
    val reward: Int,
    var timeLeft: Int
)

data class RestaurantState(
    val level: Int = 1,
    val experience: Int = 0,
    val balance: Int = 1000,
    val reputation: Int = 50,
    val employeeCount: Int = 0,
    val tables: Int = 4,
    val inventory: Map<String, Int> = mapOf(
        "rice" to 20,
        "egg" to 30,
        "chicken" to 15,
        "beef" to 10,
        "flour" to 25,
        "sugar" to 20,
        "oil" to 15,
        "vegetable" to 20
    ),
    val currentOrders: List<Order> = emptyList(),
    val completedOrders: Int = 0,
    val totalCustomers: Int = 0
)

class Restaurant {
    private val _state = MutableStateFlow(RestaurantState())
    val state: StateFlow<RestaurantState> = _state.asStateFlow()
    
    val recipes = listOf(
        Recipe(1, "Nasi Goreng", "Nasi goreng khas Indonesia", 
               mapOf("rice" to 1, "egg" to 1, "oil" to 1), 50, 30, 10, 1),
        Recipe(2, "Telur Ceplok", "Telur mata sapi sederhana",
               mapOf("egg" to 1, "oil" to 1), 20, 15, 5, 1),
        Recipe(3, "Ayam Goreng", "Ayam goreng krispi",
               mapOf("chicken" to 1, "flour" to 1, "oil" to 1), 80, 45, 15, 2),
        Recipe(4, "Rendang", "Rendang padang asli",
               mapOf("beef" to 2, "vegetable" to 2), 200, 120, 30, 5),
        Recipe(5, "Sate Ayam", "Sate ayam dengan bumbu kacang",
               mapOf("chicken" to 2, "vegetable" to 1), 150, 60, 20, 3),
        Recipe(6, "Gado-gado", "Salad sayur dengan bumbu kacang",
               mapOf("vegetable" to 3, "egg" to 1), 100, 40, 15, 2),
        Recipe(7, "Soto Ayam", "Sup ayam dengan rempah",
               mapOf("chicken" to 1, "vegetable" to 2), 120, 50, 18, 3),
        Recipe(8, "Mie Goreng", "Mie goreng spesial",
               mapOf("flour" to 2, "egg" to 1, "oil" to 1), 60, 35, 12, 2),
        Recipe(9, "Bakso", "Bakso sapi dengan kuah",
               mapOf("beef" to 1, "flour" to 1), 90, 40, 14, 3),
        Recipe(10, "Nasi Uduk", "Nasi uduk dengan lauk",
               mapOf("rice" to 1, "chicken" to 1, "egg" to 1), 110, 40, 16, 4)
    )
    
    // Cook a recipe
    fun cook(recipeId: Int): Int {
        val recipe = recipes.find { it.id == recipeId } ?: return 0
        val current = _state.value
        
        // Check if level required is met
        if (current.level < recipe.levelRequired) {
            return -1 // Level too low
        }
        
        // Check ingredients
        val canCook = recipe.ingredients.all { (ingredient, amount) ->
            (current.inventory[ingredient] ?: 0) >= amount
        }
        
        if (!canCook) {
            return -2 // Insufficient ingredients
        }
        
        // Consume ingredients
        val newInventory = current.inventory.toMutableMap()
        recipe.ingredients.forEach { (ingredient, amount) ->
            newInventory[ingredient] = (newInventory[ingredient] ?: 0) - amount
        }
        
        // Calculate quality based on level and randomness
        val quality = (1..5).random()
        val priceMultiplier = when (quality) {
            1 -> 0.5
            2 -> 0.8
            3 -> 1.0
            4 -> 1.2
            5 -> 1.5
            else -> 1.0
        }
        
        val finalPrice = (recipe.basePrice * priceMultiplier).toInt()
        
        // Add experience
        val newExp = current.experience + recipe.experienceReward
        val newLevel = (newExp / 500) + 1
        
        _state.value = current.copy(
            inventory = newInventory,
            balance = current.balance + finalPrice,
            experience = newExp,
            level = newLevel.coerceIn(1, 20),
            completedOrders = current.completedOrders + 1
        )
        
        return finalPrice
    }
    
    // Generate random order
    fun generateOrder(): Order? {
        val current = _state.value
        val availableRecipes = recipes.filter { it.levelRequired <= current.level }
        if (availableRecipes.isEmpty()) return null
        
        val recipe = availableRecipes.random()
        val customers = listOf("Ibu Tuti", "Pak Ahmad", "Bu Rina", "Mas Budi", "Dina", "Rizki")
        
        return Order(
            id = current.totalCustomers + 1,
            recipeId = recipe.id,
            customerName = customers.random(),
            timeLimit = recipe.cookingTime * 2,
            reward = recipe.basePrice * 2,
            timeLeft = recipe.cookingTime * 2
        )
    }
    
    // Add order to queue
    fun addOrder(order: Order) {
        val current = _state.value
        _state.value = current.copy(
            currentOrders = current.currentOrders + order,
            totalCustomers = current.totalCustomers + 1
        )
    }
    
    // Complete order
    fun completeOrder(orderId: Int): Int {
        val current = _state.value
        val order = current.currentOrders.find { it.id == orderId } ?: return 0
        
        val recipe = recipes.find { it.id == order.recipeId } ?: return 0
        
        // Check ingredients
        val canCook = recipe.ingredients.all { (ingredient, amount) ->
            (current.inventory[ingredient] ?: 0) >= amount
        }
        
        if (!canCook) {
            return -2 // Insufficient ingredients
        }
        
        // Consume ingredients
        val newInventory = current.inventory.toMutableMap()
        recipe.ingredients.forEach { (ingredient, amount) ->
            newInventory[ingredient] = (newInventory[ingredient] ?: 0) - amount
        }
        
        // Calculate reward
        val finalReward = order.reward
        
        // Add experience
        val newExp = current.experience + recipe.experienceReward * 2
        val newLevel = (newExp / 500) + 1
        
        _state.value = current.copy(
            currentOrders = current.currentOrders.filter { it.id != orderId },
            inventory = newInventory,
            balance = current.balance + finalReward,
            experience = newExp,
            level = newLevel.coerceIn(1, 20),
            completedOrders = current.completedOrders + 1
        )
        
        return finalReward
    }
    
    // Buy ingredients
    fun buyIngredients(ingredient: String, amount: Int): Boolean {
        val current = _state.value
        val price = when (ingredient) {
            "rice" -> 10
            "egg" -> 8
            "chicken" -> 25
            "beef" -> 30
            "flour" -> 5
            "sugar" -> 5
            "oil" -> 7
            "vegetable" -> 6
            else -> 0
        }
        
        val totalCost = price * amount
        if (current.balance < totalCost) {
            return false
        }
        
        val newInventory = current.inventory.toMutableMap()
        newInventory[ingredient] = (newInventory[ingredient] ?: 0) + amount
        
        _state.value = current.copy(
            inventory = newInventory,
            balance = current.balance - totalCost
        )
        
        return true
    }
    
    // Hire employee
    fun hireEmployee(): Boolean {
        val current = _state.value
        val cost = 500 * (current.employeeCount + 1)
        
        if (current.balance < cost) {
            return false
        }
        
        _state.value = current.copy(
            employeeCount = current.employeeCount + 1,
            balance = current.balance - cost,
            tables = current.tables + 2 // Each employee can handle 2 more tables
        )
        
        return true
    }
    
    // Upgrade restaurant
    fun upgrade(): Boolean {
        val current = _state.value
        val cost = 1000 * current.level
        
        if (current.balance < cost) {
            return false
        }
        
        _state.value = current.copy(
            level = current.level + 1,
            balance = current.balance - cost,
            tables = current.tables + 2
        )
        
        return true
    }
    
    // Update time (for order time limits)
    fun updateTime(seconds: Int) {
        val current = _state.value
        val updatedOrders = current.currentOrders.map { order ->
            order.copy(timeLeft = order.timeLeft - seconds)
        }.filter { it.timeLeft > 0 }
        
        _state.value = current.copy(currentOrders = updatedOrders)
    }
}
