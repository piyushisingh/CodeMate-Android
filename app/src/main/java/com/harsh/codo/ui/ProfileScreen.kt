package com.harsh.codo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ProfileScreen() {
    val vm: LearnViewModel = viewModel()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(Color(0xFFF8FAFC))
    ) {
        // User Info & Progress
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                // User Info
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFE3F2FD),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Icon(Icons.Filled.Person, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.padding(12.dp))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Code Learner", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Text("Programming Enthusiast", color = Color.Gray, fontSize = 13.sp)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = Color(0xFFE0F7FA),
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text("L0", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp))
                            }
                            Text("🔥 0 day streak", color = Color(0xFFFF9800), fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                // Progress Stats
                Text("Your Progress", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    ProfileStatCard("0", "Total XP")
                    ProfileStatCard("0", "Lessons")
                    ProfileStatCard("0%", "Average")
                    ProfileStatCard("0", "Streak")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Achievements
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Achievements", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color(0xFFFFC107), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("0/6", color = Color(0xFF757575), fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                AchievementGridZero()
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Daily Goal
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Daily Goal", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            progress = 0f,
                            modifier = Modifier.size(48.dp),
                            color = Color(0xFF4CAF50)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Keep it up!", fontWeight = FontWeight.Bold)
                            Text("0/3 lessons completed today", color = Color.Gray, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Language Progress
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Language Progress", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                LanguageProgressCard("Python", "Level 0", 0f, 0, 0)
                Spacer(modifier = Modifier.height(12.dp))
                LanguageProgressCard("JavaScript", "Level 0", 0f, 0, 0)
                Spacer(modifier = Modifier.height(12.dp))
                LanguageProgressCard("C++", "Level 0", 0f, 0, 0)
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun ProfileStatCard(value: String, label: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.size(width = 80.dp, height = 60.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(label, color = Color.Gray, fontSize = 12.sp)
        }
    }
}

@Composable
fun AchievementGridZero() {
    val achievements = listOf(
        Achievement("First Steps", "Complete your first lesson", false),
        Achievement("Week Warrior", "Maintain a 7-day streak", false),
        Achievement("Python Beginner", "Complete Python basics", false),
        Achievement("Quiz Master", "Get 10 perfect quiz scores", false),
        Achievement("Challenge Champion", "Complete 5 coding challenges", false),
        Achievement("Speed Coder", "Complete a challenge in under 5 minutes", false)
    )
    Column {
        for (row in achievements.chunked(3)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                for (ach in row) {
                    AchievementCard(ach)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

data class Achievement(val title: String, val desc: String, val unlocked: Boolean)

@Composable
fun AchievementCard(achievement: Achievement) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.size(100.dp, 80.dp),
        colors = CardDefaults.cardColors(containerColor = if (achievement.unlocked) Color(0xFFFFF9C4) else Color(0xFFF5F5F5))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = if (achievement.unlocked) Color(0xFFFFC107) else Color.Gray)
            Text(achievement.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = if (achievement.unlocked) Color.Black else Color.Gray)
        }
    }
}

@Composable
fun LanguageProgressCard(name: String, level: String, progress: Float, xp: Int, lessons: Int) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFE3F2FD),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Filled.Person, contentDescription = name, tint = Color(0xFF1976D2), modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name, fontWeight = FontWeight.Bold)
                    Text(level, fontSize = 13.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text("$xp XP", color = Color(0xFF388E3C), fontWeight = FontWeight.Bold)
                    Text("$lessons lessons", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = progress, color = Color(0xFF1976D2), modifier = Modifier.fillMaxWidth())
        }
    }
} 