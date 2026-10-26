@file:OptIn(ExperimentalMaterial3Api::class)
package com.harsh.codo.ui

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale
import coil.compose.rememberAsyncImagePainter
import android.util.Log
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@Composable
fun LearnScreen() {
    val vm: LearnViewModel = viewModel()
    val navController = rememberNavController()
    val sections = vm.sections
    val level = vm.level
    val xp by remember { derivedStateOf { vm.xp } }
    val lessons = vm.totalLessons
    val selectedLang = vm.selectedLanguage
    val animatedXP by animateIntAsState(targetValue = xp, animationSpec = tween(600), label = "xp")

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .background(Color(0xFFF8FAFC))
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Learn Programming", fontWeight = FontWeight.Bold, fontSize = 28.sp)
                        Text("Choose your path and start coding", color = Color.Gray, fontSize = 15.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        // Language Tabs (interactive)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            LearnViewModel.Language.values().forEach { lang ->
                                val selected = selectedLang == lang
                                OutlinedButton(
                                    onClick = { vm.switchLanguage(lang) },
                                    shape = RoundedCornerShape(24.dp),
                                    colors = ButtonDefaults.outlinedButtonColors(
                                        containerColor = if (selected) Color(0xFFE3F2FD) else Color.Transparent
                                    ),
                                    border = null,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(
                                        when (lang) {
                                            LearnViewModel.Language.PYTHON -> "Python"
                                            LearnViewModel.Language.JAVASCRIPT -> "JavaScript"
                                            LearnViewModel.Language.CPP -> "C++"
                                        },
                                        color = if (selected) Color(0xFF1976D2) else Color.Black
                                    )
                                }
                                if (lang != LearnViewModel.Language.CPP) Spacer(modifier = Modifier.width(8.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        // Stats with animated XP
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            StatCard("Level", "$level")
                            StatCard("XP", "$animatedXP")
                            StatCard("Lessons", "$lessons")
                        }
                        // Animated XP progress bar
                        val maxXP = 1000 // For demo, can be dynamic
                        val progress by animateFloatAsState(
                            targetValue = animatedXP / maxXP.toFloat(),
                            animationSpec = tween(600), label = "xpBar"
                        )
                        LinearProgressIndicator(
                            progress = progress.coerceIn(0f, 1f),
                            color = Color(0xFF1976D2),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                // Sections from ViewModel
                sections.forEachIndexed { sectionIdx, section ->
                    SectionCard(
                        title = section.title,
                        subtitle = section.subtitle,
                        lessons = section.lessons,
                        onLessonClick = { lessonIdx ->
                            val lesson = section.lessons[lessonIdx]
                            navController.navigate("detail/${sectionIdx}_$lessonIdx")
                        }
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
        composable(
            "detail/{sectionLesson}",
            arguments = listOf(navArgument("sectionLesson") { type = NavType.StringType })
        ) { backStackEntry ->
            var errorMsg: String? = null
            var lesson: LearnViewModel.Lesson? = null
            try {
                val sectionLesson = backStackEntry.arguments?.getString("sectionLesson") ?: "0_0"
                val (sectionIdx, lessonIdx) = sectionLesson.split("_").map { it.toIntOrNull() ?: -1 }
                Log.d("LearnScreen", "sectionIdx=$sectionIdx, lessonIdx=$lessonIdx, sections.size=${vm.sections.size}")
                val section = vm.sections.getOrNull(sectionIdx)
                Log.d("LearnScreen", "section?.lessons?.size=${section?.lessons?.size}")
                lesson = section?.lessons?.getOrNull(lessonIdx)
            } catch (e: Exception) {
                errorMsg = e.toString()
            }
            if (lesson != null) {
                LessonDetailScreen(
                    lesson = lesson,
                    onComplete = {
                        // Defensive: try/catch for completion too
                        try {
                            val sectionLesson = backStackEntry.arguments?.getString("sectionLesson") ?: "0_0"
                            val (sectionIdx, lessonIdx) = sectionLesson.split("_").map { it.toIntOrNull() ?: -1 }
                            vm.onLessonClicked(sectionIdx, lessonIdx)
                        } catch (_: Exception) {}
                        navController.popBackStack()
                    },
                    onBack = { navController.popBackStack() }
                )
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Lesson not found or invalid. ${errorMsg ?: ""}", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.size(width = 100.dp, height = 60.dp)
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

@Composable
fun SectionCard(title: String, subtitle: String, lessons: List<LearnViewModel.Lesson>, onLessonClick: (Int) -> Unit) {
    Card(
        shape = RoundedCornerShape(24.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(subtitle, color = Color.Gray, fontSize = 13.sp)
            Spacer(modifier = Modifier.height(12.dp))
            lessons.forEachIndexed { idx, lesson ->
                LessonCard(lesson, onClick = { onLessonClick(idx) })
            }
        }
    }
}

@Composable
fun LessonCard(lesson: LearnViewModel.Lesson, onClick: () -> Unit) {
    val bgColor = when {
        lesson.active -> Color(0xFFE3F2FD)
        lesson.locked -> Color(0xFFF5F5F5)
        else -> Color.White
    }
    val textColor = if (lesson.locked) Color.Gray else Color.Black
    val clickableModifier = if (!lesson.locked && !lesson.completed) Modifier.clickable { onClick() } else Modifier
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .then(clickableModifier),
        colors = CardDefaults.cardColors(containerColor = bgColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            when {
                lesson.active -> Icon(Icons.Filled.PlayArrow, contentDescription = null, tint = Color(0xFF1976D2))
                lesson.locked -> Icon(Icons.Filled.Lock, contentDescription = null, tint = Color.Gray)
                lesson.completed -> Icon(Icons.Filled.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50))
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(lesson.title, fontWeight = FontWeight.Bold, color = textColor)
                Text(lesson.type, color = textColor.copy(alpha = 0.7f), fontSize = 13.sp)
            }
            Text("+${lesson.xp} XP", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VideoPlayer(url: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val exoPlayer = remember(url) {
        ExoPlayer.Builder(context).build().apply {
            setMediaItem(MediaItem.fromUri(url))
            prepare()
        }
    }
    DisposableEffect(exoPlayer) {
        onDispose { exoPlayer.release() }
    }
    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                useController = true
            }
        },
        modifier = modifier
    )
}

@Composable
fun LessonDetailScreen(lesson: LearnViewModel.Lesson, onComplete: () -> Unit, onBack: () -> Unit) {
    var videoEnded by remember { mutableStateOf(false) }
    var showSummary by remember { mutableStateOf(false) }
    var showQuiz by remember { mutableStateOf(false) }
    var quizResults by remember { mutableStateOf<Map<Int, Boolean>>(emptyMap()) }
    val quizQuestions = lesson.quiz ?: emptyList()
    val allAnswered = quizQuestions.isNotEmpty() && quizResults.size == quizQuestions.size
    val correctCount = quizResults.values.count { it }
    val hasVideo = !lesson.videoUrl.isNullOrBlank()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(lesson.title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Filled.PlayArrow, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. Video
            if (hasVideo && !videoEnded && !showSummary) {
                VideoPlayer(
                    url = lesson.videoUrl!!,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { showQuiz = true; videoEnded = true }) {
                    Text("proceed with the quiz")
                }
            }
            // 2. Theoretical summary
            if ((showSummary || !hasVideo) && !showQuiz) {
                if (!lesson.imageUrl.isNullOrBlank()) {
                    Image(
                        painter = rememberAsyncImagePainter(lesson.imageUrl),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        contentScale = ContentScale.Fit
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
                Text(lesson.title, fontWeight = FontWeight.Bold, fontSize = 28.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("Type: ${lesson.type}", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text("XP: ${lesson.xp}", fontSize = 18.sp, color = Color(0xFF4CAF50))
                Spacer(modifier = Modifier.height(24.dp))
                if (lesson.description.isNotBlank()) {
                    Text(lesson.description, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (!lesson.exampleCode.isNullOrBlank()) {
                    Text("Example Code:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Surface(
                        color = Color(0xFFEEEEEE),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            lesson.exampleCode,
                            fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace,
                            fontSize = 14.sp,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (quizQuestions.isNotEmpty()) {
                    Button(onClick = { showQuiz = true }) { Text("Start Quiz") }
                }
            }
            // 3. Quiz
            if (showQuiz) {
                Text("Quiz:", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Box(modifier = Modifier.weight(1f, fill = false)) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                    ) {
                        quizQuestions.forEachIndexed { qIdx, question ->
                            QuizQuestionView(
                                question = question,
                                questionIndex = qIdx,
                                enabled = !quizResults.containsKey(qIdx),
                                onAnswered = { isCorrect ->
                                    quizResults = quizResults + (qIdx to isCorrect)
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
                if (allAnswered) {
                    Text("$correctCount/${quizQuestions.size} correct!", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = if (correctCount == quizQuestions.size) Color(0xFF388E3C) else Color(0xFFFFA000))
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(onClick = { quizResults = emptyMap() }) {
                        Text("Reset Quiz")
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onComplete,
                    enabled = !lesson.completed && (quizQuestions.isEmpty() || allAnswered)
                ) {
                    Text(if (lesson.completed) "Completed" else "Complete Lesson")
                }
            } else if (!hasVideo || videoEnded) {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = onComplete,
                    enabled = !lesson.completed && (quizQuestions.isEmpty())
                ) {
                    Text(if (lesson.completed) "Completed" else "Complete Lesson")
                }
            }
        }
    }
}

@Composable
fun QuizQuestionView(
    question: LearnViewModel.QuizQuestion,
    questionIndex: Int,
    enabled: Boolean = true,
    onAnswered: (Boolean) -> Unit = {}
) {
    var selected by remember { mutableStateOf<Int?>(null) }
    var answered by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(question.question, fontWeight = FontWeight.SemiBold, fontSize = 15.sp, modifier = Modifier.padding(bottom = 4.dp))
        if (!question.imageUrl.isNullOrBlank()) {
            Image(
                painter = rememberAsyncImagePainter(question.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp),
                contentScale = ContentScale.Fit
            )
        }
        question.options.forEachIndexed { idx, option ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(vertical = 8.dp, horizontal = 8.dp)
                    .heightIn(min = 48.dp)
                    .clickable(enabled = enabled && !answered) {
                        if (!answered && enabled) {
                            selected = idx
                            answered = true
                            onAnswered(idx == question.correctIndex)
                        }
                    }
            ) {
                RadioButton(
                    selected = selected == idx,
                    onClick = {
                        if (!answered && enabled) {
                            selected = idx
                            answered = true
                            onAnswered(idx == question.correctIndex)
                        }
                    },
                    enabled = enabled && !answered,
                    modifier = Modifier.size(28.dp)
                )
                Text(option, fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
            }
            if (idx < question.options.lastIndex) {
                Divider(color = Color.LightGray, thickness = 1.dp)
            }
        }
        if (answered && selected != null) {
            val isCorrect = selected == question.correctIndex
            Text(
                if (isCorrect) "Correct!" else "Incorrect.",
                color = if (isCorrect) Color(0xFF388E3C) else Color.Red,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(top = 4.dp, bottom = 2.dp)
            )
            if (!question.explanation.isNullOrBlank()) {
                Text(
                    question.explanation,
                    color = Color(0xFF1976D2),
                    fontSize = 14.sp,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }
        }
    }
}
