package com.zahra.space.ui.screens.game

import android.opengl.Matrix
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.filament.*
import com.google.android.filament.android.UiHelper
import com.google.android.filament.gltfio.*
import android.content.Context
import android.view.Choreographer
import android.view.SurfaceView
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.FloatBuffer

@Composable
fun GameWorldScreen() {
    var positionX by remember { mutableFloatStateOf(0f) }
    var positionY by remember { mutableFloatStateOf(0f) }
    var positionZ by remember { mutableFloatStateOf(0f) }
    var rotation by remember { mutableFloatStateOf(0f) }
    var showDialog by remember { mutableStateOf(false) }
    var dialogMessage by remember { mutableStateOf("") }
    var interactionCount by remember { mutableIntStateOf(0) }
    var gameTime by remember { mutableLongStateOf(0L) }
    var weather by remember { mutableStateOf("Cerah") }
    var balance by remember { mutableIntStateOf(0) }
    
    // Hidden messages for NPC interactions
    val hiddenMessages = listOf(
        "Ada yang titip pesan: 'Jangan lupa bahagia, walau jauh.'",
        "Jagalah hati yang kau jaga, karena dia menjaga hatimu juga.",
        "Semoga harimu indah.",
        "Untuk orang baik, semoga selalu baik.",
        "Kalau ketemu Zahra, bilang aku bangga.",
        "Cinta itu bukan memiliki, tapi menjaga. Aku menjagamu dengan doa, dari jauh.",
        "Dan orang-orang yang berdoa: 'Ya Tuhan kami, anugerahkanlah kepada kami pasangan dan keturunan sebagai penyejuk hati.' Aamiin.",
        "Z",
        "Selamat pagi.",
        "Pengingat: Sholat dhuha. Sedang di waktu yang mustajab.",
        "Jangan lupa bahagia",
        "Semoga Allah menjaga hatimu",
        "Aku di sini, menjaga hatimu.",
        "Setiap senja, aku mendoakanmu.",
        "Jangan pernah merasa sendiri."
    )
    
    val npcMessages = listOf(
        "Assalamu'alaikum, Zahra!",
        "Jangan lupa sholat ya...",
        "Hari ini cerah sekali.",
        "Mau ke mana, Nak?",
        "Kucingmu lucu sekali.",
        "Sudah makan?",
        "Doaku selalu untukmu.",
        "Semoga Allah memudahkan urusanmu.",
        "Luna kemana? Aku mau main dengannya.",
        "Masjid baru saja selesai direnovasi.",
        "Ada diskon di supermarket loh!",
        "Restoranmu ramai sekali hari ini."
    )
    
    // Prayer times in game (every 6 hours)
    val prayerTimes = listOf("Subuh", "Dzuhur", "Ashar", "Maghrib", "Isya")
    var currentPrayer by remember { mutableStateOf("") }
    var showPrayerNotif by remember { mutableStateOf(false) }
    
    // Game time progression (1 real minute = 1 game hour)
    LaunchedEffect(Unit) {
        while (true) {
            delay(60000) // 1 real minute
            gameTime += 3600000 // 1 game hour
            
            // Check prayer times (every 6 game hours)
            val gameHour = (gameTime / 3600000) % 24
            if (gameHour.toInt() % 6 == 0 && !showPrayerNotif) {
                currentPrayer = when (gameHour.toInt()) {
                    6 -> "Subuh"
                    12 -> "Dzuhur"
                    18 -> "Ashar"
                    0 -> "Maghrib"
                    5 -> "Isya"
                    else -> ""
                }
                if (currentPrayer.isNotEmpty()) {
                    showPrayerNotif = true
                }
            }
        }
    }
    
    // Weather system
    LaunchedEffect(Unit) {
        val weathers = listOf("Cerah", "Berawan", "Hujan Ringan", "Hujan Deras", "Mendung")
        while (true) {
            delay(300000) // Change every 5 real minutes
            weather = weathers.random()
        }
    }
    
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // 3D View with Filament
        AndroidView(
            factory = { context ->
                FilamentView(context).apply {
                    // Will load models when available
                }
            },
            modifier = Modifier.fillMaxSize()
        )
        
        // UI Overlay
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Bar
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("🏙️ Kota Zahra", style = MaterialTheme.typography.titleMedium)
                    Text("💰 $balance", style = MaterialTheme.typography.titleMedium)
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("⏰ ${gameTime / 3600000}:00", style = MaterialTheme.typography.bodySmall)
                    Text("🌤️ $weather", style = MaterialTheme.typography.bodySmall)
                }
            }
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Prayer notification
            if (showPrayerNotif) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    colors = androidx.compose.material3.CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text("🕌 WAKTU SHOLAT $currentPrayer", 
                             style = MaterialTheme.typography.titleLarge)
                        Text("Ayo ke masjid!", style = MaterialTheme.typography.bodyLarge)
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = {
                                    // Teleport to mosque
                                    positionX = 10f
                                    positionZ = 10f
                                    showPrayerNotif = false
                                    balance += 50 // Reward
                                }
                            ) {
                                Text("Teleport ke Masjid (+50✨)")
                            }
                            Button(
                                onClick = { showPrayerNotif = false }
                            ) {
                                Text("Nanti")
                            }
                        }
                    }
                }
            }
            
            // Hidden message indicator (F)
            if (interactionCount % 7 == 0 && interactionCount > 0) {
                Card(
                    modifier = Modifier
                        .size(40.dp)
                        .align(Alignment.End)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("F", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // NPC Interaction Dialog
            if (showDialog) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(dialogMessage, style = MaterialTheme.typography.bodyLarge)
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.End
                        ) {
                            Button(
                                onClick = { showDialog = false }
                            ) {
                                Text("Tutup")
                            }
                        }
                    }
                }
            }
            
            // Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(
                    onClick = { positionX -= 1f },
                    modifier = Modifier.size(60.dp)
                ) {
                    Text("←")
                }
                Button(
                    onClick = { positionZ += 0.5f },
                    modifier = Modifier.size(60.dp)
                ) {
                    Text("↑")
                }
                Button(
                    onClick = { positionX += 1f },
                    modifier = Modifier.size(60.dp)
                ) {
                    Text("→")
                }
                Button(
                    onClick = { positionZ -= 0.5f },
                    modifier = Modifier.size(60.dp)
                ) {
                    Text("↓")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Action Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                // Interact with NPC
                Button(
                    onClick = {
                        interactionCount++
                        // Random chance for hidden message (20%)
                        if (Math.random() < 0.2) {
                            dialogMessage = hiddenMessages.random()
                            // Bonus points for finding hidden message
                            balance += 10
                        } else {
                            dialogMessage = npcMessages.random()
                        }
                        showDialog = true
                    },
                    modifier = Modifier.weight(1f).padding(end = 4.dp)
                ) {
                    Text("💬 Ngobrol")
                }
                
                // Visit Mosque
                Button(
                    onClick = {
                        positionX = 10f
                        positionZ = 10f
                        dialogMessage = "Masjid Nurul Huda - Tempat yang tenang untuk beribadah"
                        showDialog = true
                    },
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("🕌 Masjid")
                }
                
                // Visit Restaurant
                Button(
                    onClick = {
                        positionX = -10f
                        positionZ = 5f
                        dialogMessage = "Zahra Bistro - Restoran milikmu. Ayo masak!"
                        showDialog = true
                    },
                    modifier = Modifier.weight(1f).padding(horizontal = 4.dp)
                ) {
                    Text("🍳 Resto")
                }
                
                // Visit Shop
                Button(
                    onClick = {
                        positionX = 5f
                        positionZ = -10f
                        dialogMessage = "Minimart - Belanja kebutuhan sehari-hari"
                        showDialog = true
                    },
                    modifier = Modifier.weight(1f).padding(start = 4.dp)
                ) {
                    Text("🛒 Belanja")
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Return to Dashboard Button
            Button(
                onClick = {
                    // Navigate back to dashboard (implement via navController)
                },
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("Kembali ke Dashboard", fontSize = 18.sp)
            }
        }
    }
}

