package com.harsh.codo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.clickable

@Composable
fun HomeScreen(
    onPythonQuiz: () -> Unit,
    onJSQuiz: () -> Unit,
    onCPPQuiz: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Welcome and stats row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Welcome back!", fontWeight = FontWeight.Bold, fontSize = 24.sp)
                Text("Continue your coding journey", color = Color.Gray, fontSize = 14.sp)
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Fireplace, contentDescription = "Streak", tint = Color(0xFFFF9800))
                Text(" 0", fontWeight = FontWeight.Bold, color = Color(0xFFFF9800))
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.Filled.EmojiEvents, contentDescription = "XP", tint = Color(0xFFFFD600))
                Text(" 0", fontWeight = FontWeight.Bold, color = Color(0xFFFFD600))
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Daily Goal
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
                    Text("Daily Goal", fontWeight = FontWeight.Bold)
                    Text("Complete 3 lessons to maintain your streak", fontSize = 13.sp, color = Color.Gray)
                    Text("0/3 lessons completed", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Choose Your Language
        Text("Choose Your Language", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Text("Select a programming language to continue learning", color = Color.Gray, fontSize = 13.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Column {
            LanguageProgressCard("Python", "Learn the basics of Python programming", 0f, "Level 0", Color(0xFF388E3C), onClick = onPythonQuiz)
            Spacer(modifier = Modifier.height(12.dp))
            LanguageProgressCard("JavaScript", "Master web development with JavaScript", 0f, "Level 0", Color(0xFFFFC107), onClick = onJSQuiz)
            Spacer(modifier = Modifier.height(12.dp))
            LanguageProgressCard("C++", "Understand system programming with C++", 0f, "Level 0", Color(0xFF1976D2), onClick = onCPPQuiz)
        }
        Spacer(modifier = Modifier.height(24.dp))

        // Quick Practice
        Text("Quick Practice", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            QuickPracticeButton("Code Challenge", modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.width(12.dp))
            QuickPracticeButton("Review", modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun LanguageProgressCard(name: String, desc: String, progress: Float, level: String, color: Color, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = CircleShape,
                    color = color.copy(alpha = 0.1f),
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(Icons.Filled.MenuBook, contentDescription = name, tint = color, modifier = Modifier.padding(8.dp))
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(name, fontWeight = FontWeight.Bold)
                    Text(desc, fontSize = 13.sp, color = Color.Gray)
                }
                Spacer(modifier = Modifier.weight(1f))
                Column(horizontalAlignment = Alignment.End) {
                    Text(level, color = color, fontWeight = FontWeight.Bold)
                    Text("${(progress * 100).toInt()}% Complete", fontSize = 12.sp, color = Color.Gray)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            LinearProgressIndicator(progress = progress, color = color, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
fun QuickPracticeButton(text: String, modifier: Modifier = Modifier) {
    Button(
        onClick = {},
        shape = RoundedCornerShape(12.dp),
        modifier = modifier.height(48.dp)
    ) {
        Text(text)
    }
} 