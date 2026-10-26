package com.harsh.codo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.harsh.codo.ui.theme.CodoTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.harsh.codo.ui.HomeScreen
import com.harsh.codo.ui.LearnScreen
import com.harsh.codo.ui.LoginScreen
import com.harsh.codo.ui.PracticeScreen
import androidx.compose.ui.graphics.vector.ImageVector
import com.harsh.codo.ui.ProfileScreen
import androidx.compose.runtime.*
import kotlinx.coroutines.*
import com.harsh.codo.network.RetrofitInstance
import com.harsh.codo.network.SubmissionRequest
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import retrofit2.Response
import com.harsh.codo.network.ResultResponse
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.harsh.codo.ui.QuizScreen

data class TestCase(val input: String, val expectedOutput: String)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CodoTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (currentRoute != "login") {
                            BottomNavigationBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "login",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        composable("login") {
                            LoginScreen(onLoginSuccess = {
                                navController.navigate("home") {
                                    popUpTo("login") { inclusive = true }
                                }
                            })
                        }
                        composable("home") {
                            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                                HomeScreen(
                                    onPythonQuiz = { navController.navigate("quiz/python") },
                                    onJSQuiz = { navController.navigate("quiz/javascript") },
                                    onCPPQuiz = { navController.navigate("quiz/cpp") }
                                )
                            }
                        }
                        composable("learn") { LearnScreen() }
                        composable("practice") { PracticeScreen() }
                        composable("profile") { ProfileScreen() }
                        composable("quiz/{language}") { backStackEntry ->
                            val language = backStackEntry.arguments?.getString("language") ?: "python"
                            QuizScreen(language = language, onBack = { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

sealed class BottomNavScreen(val route: String, val label: String, val icon: ImageVector) {
    object Home : BottomNavScreen("home", "Home", Icons.Filled.Home)
    object Learn : BottomNavScreen("learn", "Learn", Icons.Filled.MenuBook)
    object Practice : BottomNavScreen("practice", "Practice", Icons.Filled.PlayArrow)
    object Profile : BottomNavScreen("profile", "Profile", Icons.Filled.Person)
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        BottomNavScreen.Home,
        BottomNavScreen.Learn,
        BottomNavScreen.Practice,
        BottomNavScreen.Profile
    )
    NavigationBar {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        items.forEach { screen ->
            NavigationBarItem(
                icon = { Icon(screen.icon, contentDescription = screen.label) },
                label = { Text(screen.label) },
                selected = currentRoute == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@Composable
fun HomeScreen(
    onPythonQuiz: () -> Unit,
    onJSQuiz: () -> Unit,
    onCPPQuiz: () -> Unit
) {
    com.harsh.codo.ui.HomeScreen(
        onPythonQuiz = onPythonQuiz,
        onJSQuiz = onJSQuiz,
        onCPPQuiz = onCPPQuiz
    )
}

@Composable
fun LearnScreen() {
    com.harsh.codo.ui.LearnScreen()
}

@Composable
fun PracticeScreen() {
    // Example problem and test cases
    val problemDescription = "Write Python code to print the sum of two numbers from input."
    val testCases = listOf(
        TestCase("2 3", "5\n"),
        TestCase("10 20", "30\n"),
        TestCase("0 0", "0\n")
    )

    var userCode by remember { mutableStateOf("") }
    var results by remember { mutableStateOf<List<Pair<Boolean, String>>?>(null) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)) {

        Text(problemDescription, style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(12.dp))

        TextField(
            value = userCode,
            onValueChange = { userCode = it },
            label = { Text("Your Python Code") },
            placeholder = { Text("e.g. a, b = map(int, input().split())\nprint(a + b)") },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Default),
            singleLine = false,
            maxLines = 10
        )

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                if (userCode.isBlank()) {
                    errorMessage = "Please enter your code."
                    return@Button
                }
                isLoading = true
                errorMessage = null
                results = null
                coroutineScope.launch {
                    val tempResults = mutableListOf<Pair<Boolean, String>>()
                    for (test in testCases) {
                        try {
                            val response: Response<ResultResponse> = withContext(Dispatchers.IO) {
                                RetrofitInstance.api.createSubmission(
                                    SubmissionRequest(
                                        source_code = userCode,
                                        language_id = 71, // Python 3
                                        stdin = test.input
                                    )
                                ).execute()
                            }
                            val body = response.body()
                            val output = body?.stdout ?: body?.stderr ?: body?.compile_output ?: "No output"
                            val passed = output.trim() == test.expectedOutput.trim()
                            tempResults.add(passed to output)
                        } catch (e: Exception) {
                            errorMessage = "Network or API error: ${e.localizedMessage}"
                            break
                        }
                    }
                    results = tempResults
                    isLoading = false
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Running...")
            } else {
                Text("Run & Check")
            }
        }

        Spacer(Modifier.height(16.dp))

        errorMessage?.let {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    it,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Spacer(Modifier.height(8.dp))
        }

        results?.let { resList ->
            Text("Test Results:", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(8.dp))
            resList.forEachIndexed { idx, (passed, output) ->
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (passed) MaterialTheme.colorScheme.primaryContainer
                        else MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.padding(bottom = 8.dp)
                ) {
                    Column(Modifier.padding(8.dp)) {
                        Text("Test Case ${idx + 1}: ${if (passed) "Passed" else "Failed"}")
                        Text("Output: ${output.trim()}")
                        Text("Expected: ${testCases[idx].expectedOutput.trim()}")
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen() {
    com.harsh.codo.ui.ProfileScreen()
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    CodoTheme {
        Greeting("Android")
    }
}