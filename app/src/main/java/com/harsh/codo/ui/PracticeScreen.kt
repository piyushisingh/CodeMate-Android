package com.harsh.codo.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.compose.foundation.clickable
import androidx.compose.material3.TextFieldDefaults

@Composable
fun PracticeScreen() {
    val navController = rememberNavController()
    var selectedFilter by remember { mutableStateOf(0) }
    val filters = listOf("All", "Easy", "Medium", "Hard")
    val languages = listOf("Python", "JavaScript", "C++")
    var selectedLanguage by remember { mutableStateOf(languages[0]) }
    val challenges = listOf(
        PracticeChallenge(
            language = "Python",
            title = "FizzBuzz Challenge",
            description = "Print numbers 1-100, but replace multiples of 3 with 'Fizz' and multiples of 5 with 'Buzz'",
            difficulty = "EASY",
            time = "15min",
            xp = 50,
            completed = true
        ),
        PracticeChallenge(
            language = "Python",
            title = "Prime Number Checker",
            description = "Write a function to check if a number is prime.",
            difficulty = "MEDIUM",
            time = "20min",
            xp = 60,
            completed = false
        ),
        PracticeChallenge(
            language = "Python",
            title = "Anagram Detector",
            description = "Check if two strings are anagrams of each other.",
            difficulty = "MEDIUM",
            time = "18min",
            xp = 55,
            completed = false
        ),
        PracticeChallenge(
            language = "JavaScript",
            title = "Palindrome Checker",
            description = "Write a function to check if a string is a palindrome",
            difficulty = "EASY",
            time = "10min",
            xp = 40,
            completed = true
        ),
        PracticeChallenge(
            language = "JavaScript",
            title = "Array Chunking",
            description = "Split an array into chunks of a given size.",
            difficulty = "MEDIUM",
            time = "15min",
            xp = 50,
            completed = false
        ),
        PracticeChallenge(
            language = "JavaScript",
            title = "Debounce Function",
            description = "Implement a debounce function for event handling.",
            difficulty = "HARD",
            time = "25min",
            xp = 80,
            completed = false
        ),
        PracticeChallenge(
            language = "C++",
            title = "Reverse a Linked List",
            description = "Reverse a singly linked list.",
            difficulty = "MEDIUM",
            time = "20min",
            xp = 60,
            completed = false
        ),
        PracticeChallenge(
            language = "C++",
            title = "Binary Search",
            description = "Implement binary search on a sorted array.",
            difficulty = "EASY",
            time = "12min",
            xp = 45,
            completed = false
        ),
        PracticeChallenge(
            language = "C++",
            title = "LRU Cache",
            description = "Design and implement an LRU cache.",
            difficulty = "HARD",
            time = "30min",
            xp = 100,
            completed = false
        )
    )
    val filteredChallenges = challenges.filter {
        (selectedLanguage == it.language) &&
        (selectedFilter == 0 || it.difficulty.equals(filters[selectedFilter], ignoreCase = true))
    }
    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Language Selector
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    languages.forEach { lang ->
                        OutlinedButton(
                            onClick = { selectedLanguage = lang },
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedLanguage == lang) Color(0xFFE3F2FD) else Color.Transparent
                            ),
                            border = null
                        ) {
                            Text(lang, color = if (selectedLanguage == lang) Color(0xFF1976D2) else Color.Black)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Stats Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    PracticeStatCard("Completed", challenges.count { it.completed && it.language == selectedLanguage }.toString())
                    PracticeStatCard("XP Earned", challenges.filter { it.completed && it.language == selectedLanguage }.sumOf { it.xp }.toString())
                    PracticeStatCard("Day Streak", "5")
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Filter Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    filters.forEachIndexed { i, filter ->
                        FilterChip(
                            selected = selectedFilter == i,
                            onClick = { selectedFilter = i },
                            label = { Text(filter) },
                            shape = RoundedCornerShape(24.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Challenges List
                Text("Practice Coding", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Text("Sharpen your skills with challenges", color = Color.Gray, fontSize = 13.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    filteredChallenges.forEach { challenge ->
                        PracticeChallengeCard(challenge, onClick = {
                            navController.navigate("codeEditor/${challenge.title}/${challenge.language}/${challenge.description}")
                        })
                    }
                }
            }
        }
        composable(
            "codeEditor/{title}/{language}/{description}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("language") { type = NavType.StringType },
                navArgument("description") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val language = backStackEntry.arguments?.getString("language") ?: "Python"
            val description = backStackEntry.arguments?.getString("description") ?: ""
            PracticeCodeEditorScreen(title = title, initialLanguage = language, description = description, onBack = { navController.popBackStack() })
        }
    }
}

@Composable
fun PracticeStatCard(label: String, value: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.size(width = 110.dp, height = 60.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(value, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(label, color = Color.Gray, fontSize = 13.sp)
        }
    }
}

data class PracticeChallenge(
    val language: String,
    val title: String,
    val description: String,
    val difficulty: String,
    val time: String,
    val xp: Int,
    val completed: Boolean
)

@Composable
fun PracticeChallengeCard(challenge: PracticeChallenge, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.Star, contentDescription = null, tint = Color(0xFF4CAF50))
                Spacer(modifier = Modifier.width(8.dp))
                Text(challenge.title, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.weight(1f))
                if (challenge.completed) {
                    Icon(Icons.Filled.EmojiEvents, contentDescription = null, tint = Color(0xFF4CAF50))
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(challenge.language, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(challenge.description, color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                DifficultyChip(challenge.difficulty)
                Spacer(modifier = Modifier.width(8.dp))
                Text(challenge.time, color = Color.Gray, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text("+${challenge.xp} XP", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = onClick,
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 4.dp)
                ) {
                    Text("Solve!!")
                }
            }
        }
    }
}

