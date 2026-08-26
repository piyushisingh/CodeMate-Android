package com.harsh.codo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.harsh.codo.firebase.FirebaseVideoService
import kotlinx.coroutines.launch
import java.util.*

class LearnViewModel : ViewModel() {
    enum class Language { PYTHON, JAVASCRIPT, CPP }

    data class QuizQuestion(
        val id: String = UUID.randomUUID().toString(),
        val question: String,
        val options: List<String>,
        val correctIndex: Int,
        val imageUrl: String? = null,
        val explanation: String? = null
    )

    data class Lesson(
        val id: String,
        val title: String,
        val type: String,
        val xp: Int,
        val section: String,
        val description: String = "",
        val exampleCode: String? = null,
        val quiz: List<QuizQuestion>? = null,
        val imageUrl: String? = null,
        val videoUrl: String? = null,
        val locked: Boolean = false,
        val completed: Boolean = false,
        val active: Boolean = false,
        val firebaseVideoId: String? = null
    )
    data class Section(
        val title: String,
        val subtitle: String,
        val lessons: List<Lesson>
    )
    data class LanguageState(
        val sections: List<Section>,
        val level: Int,
        val xp: Int,
        val totalLessons: Int
    )

    var selectedLanguage by mutableStateOf(Language.PYTHON)
        private set
    private var languageStates by mutableStateOf(mapOf<Language, LanguageState>())
    val sections: List<Section> get() = languageStates[selectedLanguage]?.sections ?: emptyList()
    val level: Int get() = languageStates[selectedLanguage]?.level ?: 1
    val xp: Int get() = languageStates[selectedLanguage]?.xp ?: 0
    val totalLessons: Int get() = languageStates[selectedLanguage]?.totalLessons ?: 0

    // Firebase video service
    private val firebaseVideoService = FirebaseVideoService()

    init {
        loadInitialData()
        resetProgress()
        loadFirebaseVideos()
    }

    private fun loadFirebaseVideos() {
        viewModelScope.launch {
            try {
                // Load videos for all languages
                val pythonVideos = firebaseVideoService.getAllVideosForLanguage("python")
                val jsVideos = firebaseVideoService.getAllVideosForLanguage("javascript")
                val cppVideos = firebaseVideoService.getAllVideosForLanguage("cpp")
                
                // Update lesson video URLs with Firebase URLs
                updateLessonVideos(Language.PYTHON, pythonVideos)
                updateLessonVideos(Language.JAVASCRIPT, jsVideos)
                updateLessonVideos(Language.CPP, cppVideos)
            } catch (e: Exception) {
                // Handle error - videos will fall back to default URLs
                println("Error loading Firebase videos: ${e.message}")
            }
        }
    }

    private fun updateLessonVideos(language: Language, videoUrls: Map<String, String>) {
        val currentState = languageStates[language] ?: return
        val updatedSections = currentState.sections.map { section ->
            section.copy(lessons = section.lessons.map { lesson ->
                val firebaseUrl = videoUrls[lesson.id]
                if (firebaseUrl != null) {
                    lesson.copy(videoUrl = firebaseUrl)
                } else {
                    lesson
                }
            })
        }
        
        languageStates = languageStates.toMutableMap().apply {
            put(language, currentState.copy(sections = updatedSections))
        }
    }

    /**
     * Get Firebase video URL for a specific lesson
     */
    suspend fun getFirebaseVideoUrl(lessonId: String, language: String): String? {
        return try {
            firebaseVideoService.getVideoUrl(lessonId, language)
        } catch (e: Exception) {
            null
        }
    }

