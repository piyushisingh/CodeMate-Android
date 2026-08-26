import express from 'express';
import http from 'http';
import cors from 'cors';
import dotenv from 'dotenv';
import { WebSocketServer } from 'ws';
import mongoose from 'mongoose';
import { Video } from './models/Video.js';

// Load environment variables
dotenv.config();

const app = express();
app.use(cors());
app.use(express.json({ limit: '50mb' }));
app.use(express.urlencoded({ limit: '50mb', extended: true }));

const PORT = process.env.PORT || 5000;
const MONGODB_URI = process.env.MONGODB_URI || 'mongodb://localhost:27017/codemate';

// Connect to MongoDB
mongoose.connect(MONGODB_URI)
  .then(() => console.log('Connected to MongoDB'))
  .catch(err => console.error('MongoDB connection error:', err));

// Basic route
app.get('/', function(req, res) {
  res.send('SurakshaSetu Backend API is running');
});

// Video routes
app.get('/api/videos', async function(req, res) {
  try {
    const { language, section, lesson } = req.query as { language?: string; section?: string; lesson?: string };
    let filter: any = {};
    
    if (language) filter.language = language;
    if (section) filter.section = section;
    if (lesson) filter.lesson = lesson;
    
    const videos = await Video.find(filter);
    res.json(videos);
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch videos' });
  }
});

app.get('/api/videos/:id', async function(req, res) {
  try {
    const { id } = req.params;
    const video = await Video.findById(id);
    if (!video) {
      return res.status(404).json({ error: 'Video not found' });
    }
    res.json(video);
  } catch (error) {
    res.status(500).json({ error: 'Failed to fetch video' });
  }
});

app.post('/api/videos', async function(req, res) {
  try {
    const videoData = req.body;
    const video = new Video(videoData);
    await video.save();
    res.status(201).json(video);
  } catch (error) {
    res.status(500).json({ error: 'Failed to create video record' });
  }
});

app.put('/api/videos/:id', async function(req, res) {
  try {
    const { id } = req.params;
    const video = await Video.findByIdAndUpdate(
      id,
      req.body,
      { new: true }
    );
    if (!video) {
      return res.status(404).json({ error: 'Video not found' });
    }
    res.json(video);
  } catch (error) {
    res.status(500).json({ error: 'Failed to update video' });
  }
});

app.delete('/api/videos/:id', async function(req, res) {
  try {
    const { id } = req.params;
    const video = await Video.findByIdAndDelete(id);
    if (!video) {
      return res.status(404).json({ error: 'Video not found' });
    }
    res.json({ message: 'Video deleted successfully' });
  } catch (error) {
    res.status(500).json({ error: 'Failed to delete video' });
  }
});

// Check if a lesson has a video
app.get('/api/videos/check/:language/:lesson', async function(req, res) {
  try {
    const { language, lesson } = req.params;
    const video = await Video.findOne({ language, lesson });
    res.json({ hasVideo: !!video, video: video || null });
  } catch (error) {
    res.status(500).json({ error: 'Failed to check video' });
  }
});

// Create HTTP server and WebSocket server
const server = http.createServer(app);
const wss = new WebSocketServer({ server });

wss.on('connection', (ws) => {
  ws.on('message', (message) => {
    // Echo message for now
    ws.send(`Received: ${message}`);
  });
  ws.send('WebSocket connection established');
});

server.listen(PORT, () => {
  console.log(`Server running on port ${PORT}`);
});