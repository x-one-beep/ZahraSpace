package com.zahra.space.ui.screens.profile

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
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
import com.zahra.space.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(onNavigateToSettings: () -> Unit) {
    val viewModel: ProfileViewModel = hiltViewModel()
    val userName by viewModel.userName.collectAsState()
    val userAge by viewModel.userAge.collectAsState()
    val totalPoints by viewModel.totalPoints.collectAsState()
    val streak by viewModel.streak.collectAsState()
    val level by viewModel.level.collectAsState()
    val daysSinceInstall by viewModel.daysSinceInstall.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Profile Header with SVG-style character
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Character representation (simple avatar)
                Box(
                    modifier = Modifier
                        .width(100.dp)
                        .height(120.dp)
                ) {
                    // Hijab (black)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color.Black,
                                shape = androidx.compose.foundation.shape.RoundedCornerShape(50, 50, 20, 20)
                            )
                    )
                    // Face (skin tone)
                    Box(
                        modifier = Modifier
                            .width(60.dp)
                            .height(70.dp)
                            .align(Alignment.TopCenter)
                            .offset(y = 15.dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color(0xFFE2A98C),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    // Eyes
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.Center)
                            .offset(x = (-10).dp, y = (-5).dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color.Black,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .align(Alignment.Center)
                            .offset(x = 10.dp, y = (-5).dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color.Black,
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    // Cheeks
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .align(Alignment.Center)
                            .offset(x = (-18).dp, y = 5.dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color(0xFFF0A998).copy(alpha = 0.4f),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .align(Alignment.Center)
                            .offset(x = 18.dp, y = 5.dp)
                            .background(
                                color = androidx.compose.ui.graphics.Color(0xFFF0A998).copy(alpha = 0.4f),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                    )
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(userName, style = MaterialTheme.typography.headlineMedium)
                    Text("$userAge tahun", style = MaterialTheme.typography.bodyLarge)
                    Text("Bergabung: $daysSinceInstall hari yang lalu", 
                         style = MaterialTheme.typography.bodySmall)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Stats Card
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                StatCard("Poin", "$totalPoints ✨")
                StatCard("Streak", "$streak 🔥")
                StatCard("Level", "$level")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Special Stats
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text("📊 Statistik Khusus", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                
                StatRow("Hari tanpa chat", "$daysSinceInstall hari")
                StatRow("Doa terkirim", "${daysSinceInstall * 100}")
                StatRow("Pesan tersembunyi", "0")
                StatRow("Surat dari F", "0")
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Achievements
        Text("🏆 Pencapaian", style = MaterialTheme.typography.titleLarge)
        
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(achievements) { achievement ->
                AchievementBadge(achievement)
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = onNavigateToSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("⚙️ Pengaturan")
        }
    }
}

@Composable
fun StatCard(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, style = MaterialTheme.typography.titleLarge)
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
fun StatRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun AchievementBadge(achievement: Achievement) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(achievement.icon, fontSize = 24.sp)
            Text(achievement.name, fontSize = 10.sp, maxLines = 2)
        }
    }
}

data class Achievement(
    val name: String,
    val icon: String
)

val achievements = listOf(
    Achievement("Rajin", "🌟"),
    Achievement("Istiqomah", "🔥"),
    Achievement("Hafidzah", "📖"),
    Achievement("Shalihah", "🌸"),
    Achievement("Chef", "🍳"),
    Achievement("Peduli", "💝"),
    Achievement("Fajar", "✨"),
    Achievement("Zahra", "🌙")
)
