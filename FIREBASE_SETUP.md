# Firebase Setup Guide for CodeMate

This guide will help you set up Firebase for video storage and management in your CodeMate app.

## Prerequisites

1. A Google account
2. Android Studio (latest version)
3. Your CodeMate project

## Step 1: Create a Firebase Project

1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Click "Create a project" or "Add project"
3. Enter project name: `codemate-harsh` (or your preferred name)
4. Enable Google Analytics (optional but recommended)
5. Click "Create project"

## Step 2: Add Android App to Firebase

1. In your Firebase project console, click the Android icon (</>) to add an Android app
2. Enter your package name: `com.harsh.codo`
3. Enter app nickname: `CodeMate`
4. Enter SHA-1 certificate fingerprint (optional for now)
5. Click "Register app"

## Step 3: Download Configuration File

1. Download the `google-services.json` file
2. Place it in the `app/` directory of your project
3. Replace the existing `google-services.json` file with the downloaded one

## Step 4: Enable Firebase Services

### Firebase Storage
1. In Firebase Console, go to "Storage" in the left sidebar
2. Click "Get started"
3. Choose "Start in test mode" for development (you can secure it later)
4. Select a location for your storage bucket
5. Click "Done"

### Firebase Firestore (Optional for lesson metadata)
1. In Firebase Console, go to "Firestore Database" in the left sidebar
2. Click "Create database"
3. Choose "Start in test mode" for development
4. Select a location for your database
5. Click "Done"

## Step 5: Configure Security Rules

### Storage Rules
Go to Storage > Rules and update the rules:

```javascript
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    // Allow read access to all videos
    match /videos/{language}/{lessonId} {
      allow read: if true;
      allow write: if request.auth != null; // Only authenticated users can upload
    }
    
    // Allow read access to all other files
    match /{allPaths=**} {
      allow read: if true;
      allow write: if request.auth != null;
    }
  }
}
```

### Firestore Rules (if using Firestore)
Go to Firestore Database > Rules and update the rules:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Allow read access to lessons
    match /lessons/{lessonId} {
      allow read: if true;
      allow write: if request.auth != null;
    }
    
    // Allow users to manage their own progress
    match /user_progress/{userId}/lessons/{lessonId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
  }
}
```

## Step 6: Update Your App Configuration

The Firebase dependencies and configuration have already been added to your project:

- `app/build.gradle.kts` - Contains Firebase dependencies
- `build.gradle.kts` - Contains Google Services plugin
- `gradle/libs.versions.toml` - Contains Firebase library versions

## Step 7: Test the Setup

1. Build your project: `./gradlew build`
2. Run the app on a device or emulator
3. Try uploading a video using the VideoUploadComponent
4. Try playing a video using the FirebaseVideoPlayer

## Step 8: Upload Sample Videos

You can upload sample videos for your lessons:

1. Use the VideoUploadComponent in your app
2. Or upload directly through Firebase Console:
   - Go to Storage in Firebase Console
   - Create folders: `videos/python/`, `videos/javascript/`, `videos/cpp/`
   - Upload your video files with lesson IDs as names (e.g., `if.mp4`, `loops.mp4`)

## Video File Structure

Your videos should be organized as follows:
```
videos/
├── python/
│   ├── if.mp4
│   ├── loops.mp4
│   └── functions.mp4
├── javascript/
│   ├── variables.mp4
│   ├── functions.mp4
│   └── arrays.mp4
└── cpp/
    ├── basics.mp4
    ├── variables.mp4
    └── functions.mp4
```

## Troubleshooting

### Common Issues:

1. **Build Errors**: Make sure `google-services.json` is in the correct location (`app/` directory)
2. **Permission Denied**: Check your Firebase Storage and Firestore security rules
3. **Video Not Playing**: Ensure the video URL is accessible and the format is supported
4. **Upload Failures**: Check your internet connection and Firebase project configuration

### Getting SHA-1 Certificate Fingerprint:

For release builds, you'll need to add your SHA-1 certificate fingerprint:

```bash
# For debug builds
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android

# For release builds (replace with your keystore path)
keytool -list -v -keystore your-keystore.jks -alias your-alias
```

## Next Steps

1. **Authentication**: Add Firebase Authentication for user management
2. **Analytics**: Enable Firebase Analytics to track user engagement
3. **Crashlytics**: Add Firebase Crashlytics for crash reporting
4. **Performance**: Monitor app performance with Firebase Performance
5. **Security**: Implement proper security rules for production

## Support

If you encounter any issues:
1. Check the [Firebase Documentation](https://firebase.google.com/docs)
2. Visit [Firebase Support](https://firebase.google.com/support)
3. Check the [Android Firebase GitHub](https://github.com/firebase/firebase-android-sdk)

## Production Considerations

Before deploying to production:

1. **Security Rules**: Update security rules to be more restrictive
2. **Authentication**: Implement proper user authentication
3. **Video Optimization**: Consider video compression and CDN
4. **Cost Management**: Monitor Firebase usage and costs
5. **Backup**: Implement backup strategies for your data 