// Filament View for 3D rendering
class FilamentView(context: Context) : SurfaceView(context), Choreographer.FrameCallback {
    private val engine = Engine.create()
    private val renderer = Renderer(engine)
    private val scene = Scene(engine)
    private val view = View(engine)
    private val camera = Camera(engine)
    private val uiHelper = UiHelper(UiHelper.ContextErrorPolicy.DONT_CHECK)
    
    // Placeholder for models - will be loaded from assets when available
    private val models = mutableListOf<Entity>()
    
    init {
        uiHelper.attachTo(this)
        uiHelper.renderCallback = object : UiHelper.RendererCallback {
            override fun onNativeWindowChanged(surface: SurfaceView) {
                // Handle surface change
            }
            override fun onDetachedFromSurface() {
                // Handle detachment
            }
            override fun onResized(width: Int, height: Int) {
                view.setViewport(0, 0, width, height)
            }
        }
        
        setupScene()
        Choreographer.getInstance().postFrameCallback(this)
    }
    
    private fun setupScene() {
        // Create camera
        camera.setProjection(45.0, 1.0, 0.1, 100.0, Camera.Fov.VERTICAL)
        camera.lookAt(0.0, 2.0, 5.0, 0.0, 1.0, 0.0, 0.0, 1.0, 0.0)
        
        view.camera = camera
        view.scene = scene
        
        // Add ambient light
        val ambientLight = EntityManager.get().create()
        LightManager.Builder(LightManager.Type.DIRECTIONAL)
            .color(1.0f, 1.0f, 1.0f)
            .intensity(100000.0f)
            .direction(0.0f, -1.0f, 0.0f)
            .build(engine, ambientLight)
        scene.addEntity(ambientLight)
        
        // Add a simple ground plane
        createGroundPlane()
    }
    