    private fun loadInitialData() {
        val pythonSections = listOf(
            Section("Control Flow", "Master if statements, loops, and logic", listOf(
                Lesson(
                    id = "if",
                    title = "If Statements",
                    type = "Lesson",
                    xp = 25,
                    section = "Control Flow",
                    description = "Learn how to use if, else if, and else to control the flow of your Python programs.",
                    exampleCode = """python\nx = 10\nif x > 5:\n    print(\"x is greater than 5\")\nelse:\n    print(\"x is 5 or less\")\n""".trimIndent(),
                    quiz = listOf(
                        QuizQuestion(
                            question = "What keyword is used for an alternative condition in Python if the first `if` is false?",
                            options = listOf("else", "elif", "elseif", "otherwise"),
                            correctIndex = 1,
                            explanation = "The correct answer is 'elif', which is short for 'else if'."
                        ),
                        QuizQuestion(
                            question = "What will be printed if x = 3 from the code `if x > 5: print('A') else: print('B')`?",
                            options = listOf("A", "B", "Nothing", "Error"),
                            correctIndex = 1,
                            explanation = "Since 3 is not greater than 5, the code inside the `else` block is executed."
                        ),
                        QuizQuestion(
                            question = "What is the output of the following code?\nx = 5\nif x > 2:\n    print(\"A\")\nelif x > 4:\n    print(\"B\")",
                            options = listOf("A", "B", "C", "A and B"),
                            correctIndex = 0,
                            explanation = "The first `if` condition `x > 2` is true, so its block is executed and the rest of the `elif` chain is skipped."
                        ),
                        QuizQuestion(
                            question = "Which operator checks for inequality in Python?",
                            options = listOf("<>", "=/=", "!=", "NOT ="),
                            correctIndex = 2,
                            explanation = "`!=` is the correct operator for checking if two values are not equal."
                        ),
                        QuizQuestion(
                            question = "What will be printed by this code?\nage = 15\nif age >= 18:\n    print(\"Adult\")\nelse:\n    print(\"Minor\")",
                            options = listOf("Adult", "Minor", "Error", "Nothing"),
                            correctIndex = 1,
                            explanation = "Since `age` (15) is not greater than or equal to 18, the `else` block is executed."
                        ),
                        QuizQuestion(
                            question = "How do you correctly check if `x` is between 10 and 20 (inclusive)?",
                            options = listOf("10 <= x <= 20", "x > 10 AND x < 20", "x.between(10, 20)", "10 < x < 20"),
                            correctIndex = 0,
                            explanation = "Python allows for chaining comparison operators, making `10 <= x <= 20` a concise way to check a range."
                        ),
                        QuizQuestion(
                            question = "What is the result of an `if` condition with the value `0`?",
                            options = listOf("The condition is True", "The condition is False", "It causes a syntax error", "It depends on the context"),
                            correctIndex = 1,
                            explanation = "In Python, `0`, `None`, and empty collections are considered 'Falsy'. The `if` block will not execute."
                        ),
                        QuizQuestion(
                            question = "What does the `and` operator do in an `if` statement?",
                            options = listOf("Returns True if at least one condition is true", "Returns True only if all conditions are true", "Returns False if any condition is true", "Inverts the result of a condition"),
                            correctIndex = 1,
                            explanation = "The `and` operator requires all connected conditions to be true for the entire expression to be true."
                        ),
                        QuizQuestion(
                            question = "What is the purpose of indentation in a Python `if` statement?",
                            options = listOf("To make the code look nice", "It is optional and good practice", "To define the block of code to be executed", "To add a comment"),
                            correctIndex = 2,
                            explanation = "Python uses indentation (whitespace) to define code blocks, unlike other languages that use curly braces `{}`."
                        ),
                        QuizQuestion(
                            question = "What will be the output of this code?\nname = \"\"\nif not name:\n    print(\"Name is empty\")\nelse:\n    print(\"Name is not empty\")",
                            options = listOf("Name is empty", "Name is not empty", "Error", "Nothing is printed"),
                            correctIndex = 0,
                            explanation = "An empty string `\"\"` is 'Falsy'. The `not` operator inverts this to `True`, so the `if` block is executed."
                        )
                    ),
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/c/c3/Python-logo-notext.svg",
                    firebaseVideoId = "if",
                    locked = false, completed = false, active = true
                ),
                Lesson(
                    id = "loops",
                    title = "Loops and Iteration",
                    type = "Lesson",
                    xp = 30,
                    section = "Control Flow",
                    description = "Learn how to repeat actions using for and while loops in Python.",
                    exampleCode = """python\nfor i in range(5):\n    print(i)\n\nj = 0\nwhile j < 5:\n    print(j)\n    j += 1\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "loops",
                    locked = true
                ),
                Lesson(
                    id = "logic",
                    title = "Logic Challenge",
                    type = "Challenge",
                    xp = 40,
                    section = "Control Flow",
                    description = "Test your understanding of if statements and loops with a coding challenge!",
                    exampleCode = null,
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "logic",
                    locked = true
                )
            )),
            Section("Variables & Data Types", "Learn about variables and data types", listOf(
                Lesson(
                    id = "variables",
                    title = "Variables",
                    type = "Lesson",
                    xp = 20,
                    section = "Variables & Data Types",
                    description = "Learn how to create and use variables in Python.",
                    exampleCode = """python\nname = \"Alice\"\nage = 25\nheight = 1.75\nis_student = True\nprint(f\"Name: {name}, Age: {age}\")\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "variables",
                    locked = true
                ),
                Lesson(
                    id = "data_types",
                    title = "Data Types",
                    type = "Lesson",
                    xp = 25,
                    section = "Variables & Data Types",
                    description = "Explore different data types in Python: strings, integers, floats, and booleans.",
                    exampleCode = """python\n# String\ntext = \"Hello World\"\n# Integer\nnumber = 42\n# Float\ndecimal = 3.14\n# Boolean\nis_true = True\nprint(type(text), type(number))\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "data_types",
                    locked = true
                )
            )),
            Section("Functions", "Create reusable code with functions", listOf(
                Lesson(
                    id = "func_basic",
                    title = "Function Basics",
                    type = "Lesson",
                    xp = 30,
                    section = "Functions",
                    description = "Learn how to define and call functions in Python.",
                    exampleCode = """python\ndef greet(name):\n    print(f\"Hello, {name}!\")\n\ngreet(\"Alice\")\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "func_basic",
                    locked = true
                ),
                Lesson(
                    id = "params",
                    title = "Parameters and Arguments",
                    type = "Lesson",
                    xp = 30,
                    section = "Functions",
                    description = "Understand how to use parameters and arguments in Python functions.",
                    exampleCode = """python\ndef add(a, b):\n    return a + b\n\nresult = add(2, 3)\nprint(result)\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "params",
                    locked = true
                ),
                Lesson(
                    id = "func_challenge",
                    title = "Function Challenge",
                    type = "Challenge",
                    xp = 50,
                    section = "Functions",
                    description = "Put your function skills to the test with a coding challenge!",
                    exampleCode = null,
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "func_challenge",
                    locked = true
                )
            )),
            Section("Lists & Collections", "Work with lists and other collections", listOf(
                Lesson(
                    id = "lists",
                    title = "Lists",
                    type = "Lesson",
                    xp = 25,
                    section = "Lists & Collections",
                    description = "Learn how to create and manipulate lists in Python.",
                    exampleCode = """python\nfruits = [\"apple\", \"banana\", \"orange\"]\nfruits.append(\"grape\")\nprint(fruits[0])  # apple\nprint(len(fruits))  # 4\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "lists",
                    locked = true
                ),
                Lesson(
                    id = "dictionaries",
                    title = "Dictionaries",
                    type = "Lesson",
                    xp = 30,
                    section = "Lists & Collections",
                    description = "Learn how to use dictionaries to store key-value pairs.",
                    exampleCode = """python\nperson = {\n    \"name\": \"John\",\n    \"age\": 30,\n    \"city\": \"New York\"\n}\nprint(person[\"name\"])\nperson[\"job\"] = \"Developer\"\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "dictionaries",
                    locked = true
                )
            ))
        )
        val jsSections = listOf(
            Section("JavaScript Basics", "Start with the fundamentals", listOf(
                Lesson(
                    id = "js_variables",
                    title = "Variables & Constants",
                    type = "Lesson",
                    xp = 20,
                    section = "JavaScript Basics",
                    description = "Learn about var, let, and const.",
                    locked = false,
                    completed = false,
                    active = true,
                    videoUrl = "android.resource://com.harsh.codo/raw/js_variables"
                ),
                Lesson(
                    id = "js_operators",
                    title = "Operators",
                    type = "Lesson",
                    xp = 20,
                    section = "JavaScript Basics",
                    description = "Learn about different operators in JS.",
                    locked = true,
                    videoUrl = "android.resource://com.harsh.codo/raw/js_operators"
                )
            )),
            Section("Functions & Arrays", "Organize and store data", listOf(
                Lesson(
                    id = "js_functions",
                    title = "Functions",
                    type = "Lesson",
                    xp = 30,
                    section = "Functions & Arrays",
                    description = "Learn how to write and use functions.",
                    locked = true,
                    videoUrl = "android.resource://com.harsh.codo/raw/js_functions"
                ),
                Lesson(
                    id = "js_arrays",
                    title = "Arrays",
                    type = "Lesson",
                    xp = 30,
                    section = "Functions & Arrays",
                    description = "Learn how to work with lists of data.",
                    locked = true,
                    videoUrl = "android.resource://com.harsh.codo/raw/js_arrays"
                )
            ))
        )
        val cppSections = listOf(
            Section("C++ Basics", "Learn C++ fundamentals", listOf(
                Lesson(
                    id = "cpp_vars",
                    title = "Variables",
                    type = "Lesson",
                    xp = 20,
                    section = "C++ Basics",
                    description = "Learn how to declare and use variables in C++.",
                    exampleCode = """cpp\nint x = 5;\ndouble y = 3.14;\nstd::cout << x + y << std::endl;\n""".trimIndent(),
                    quiz = listOf(
                        QuizQuestion(
                            question = "What is the primary difference between `int` and `double` in C++?",
                            options = listOf("`int` is for integers, `double` is for floating-point numbers", "Both are for integers, but `double` has more range", "No difference, they are interchangeable", "`double` is for text, `int` is for numbers"),
                            correctIndex = 0,
                            explanation = "`int` is used for whole numbers, while `double` is used for numbers with a fractional part."
                        ),
                        QuizQuestion(
                            question = "Which is the correct way to declare and initialize an integer variable in C++?",
                            options = listOf("variable x = 10;", "int x = 10;", "x = 10;", "integer x = 10;"),
                            correctIndex = 1,
                            explanation = "In C++, you must specify the data type (`int`) followed by the variable name and then assign a value."
                        ),
                        QuizQuestion(
                            question = "What does the `const` keyword do when declaring a variable?",
                            options = listOf("It makes the variable global", "It makes the variable's value unchangeable", "It converts the variable to a string", "It allocates more memory to the variable"),
                            correctIndex = 1,
                            explanation = "A `const` variable must be initialized upon declaration and its value cannot be modified later."
                        ),
                        QuizQuestion(
                            question = "Which of the following is a valid variable name in C++?",
                            options = listOf("2_vars", "my-variable", "_my_var", "my variable"),
                            correctIndex = 2,
                            explanation = "C++ variable names can start with an underscore or a letter, but not a number. Hyphens and spaces are not allowed."
                        ),
                        QuizQuestion(
                            question = "What is the most suitable data type for storing a single character, like 'A'?",
                            options = listOf("string", "char", "character", "int"),
                            correctIndex = 1,
                            explanation = "The `char` data type is specifically designed to hold a single character."
                        ),
                        QuizQuestion(
                            question = "How would you print the value of a variable `myVar` to the console?",
                            options = listOf("print(myVar);", "console.log(myVar);", "std::cout << myVar;", "System.out.println(myVar);"),
                            correctIndex = 2,
                            explanation = "`std::cout` from the `<iostream>` library is used for standard output in C++."
                        ),
                        QuizQuestion(
                            question = "What is the likely result of `int result = 5 / 2;`?",
                            options = listOf("2.5", "2", "3", "Compilation Error"),
                            correctIndex = 1,
                            explanation = "When dividing two integers, C++ performs integer division, which truncates the decimal part. The result is 2."
                        ),
                        QuizQuestion(
                            question = "What is the purpose of the `bool` data type?",
                            options = listOf("To store text", "To store a list of items", "To store true or false values", "To store large numbers"),
                            correctIndex = 2,
                            explanation = "A `bool` variable can only hold the values `true` or `false`, which is useful for logical operations."
                        ),
                        QuizQuestion(
                            question = "Which header file is required to use `std::cout`?",
                            options = listOf("<string>", "<iostream>", "<vector>", "<cmath>"),
                            correctIndex = 1,
                            explanation = "The `<iostream>` header contains declarations for standard input/output streams, including `cout`."
                        ),
                        QuizQuestion(
                            question = "What is the value of an uninitialized local variable in C++?",
                            options = listOf("0", "null", "An indeterminate or garbage value", "It causes a compilation error"),
                            correctIndex = 2,
                            explanation = "Local variables are not automatically initialized to a default value and will contain whatever garbage value is in that memory location."
                        )
                    ),
                    imageUrl = "https://upload.wikimedia.org/wikipedia/commons/1/18/ISO_C%2B%2B_Logo.svg",
                    firebaseVideoId = "cpp_vars",
                    locked = false, completed = false, active = true
                ),
                Lesson(
                    id = "cpp_ops",
                    title = "Operators",
                    type = "Lesson",
                    xp = 20,
                    section = "C++ Basics",
                    description = "Explore arithmetic and logical operators in C++.",
                    exampleCode = """cpp\nint a = 10;\nint b = 3;\nstd::cout << (a + b) << std::endl;\nstd::cout << (a > b) << std::endl;\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "cpp_ops",
                    locked = true
                ),
                Lesson(
                    id = "cpp_func",
                    title = "Functions",
                    type = "Lesson",
                    xp = 30,
                    section = "C++ Basics",
                    description = "Learn how to define and call functions in C++.",
                    exampleCode = """cpp\nint add(int a, int b) {\n  return a + b;\n}\nint result = add(2, 3);\nstd::cout << result << std::endl;\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "cpp_func",
                    locked = true
                )
            )),
            Section("Control Flow", "Master control structures", listOf(
                Lesson(
                    id = "cpp_if",
                    title = "If Statements",
                    type = "Lesson",
                    xp = 25,
                    section = "Control Flow",
                    description = "Learn how to use if, else if, and else statements in C++.",
                    exampleCode = """cpp\nint x = 10;\nif (x > 5) {\n    std::cout << \"x is greater than 5\" << std::endl;\n} else {\n    std::cout << \"x is 5 or less\" << std::endl;\n}\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "cpp_if",
                    locked = true
                ),
                Lesson(
                    id = "cpp_loops",
                    title = "Loops",
                    type = "Lesson",
                    xp = 25,
                    section = "Control Flow",
                    description = "Learn how to use for and while loops in C++.",
                    exampleCode = """cpp\nfor (int i = 0; i < 5; i++) {\n    std::cout << i << std::endl;\n}\n\nint j = 0;\nwhile (j < 5) {\n    std::cout << j << std::endl;\n    j++;\n}\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "cpp_loops",
                    locked = true
                )
            )),
            Section("Pointers", "Understand pointers", listOf(
                Lesson(
                    id = "ptr_intro",
                    title = "Pointer Intro",
                    type = "Lesson",
                    xp = 25,
                    section = "Pointers",
                    description = "Learn what pointers are and how to use them in C++.",
                    exampleCode = """cpp\nint x = 10;\nint* p = &x;\nstd::cout << *p << std::endl;\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "ptr_intro",
                    locked = true
                ),
                Lesson(
                    id = "ptr_arith",
                    title = "Pointer Arithmetic",
                    type = "Lesson",
                    xp = 25,
                    section = "Pointers",
                    description = "Learn how to perform arithmetic on pointers in C++.",
                    exampleCode = """cpp\nint arr[3] = {1, 2, 3};\nint* p = arr;\np++;\nstd::cout << *p << std::endl; // 2\n""".trimIndent(),
                    quiz = null,
                    imageUrl = null,
                    firebaseVideoId = "ptr_arith",
                    locked = true
                )
            ))
        )
        languageStates = mapOf(
            Language.PYTHON to LanguageState(pythonSections, 0, 0, 12),
            Language.JAVASCRIPT to LanguageState(jsSections, 0, 0, 2),
            Language.CPP to LanguageState(cppSections, 0, 0, 5)
        )
    }

    fun switchLanguage(language: Language) {
        selectedLanguage = language
    }

    fun evaluateQuiz(
        quiz: List<QuizQuestion>,
        answers: Map<String, String>
    ): Int {
        var score = 0
        quiz.forEach { question ->
            val correctAnswer = question.options[question.correctIndex]
            if (answers[question.id] == correctAnswer) {
                score++
            }
        }
        return score
    }

    fun markLessonAsCompleted(lessonId: String) {
        val currentState = languageStates[selectedLanguage] ?: return
        
        var lessonFound = false
        val updatedSections = currentState.sections.map { section ->
            // Don't modify sections if we already found and updated the lesson
            if (lessonFound) return@map section

            val lessons = section.lessons
            val lessonIndex = lessons.indexOfFirst { it.id == lessonId }

            if (lessonIndex != -1) {
                lessonFound = true
                val updatedLessons = lessons.toMutableList()

                // Mark current lesson as complete
                val completedLesson = updatedLessons[lessonIndex]
                updatedLessons[lessonIndex] = completedLesson.copy(completed = true, active = false)

                // Unlock next lesson
                if (lessonIndex + 1 < updatedLessons.size) {
                    val nextLesson = updatedLessons[lessonIndex + 1]
                    updatedLessons[lessonIndex + 1] = nextLesson.copy(locked = false, active = true)
                }
                
                return@map section.copy(lessons = updatedLessons)
            }
            section
        }

        val newXp = currentState.xp + (currentState.sections.flatMap { it.lessons }.find { it.id == lessonId }?.xp ?: 0)

        languageStates = languageStates.toMutableMap().apply {
            put(selectedLanguage, currentState.copy(sections = updatedSections, xp = newXp))
        }
    }

    fun onLessonClicked(sectionIdx: Int, lessonIdx: Int) {
        val lang = selectedLanguage
        val state = languageStates[lang] ?: return
        val updatedSections = state.sections.toMutableList()
        val section = updatedSections[sectionIdx]
        val lessons = section.lessons.toMutableList()
        val lesson = lessons[lessonIdx]
        if (!lesson.locked && !lesson.completed) {
            // Mark as completed
            lessons[lessonIdx] = lesson.copy(completed = true, active = false)
            // Unlock next lesson in this section
            if (lessonIdx + 1 < lessons.size) {
                lessons[lessonIdx + 1] = lessons[lessonIdx + 1].copy(locked = false, active = true)
            } else if (sectionIdx + 1 < updatedSections.size) {
                // Unlock first lesson of next section
                val nextSection = updatedSections[sectionIdx + 1]
                val nextLessons = nextSection.lessons.toMutableList()
                nextLessons[0] = nextLessons[0].copy(locked = false, active = true)
                updatedSections[sectionIdx + 1] = nextSection.copy(lessons = nextLessons)
            }
            // Update XP and state
            val newXP = state.xp + lesson.xp
            lessons[lessonIdx] = lessons[lessonIdx].copy(completed = true)
            updatedSections[sectionIdx] = section.copy(lessons = lessons)
            languageStates = languageStates.toMutableMap().apply {
                put(lang, state.copy(sections = updatedSections, xp = newXP))
            }
        }
    }

    fun resetProgress() {
        fun resetSections(sections: List<Section>): List<Section> {
            return sections.mapIndexed { sectionIdx, section ->
                section.copy(lessons = section.lessons.mapIndexed { lessonIdx, lesson ->
                    when {
                        sectionIdx == 0 && lessonIdx == 0 -> lesson.copy(locked = false, completed = false, active = true)
                        else -> lesson.copy(locked = true, completed = false, active = false)
                    }
                })
            }
        }
        languageStates = languageStates.mapValues { (lang, state) ->
            state.copy(
                level = 0,
                xp = 0,
                sections = resetSections(state.sections)
            )
        }
    }
} 