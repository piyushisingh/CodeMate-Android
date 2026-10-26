package com.harsh.codo.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(language: String, onBack: () -> Unit) {
    // Real quiz data for each language
    val quizData = mapOf(
        "python" to listOf(
            QuizQuestion("What is the output of print(2 ** 3)?", listOf("6", "8", "9", "5"), 1),
            QuizQuestion("Which keyword is used to define a function in Python?", listOf("func", "def", "function", "define"), 1),
            QuizQuestion("What is the correct file extension for Python files?", listOf(".pyth", ".pt", ".py", ".pyt"), 2),
            QuizQuestion("Which of these is a valid variable name?", listOf("2var", "var_2", "var-2", "var 2"), 1),
            QuizQuestion("What does len([1,2,3]) return?", listOf("2", "3", "1", "0"), 1),
            QuizQuestion("How do you start a comment in Python?", listOf("//", "#", "<!--", "/*"), 1),
            QuizQuestion("What is the output of print('Hello' + 'World')?", listOf("Hello World", "HelloWorld", "Hello+World", "Error"), 1),
            QuizQuestion("Which of these is a list?", listOf("{1,2,3}", "[1,2,3]", "(1,2,3)", "<1,2,3>"), 1),
            QuizQuestion("What is the result of 5 // 2 in Python?", listOf("2.5", "2", "3", "2.0"), 1),
            QuizQuestion("Which function converts a string to an integer?", listOf("str()", "int()", "float()", "chr()"), 1)
        ),
        "javascript" to listOf(
            QuizQuestion("Which keyword declares a block-scoped variable?", listOf("var", "let", "const", "function"), 1),
            QuizQuestion("What is the output of '2' + 2 in JavaScript?", listOf("4", "22", "'4'", "Error"), 1),
            QuizQuestion("Which method adds an element to the end of an array?", listOf("push()", "pop()", "shift()", "unshift()"), 0),
            QuizQuestion("How do you write a single-line comment?", listOf("// comment", "<!-- comment -->", "# comment", "/* comment */"), 0),
            QuizQuestion("What is the result of typeof null?", listOf("'object'", "'null'", "'undefined'", "'number'"), 0),
            QuizQuestion("Which operator checks both value and type?", listOf("==", "=", "===", "!=="), 2),
            QuizQuestion("How do you define a function?", listOf("function myFunc() {}", "def myFunc() {}", "func myFunc() {}", "function:myFunc() {}"), 0),
            QuizQuestion("What is the output of Boolean('')?", listOf("true", "false", "null", "undefined"), 1),
            QuizQuestion("Which array method returns a new array with elements that pass a test?", listOf("map()", "filter()", "reduce()", "forEach()"), 1),
            QuizQuestion("How do you access the first element of an array arr?", listOf("arr[0]", "arr(0)", "arr{0}", "arr.0"), 0)
        ),
        "cpp" to listOf(
            QuizQuestion("Which symbol is used to end a statement in C++?", listOf(".", ":", ";", ","), 2),
            QuizQuestion("What is the correct way to declare an int variable?", listOf("int x;", "x int;", "integer x;", "int: x;"), 0),
            QuizQuestion("Which header is needed for cout?", listOf("<iostream>", "<stdio.h>", "<conio.h>", "<string>"), 0),
            QuizQuestion("What is the output of cout << 2 + 3;?", listOf("5", "23", "2+3", "Error"), 0),
            QuizQuestion("Which operator is used for input?", listOf("<<", ">>", "<>", "=="), 1),
            QuizQuestion("How do you start a single-line comment?", listOf("//", "#", "/*", "<!--"), 0),
            QuizQuestion("What is the correct way to create a function?", listOf("void f() {}", "function f() {}", "def f() {}", "func f() {}"), 0),
            QuizQuestion("Which of these is a loop in C++?", listOf("for", "foreach", "repeat", "loop"), 0),
            QuizQuestion("What is the value of int x = 5/2;?", listOf("2.5", "2", "2.0", "3"), 1),
            QuizQuestion("Which keyword is used to return a value?", listOf("break", "return", "continue", "exit"), 1)
        )
    )

    val questions = quizData[language] ?: emptyList()
    var current by remember { mutableStateOf(0) }
    var score by remember { mutableStateOf(0) }
    var showResult by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(language.replaceFirstChar { it.uppercase() } + " Quiz") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.Filled.ArrowBack, contentDescription = "Back") } }
            )
        }
    ) { padding ->
        if (showResult) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text("Quiz Complete!", fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Text("You scored $score out of ${questions.size}", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Button(onClick = { current = 0; score = 0; showResult = false }) { Text("Restart Quiz") }
            }
        } else if (questions.isNotEmpty() && current < questions.size) {
            val q = questions[current]
            Column(modifier = Modifier.padding(padding).padding(16.dp)) {
                Text("Question ${current + 1}/${questions.size}", fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))
                Text(q.question, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))
                q.options.forEachIndexed { idx, option ->
                    Button(
                        onClick = {
                            if (idx == q.correctIndex) score++
                            if (current < questions.size - 1) current++ else showResult = true
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                    ) {
                        Text(option)
                    }
                }
            }
        }
    }
}

private data class QuizQuestion(val question: String, val options: List<String>, val correctIndex: Int) 