@Composable
fun DifficultyChip(difficulty: String) {
    val color = when (difficulty) {
        "EASY" -> Color(0xFFB2FF59)
        "MEDIUM" -> Color(0xFFFFF176)
        "HARD" -> Color(0xFFFF8A65)
        else -> Color.LightGray
    }
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = color
    ) {
        Text(
            difficulty,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun PracticeCodeEditorScreen(title: String, initialLanguage: String, description: String, onBack: () -> Unit) {
    var selectedLanguage by remember { mutableStateOf(initialLanguage) }
    var code by remember { mutableStateOf("") }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    val languages = listOf("Python", "JavaScript", "C++")
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(Icons.Filled.EmojiEvents, contentDescription = "Back")
            }
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp, modifier = Modifier.padding(start = 8.dp))
        }
        Spacer(modifier = Modifier.height(16.dp))
        // Language Selector
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            languages.forEach { lang ->
                OutlinedButton(
                    onClick = { selectedLanguage = lang },
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = if (selectedLanguage == lang) Color(0xFFE3F2FD) else Color.Transparent
                    ),
                    border = null
                ) {
                    Text(lang, color = if (selectedLanguage == lang) Color(0xFF1976D2) else Color.Black)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(description, color = Color.Gray, fontSize = 15.sp)
        Spacer(modifier = Modifier.height(12.dp))
        // Code Editor
        Text("Write your solution:", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(8.dp))
        Surface(
            shape = RoundedCornerShape(8.dp),
            color = Color(0xFFEEEEEE),
            modifier = Modifier.weight(1f)
        ) {
            TextField(
                value = code,
                onValueChange = { code = it },
                placeholder = { Text("Type your code here...") },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp),
                textStyle = LocalTextStyle.current.copy(fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace),
                maxLines = Int.MAX_VALUE,
                singleLine = false,
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            resultMessage = if (code.contains("return")) "Success! Your code looks correct." else "Error: Please include a return statement."
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Run Code")
        }
        if (resultMessage != null) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(resultMessage!!, color = if (resultMessage!!.startsWith("Success")) Color(0xFF388E3C) else Color.Red, fontWeight = FontWeight.Bold)
        }
    }
} 