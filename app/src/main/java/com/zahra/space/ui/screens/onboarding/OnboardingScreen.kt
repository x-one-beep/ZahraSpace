package com.zahra.space.ui.screens.onboarding

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.zahra.space.viewmodel.OnboardingViewModel

@Composable
fun OnboardingScreen(
    onComplete: () -> Unit,
    viewModel: OnboardingViewModel = hiltViewModel()
) {
    var currentStep by remember { mutableIntStateOf(1) }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var selectedAvatar by remember { mutableStateOf("👩") }
    var selectedPet by remember { mutableStateOf("cat") }
    var installDate by remember { mutableStateOf(System.currentTimeMillis()) }
    
    val avatars = listOf("👧", "👩", "🧕")
    val pets = listOf(
        Triple("cat", "🐱 Luna", "Lembut, manja, suka bobo di sajadah"),
        Triple("rabbit", "🐰 Miya", "Lincah, cepat, suka lompat-lompat"),
        Triple("hamster", "🐹 Mochi", "Rajin, suka nyimpen makanan, gemesin")
    )
    
    LaunchedEffect(Unit) {
        viewModel.setInstallDate(installDate)
    }
    
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        item {
            when (currentStep) {
                1 -> {
                    Text(
                        text = "Assalamu'alaikum, Zahra! 👋",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    Text(
                        text = "Selamat datang di ruang spesialmu.\nTempat kamu tumbuh, belajar, dan bahagia.",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                
                2 -> {
                    Text(
                        text = "Lengkapi Profil",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text("Nama panggilan") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    OutlinedTextField(
                        value = age,
                        onValueChange = { age = it },
                        label = { Text("Usia") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Pilih Avatar:")
                    Row {
                        avatars.forEach { avatar ->
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                RadioButton(
                                    selected = selectedAvatar == avatar,
                                    onClick = { selectedAvatar = avatar }
                                )
                                Text(avatar, fontSize = 40.sp)
                            }
                        }
                    }
                }
                
                3 -> {
                    Text(
                        text = "Pilih Teman Virtualmu",
                        style = MaterialTheme.typography.headlineMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    pets.forEach { (petId, name, desc) ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            onClick = { selectedPet = petId }
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = selectedPet == petId,
                                    onClick = { selectedPet = petId }
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text(name, style = MaterialTheme.typography.titleMedium)
                                    Text(desc, style = MaterialTheme.typography.bodySmall)
                                }
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            Row {
                if (currentStep > 1) {
                    Button(
                        onClick = { currentStep-- },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("Kembali")
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }
                
                Button(
                    onClick = {
                        if (currentStep < 3) {
                            currentStep++
                        } else {
                            viewModel.saveUserData(
                                name = name,
                                age = age.toIntOrNull() ?: 0,
                                avatar = selectedAvatar,
                                petType = selectedPet,
                                installDate = installDate
                            )
                            onComplete()
                        }
                    },
                    modifier = Modifier.weight(1f),
                    enabled = when (currentStep) {
                        2 -> name.isNotBlank() && age.isNotBlank()
                        else -> true
                    }
                ) {
                    Text(if (currentStep < 3) "Lanjut" else "Mulai Petualangan")
                }
            }
        }
    }
}
