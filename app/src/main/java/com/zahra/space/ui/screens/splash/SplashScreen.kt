package com.zahra.space.ui.screens.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zahra.space.ui.theme.IslamicGold
import com.zahra.space.ui.theme.IslamicGreen
import com.zahra.space.ui.theme.IslamicGreenDark
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    val infiniteTransition = rememberInfiniteTransition()
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing)
        )
    )
    
    val quotes = listOf(
        "Untuk Zahra",
        "400 MB ini bukan sekadar data",
        "Setiap byte-nya adalah doa",
        "Setiap baris kodenya adalah harapan",
        "Setiap animasinya adalah rasa",
        "Aku tidak pernah berharap kau membalas",
        "Aku hanya berharap, suatu hari",
        "di surga nanti, kita bisa tersenyum",
        "dan berkata: Alhamdulillah, kita di sini"
    )
    
    var currentQuote by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            delay(400)
            currentQuote = (currentQuote + 1) % quotes.size
        }
    }
    
    LaunchedEffect(Unit) {
        delay(4000)
        onTimeout()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(IslamicGreenDark, IslamicGreen)
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            // Arabic text
            Text(
                text = "زَاحِرَة سْبَيْس",
                color = IslamicGold.copy(alpha = 0.7f),
                fontFamily = FontFamily.Serif,
                fontSize = 24.sp,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            
            // App name
            Text(
                text = "ZAHRA SPACE",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            // Animated star
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .rotate(rotation)
            ) {
                Text(
                    text = "⭐",
                    fontSize = 64.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Loading dots
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(3) { index ->
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .background(
                                color = if (index == 0) IslamicGold else IslamicGold.copy(alpha = 0.5f),
                                shape = CircleShape
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            
            Spacer(modifier = Modifier.height(48.dp))
            
            // Created by
            Text(
                text = "created by",
                fontSize = 14.sp,
                color = Color.White.copy(alpha = 0.5f)
            )
            Text(
                text = "Fajar",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = IslamicGold,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            
            // For
            Text(
                text = "for",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.3f)
            )
            Text(
                text = "Zahra",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = IslamicGold.copy(alpha = 0.8f)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Rotating quotes
            Text(
                text = quotes[currentQuote],
                fontSize = 14.sp,
                color = Color.White,
                modifier = Modifier.padding(horizontal = 32.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Version
            Text(
                text = "v1.0.0",
                fontSize = 12.sp,
                color = Color.White.copy(alpha = 0.3f)
            )
        }
    }
}
