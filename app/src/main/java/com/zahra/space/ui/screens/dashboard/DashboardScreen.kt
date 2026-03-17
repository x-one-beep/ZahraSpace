package com.zahra.space.ui.screens.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zahra.space.viewmodel.DashboardViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun DashboardScreen(
    onNavigateToQuran: () -> Unit,
    onNavigateToDzikir: () -> Unit,
    onNavigateToChecklist: () -> Unit,
    onNavigateToTodo: () -> Unit,
    onNavigateToFitness: () -> Unit,
    onNavigateToPet: () -> Unit,
    onNavigateToGame: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onNavigateToGameWorld: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val userName by viewModel.userName.collectAsState()
    val totalPoints by viewModel.totalPoints.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val imanLevel by viewModel.imanLevel.collectAsState()
    
    val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("id"))
    val currentDate = dateFormat.format(Date())
    val currentTime = SimpleDateFormat("HH:mm", Locale("id")).format(Date())
    
    val greeting = when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
        in 0..10 -> "Selamat Pagi"
        in 11..14 -> "Selamat Siang"
        in 15..17 -> "Selamat Sore"
        else -> "Selamat Malam"
    }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "$greeting,",
                    style = MaterialTheme.typography.headlineSmall
                )
                Text(
                    text = userName,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = currentTime,
                style = MaterialTheme.typography.titleLarge
            )
        }
        
        Text(
            text = currentDate,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Iman & Points Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Iman: ${imanLevel}%", style = MaterialTheme.typography.titleMedium)
                    Text("✨ Poin: $totalPoints", style = MaterialTheme.typography.titleMedium)
                }
                Spacer(modifier = Modifier.height(4.dp))
                
                // Iman progress bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .background(
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                            shape = MaterialTheme.shapes.small
                        )
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(imanLevel / 100f)
                            .height(8.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primary,
                                shape = MaterialTheme.shapes.small
                            )
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatItem("Poin", "$totalPoints ✨")
                    StatItem("Streak", "$streak 🔥")
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Calendar Preview
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("📅 Maret 2026", style = MaterialTheme.typography.titleMedium)
                    Text("🟢🟡🔴", style = MaterialTheme.typography.bodySmall)
                }
                Text(
                    text = "Su Mo Tu We Th Fr Sa",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "    🟢 🟡 🟢 🟢 🟢 🟢",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "🟢 🟢 🟢 🟢 🟡 🟢 🟢",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "🟡 🟢 🟢 🟢 🟢 🟢 🟢",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "🟢 🟢 🔴 🟢 🟢 🟢 🟡",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Prayer Checklist
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(12.dp)
            ) {
                Text("✅ Sholat Hari Ini", style = MaterialTheme.typography.titleMedium)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PrayerButton("Subuh", false)
                    PrayerButton("Dzuhur", false)
                    PrayerButton("Ashar", false)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PrayerButton("Maghrib", false)
                    PrayerButton("Isya", false)
                    PrayerButton("Dhuha", false)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Haid Tracker Preview
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("🌸 Haid Tracker", style = MaterialTheme.typography.titleMedium)
                    Text("Belum ada data", style = MaterialTheme.typography.bodySmall)
                }
                Text("📝", fontSize = 24.sp)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Pet Preview
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("🐱 Luna", style = MaterialTheme.typography.titleMedium)
                    Text("Lapar: 50% · Senang: 50%", style = MaterialTheme.typography.bodySmall)
                }
                Row {
                    Text("🍖", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 4.dp))
                    Text("🎾", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 4.dp))
                    Text("🧼", fontSize = 20.sp, modifier = Modifier.padding(horizontal = 4.dp))
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Menu Grid
        val menuItems = listOf(
            MenuItem("Quran", "📖", onNavigateToQuran),
            MenuItem("Dzikir", "📿", onNavigateToDzikir),
            MenuItem("Checklist", "✅", onNavigateToChecklist),
            MenuItem("Todo", "📋", onNavigateToTodo),
            MenuItem("Fitness", "💪", onNavigateToFitness),
            MenuItem("Pet", "🐱", onNavigateToPet),
            MenuItem("Game", "🎮", onNavigateToGame),
            MenuItem("Profil", "👤", onNavigateToProfile)
        )
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.weight(1f)
        ) {
            items(menuItems) { item ->
                MenuCard(item)
            }
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Game World Button
        Card(
            modifier = Modifier.fillMaxWidth(),
            onClick = onNavigateToGameWorld
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("🎮", fontSize = 24.sp, modifier = Modifier.padding(end = 8.dp))
                Text("Masuk ke Dunia Game", style = MaterialTheme.typography.titleMedium)
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, style = MaterialTheme.typography.titleLarge)
        Text(text = label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun PrayerButton(name: String, isDone: Boolean) {
    Card(
        modifier = Modifier.size(60.dp, 40.dp),
        colors = androidx.compose.material3.CardDefaults.cardColors(
            containerColor = if (isDone) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
                            else MaterialTheme.colorScheme.surface
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = name.take(1),
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

data class MenuItem(val title: String, val icon: String, val onClick: () -> Unit)

@Composable
fun MenuCard(item: MenuItem) {
    Card(
        onClick = item.onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(8.dp)
        ) {
            Text(text = item.icon, fontSize = 20.sp)
            Text(text = item.title, style = MaterialTheme.typography.bodySmall, fontSize = 10.sp)
        }
    }
}
