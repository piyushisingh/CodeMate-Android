# 🔄 Migration Guide: Firebase to Node.js Backend

This guide explains how to migrate from Firebase to the new Node.js backend with MongoDB for video management in CodeMate.

## Overview

The new backend replaces Firebase Storage and Firestore with a Node.js server and MongoDB database. This provides more control over data and better integration with custom server logic.

## Architecture Changes

### Before (Firebase)
```
Android App → Firebase Storage + Firestore → Video Files
```

### After (Node.js + MongoDB)
```
Android App → Node.js Backend API → MongoDB + File Storage → Video Files
```

## Backend Setup

1. Install MongoDB locally or use a cloud service like MongoDB Atlas
2. Update the `.env` file with your MongoDB connection string:
   ```
   MONGODB_URI=mongodb://localhost:27017/codemate
   ```
3. Start the backend server:
   ```bash
   cd surakshasetu-backend
   npm install
   npm run dev
   ```

## Database Migration

### Step 1: Export Data from Firebase

Use the Firebase CLI to export your existing data:

```bash
firebase firestore:export ./firebase-export
```

### Step 2: Convert Data Format

Create a script to convert Firebase data to MongoDB format. The main differences:
- Firebase document IDs need to be converted to MongoDB `_id`
- Timestamps format may need adjustment
- Field names should match the new schema

### Step 3: Import Data to MongoDB

Use a script or MongoDB tools to import the converted data:

```javascript
// Example import script
const videos = require('./converted-videos.json');
const { Video } = require('./surakshasetu-backend/src/models/Video');

async function importVideos() {
  for (const video of videos) {
    await Video.create(video);
  }
  console.log(`Imported ${videos.length} videos`);
}
```

## Android App Changes

### 1. Update Dependencies

Remove Firebase dependencies from `build.gradle`:

```gradle
// Remove these Firebase dependencies
// implementation 'com.google.firebase:firebase-storage:xx.x.x'
// implementation 'com.google.firebase:firebase-firestore:xx.x.x'
```

### 2. Create New Video Service

Replace [FirebaseVideoService.kt](file:///c%3A/codemate/app/src/main/java/com/harsh/codo/firebase/FirebaseVideoService.kt) with a new service that calls the Node.js backend:

```kotlin
class NodeJsVideoService {
    private val baseUrl = "http://your-server.com/api/videos"
    
    suspend fun getVideoUrl(lessonId: String, language: String): String? {
        try {
            val response = httpClient.get("$baseUrl/check/$language/$lessonId")
            val result = response.body<VideoCheckResponse>()
            return if (result.hasVideo) result.video?.url else null
        } catch (e: Exception) {
            return null
        }
    }
    
    suspend fun uploadVideo(
        lessonId: String,
        language: String,
        videoUri: Uri,
        context: Context
    ): Result<String> {
        // Implementation for uploading to Node.js backend
        // This would involve sending the file to your backend
        // and creating a video record in MongoDB
    }
}
```

### 3. Update Network Client

Configure your HTTP client to work with the new backend:

```kotlin
val httpClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
    defaultRequest {
        host = "your-server.com"
        port = 5000
        url {
            protocol = URLProtocol.HTTP
        }
    }
}
```

## Video Upload Process

### Old Process (Firebase)
1. Upload video file to Firebase Storage
2. Save metadata to Firestore

### New Process (Node.js)
1. Send video file to Node.js backend endpoint
2. Backend saves file and creates MongoDB record

## API Endpoints

Replace Firebase calls with these REST endpoints:

| Firebase Operation | Node.js Endpoint | Method |
|-------------------|------------------|--------|
| Get video URL | `GET /api/videos/check/{language}/{lesson}` | GET |
| Upload video | `POST /api/videos` | POST |
| Update video | `PUT /api/videos/{id}` | PUT |
| Delete video | `DELETE /api/videos/{id}` | DELETE |
| List videos | `GET /api/videos` | GET |

## Testing the Migration

1. Start the Node.js backend server
2. Run the sample data population script:
   ```bash
   python upload_videos_nodejs.py
   ```
3. Test API endpoints with curl or Postman
4. Update Android app to use new endpoints
5. Test video playback in the app

## Rollback Plan

If issues occur during migration:

1. Keep Firebase project active
2. Maintain both systems temporarily
3. Switch back to Firebase implementation
4. Debug Node.js backend issues

## Performance Considerations

- MongoDB queries with proper indexing
- Video file storage optimization
- CDN integration for video delivery
- Caching strategies for frequently accessed data

## Security

- Implement authentication/authorization
- Validate all API requests
- Sanitize user inputs
- Secure MongoDB connection
- Use HTTPS in production

## Monitoring

- Add logging to API endpoints
- Monitor database performance
- Track upload success rates
- Set up error reporting