# 🚀 Node.js Backend Setup for CodeMate

This document explains how to set up and use the new Node.js backend with MongoDB for video management in CodeMate.

## 📋 Prerequisites

1. Node.js (v14 or higher)
2. MongoDB (local or cloud instance)
3. Python 3.x (for upload scripts)

## 🛠️ Setup Instructions

### 1. Install Dependencies

```bash
cd surakshasetu-backend
npm install
```

### 2. Configure Environment

Create a `.env` file in the `surakshasetu-backend` directory:

```env
PORT=5000
MONGODB_URI=mongodb://localhost:27017/codemate
NODE_ENV=development
```

### 3. Start the Backend

For development:
```bash
npm run dev
```

For production:
```bash
npm run build
npm start
```

## 🧪 Testing the Backend

Run the test script to verify the backend is working:

```bash
python test_backend.py
```

Or test manually with curl:

```bash
# Test basic endpoint
curl http://localhost:5000/

# Get all videos
curl http://localhost:5000/api/videos

# Check if a lesson has a video
curl http://localhost:5000/api/videos/check/python/variables
```

## 📡 API Endpoints

### Video Management

- `GET /api/videos` - Get all videos (with optional query params: language, section, lesson)
- `GET /api/videos/:id` - Get a specific video by ID
- `POST /api/videos` - Create a new video record
- `PUT /api/videos/:id` - Update a video record
- `DELETE /api/videos/:id` - Delete a video record
- `GET /api/videos/check/:language/:lesson` - Check if a lesson has a video

### Example Requests

#### Create a Video Record
```bash
curl -X POST http://localhost:5000/api/videos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Python Variables",
    "description": "Learn about variables in Python",
    "url": "https://example.com/videos/python/basics/variables.mp4",
    "language": "python",
    "section": "basics",
    "lesson": "variables"
  }'
```

## 🐍 Python Upload Scripts

### upload_videos_nodejs.py

This script provides a Python interface for uploading videos to the Node.js backend:

```bash
python upload_videos_nodejs.py
```

### migrate_firebase_to_mongodb.py

This script helps migrate data from Firebase to MongoDB:

```bash
python migrate_firebase_to_mongodb.py
```

## 🔄 Migration from Firebase

To migrate from Firebase to the new Node.js backend:

1. Export your Firebase data
2. Run the migration script:
   ```bash
   python migrate_firebase_to_mongodb.py
   ```
3. Update the Android app to use the new backend endpoints
4. Test the migration

## 📱 Android App Integration

To integrate the Android app with the new backend:

1. Replace Firebase calls with REST API calls to the backend
2. Update the base URL in your network client
3. Modify the video service to use the new endpoints
4. Handle authentication if needed

## 🛡️ Security Considerations

- Implement authentication/authorization for API endpoints
- Use HTTPS in production
- Validate all incoming requests
- Sanitize user inputs
- Secure your MongoDB connection

## 📊 Monitoring

- Add logging to API endpoints
- Monitor database performance
- Track upload success rates
- Set up error reporting

## 🆘 Troubleshooting

### Common Issues

1. **MongoDB connection failed**: 
   - Check if MongoDB is running
   - Verify the connection string in `.env`

2. **Port already in use**:
   - Change the PORT in `.env`
   - Kill the process using the port

3. **TypeScript compilation errors**:
   - Ensure all dependencies are installed
   - Check Express version compatibility

### Getting Help

If you encounter issues:
1. Check the console output for error messages
2. Verify all environment variables are set
3. Ensure MongoDB is accessible
4. Check the API documentation in README.md