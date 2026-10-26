# SurakshaSetu Backend with MongoDB

This is the Node.js backend for CodeMate with MongoDB database for video management.

## Features

- RESTful API for video management
- MongoDB database for storing video metadata
- WebSocket support for real-time communication
- CORS enabled for frontend integration

## Prerequisites

- Node.js (v14 or higher)
- MongoDB (local or cloud instance)
- Python 3.x (for upload scripts)

## Setup

1. Install dependencies:
   ```bash
   npm install
   ```

2. Create a `.env` file with your configuration:
   ```env
   PORT=5000
   MONGODB_URI=mongodb://localhost:27017/codemate
   NODE_ENV=development
   ```

3. Start the development server:
   ```bash
   npm run dev
   ```

4. Or build and start for production:
   ```bash
   npm run build
   npm start
   ```

## API Endpoints

### Video Management

- `GET /api/videos` - Get all videos (with optional query params: language, section, lesson)
- `GET /api/videos/:id` - Get a specific video by ID
- `POST /api/videos` - Create a new video record
- `PUT /api/videos/:id` - Update a video record
- `DELETE /api/videos/:id` - Delete a video record
- `GET /api/videos/check/:language/:lesson` - Check if a lesson has a video

### Request/Response Examples

#### Create a Video Record
```json
POST /api/videos
{
  "title": "Python Variables",
  "description": "Learn about variables in Python",
  "url": "https://example.com/videos/python/basics/variables.mp4",
  "language": "python",
  "section": "basics",
  "lesson": "variables",
  "duration": 300,
  "fileSize": 1024000,
  "thumbnailUrl": "https://example.com/thumbnails/variables.jpg"
}
```

#### Response
```json
{
  "_id": "5f8d0d5f9b1e8a001c8b4567",
  "title": "Python Variables",
  "description": "Learn about variables in Python",
  "url": "https://example.com/videos/python/basics/variables.mp4",
  "language": "python",
  "section": "basics",
  "lesson": "variables",
  "duration": 300,
  "fileSize": 1024000,
  "thumbnailUrl": "https://example.com/thumbnails/variables.jpg",
  "hasVideo": true,
  "createdAt": "2020-10-19T10:30:00.000Z",
  "updatedAt": "2020-10-19T10:30:00.000Z"
}
```

## Database Schema

The videos are stored in MongoDB with the following schema:

```typescript
interface IVideo {
  title: string;
  description: string;
  url: string;
  language: string;
  section: string;
  lesson: string;
  duration?: number; // in seconds
  fileSize?: number; // in bytes
  thumbnailUrl?: string;
  hasVideo: boolean;
  createdAt: Date;
  updatedAt: Date;
}
```

## Python Upload Scripts

There are Python scripts available for uploading videos:

1. `upload_videos_nodejs.py` - For uploading to the Node.js backend
2. `bulk_video_upload.py` - For bulk uploading multiple videos

## Testing the API

You can test the API endpoints using tools like Postman or curl:

```bash
# Get all videos
curl http://localhost:5000/api/videos

# Get videos for a specific language
curl "http://localhost:5000/api/videos?language=python"

# Create a new video
curl -X POST http://localhost:5000/api/videos \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Test Video",
    "description": "A test video",
    "url": "https://example.com/test.mp4",
    "language": "python",
    "section": "basics",
    "lesson": "test"
  }'
```

## Integration with Frontend

To integrate with the Android app or web frontend:

1. Update the API base URL to point to your backend server
2. Replace Firebase calls with REST API calls to the backend
3. Handle authentication if needed (currently not implemented)

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a pull request

## License

This project is licensed under the MIT License.