    private fun createGroundPlane() {
        // Simple ground plane for testing
        val vertexData = floatArrayOf(
            -20f, 0f, -20f,  0f, 0.5f, 0f,  // Green color
             20f, 0f, -20f,  0f, 0.5f, 0f,
            -20f, 0f,  20f,  0f, 0.5f, 0f,
             20f, 0f,  20f,  0f, 0.5f, 0f
        )
        
        val indexData = shortArrayOf(0, 1, 2, 1, 3, 2)
        
        val vertexBuffer = VertexBuffer.Builder()
            .vertexCount(4)
            .bufferCount(1)
            .attribute(VertexAttribute.POSITION, 0, VertexBuffer.AttributeType.FLOAT3, 0, 24)
            .attribute(VertexAttribute.COLOR, 0, VertexBuffer.AttributeType.FLOAT3, 12, 24)
            .build(engine)
        vertexBuffer.setBufferAt(engine, 0, ByteBuffer.wrap(vertexData.toByteArray()))
        
        val indexBuffer = IndexBuffer.Builder()
            .indexCount(6)
            .bufferType(IndexBuffer.Builder.IndexType.USHORT)
            .build(engine)
        indexBuffer.setBuffer(engine, ByteBuffer.wrap(indexData.toByteArray()))
        
        val material = Material(engine, """
            {
                "material": {
                    "name": "ground",
                    "shadingModel": "unlit"
                }
            }
        """.toByteArray())
        
        val renderable = Renderable.Builder(1)
            .geometry(0, Renderable.PrimitiveType.TRIANGLES, vertexBuffer, indexBuffer, 0, 6)
            .material(0, material.defaultInstance)
            .build(engine)
        
        scene.addEntity(renderable)
        models.add(renderable)
    }
    
    override fun doFrame(frameTimeNanos: Long) {
        if (uiHelper.isReadyToRender) {
            renderer.render(view)
        }
        Choreographer.getInstance().postFrameCallback(this)
    }
    
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        Choreographer.getInstance().removeFrameCallback(this)
        models.forEach { engine.destroyEntity(it) }
        engine.destroyRenderer(renderer)
        engine.destroyView(view)
        engine.destroyScene(scene)
        engine.destroyCamera(camera)
        engine.destroy()
    }
}
