# CODEMATE

An Android coding-learning app built with Kotlin and Jetpack Compose.

## Overview

CodeMate is a hackathon Android application that helps users learn programming (Python, JavaScript, C++) through interactive lessons, quizzes, and coding challenges.

## Features

- Interactive lessons with video content for Python, JavaScript, and C++
- Quizzes per language
- Coding practice with real code execution (Judge0 API)
- Firebase-backed storage for lesson videos
- User profile with XP and progress tracking

## Tech Stack

- **Language:** Kotlin
- **UI:** Jetpack Compose (Material 3)
- **Architecture:** Single-module, MVVM-style with ViewModels
- **Backend services:** Firebase (Storage, Firestore)
- **Networking:** Retrofit + Gson (Judge0 API)
- **Video playback:** Media3 ExoPlayer
- **Image loading:** Coil

## Requirements

- JDK 17+
- Android SDK 35 (compileSdk 35, targetSdk 35, minSdk 24)
- Android Studio (with bundled JBR / JDK 21)

## Build

```bash
./gradlew assembleDebug
```

The debug APK is produced at `app/build/outputs/apk/debug/app-debug.apk`.

## Run

1. Install the APK on an emulator or device:
   ```bash
   adb install app/build/outputs/apk/debug/app-debug.apk
   ```
2. Launch the app:
   ```bash
   adb shell am start -n com.harsh.codo/.MainActivity
   ```

## Firebase Configuration

The application uses Firebase (Storage and Firestore) for lesson video content.
Replace `app/google-services.json` with your own Firebase project configuration
matching the application ID `com.harsh.codo` to enable live Firebase features.

## Project Structure

```
app/src/main/java/com/harsh/codo/
├── MainActivity.kt          # App entry point + navigation
├── firebase/                # Firebase video service
├── network/                 # Judge0 API service + Retrofit
└── ui/                      # Compose screens (Login, Home, Learn, Practice, Profile, Quiz)
```

## Companion Backend

`/surakshasetu-backend` contains an optional Node.js + MongoDB backend for video
management. See its README for setup instructions.
