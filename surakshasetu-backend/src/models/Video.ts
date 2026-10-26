import mongoose, { Document, Schema } from 'mongoose';

export interface IVideo extends Document {
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

const VideoSchema: Schema = new Schema({
  title: { type: String, required: true },
  description: { type: String, default: '' },
  url: { type: String, required: true },
  language: { type: String, required: true },
  section: { type: String, required: true },
  lesson: { type: String, required: true },
  duration: { type: Number },
  fileSize: { type: Number },
  thumbnailUrl: { type: String },
  hasVideo: { type: Boolean, default: true },
}, {
  timestamps: true
});

// Index for faster queries
VideoSchema.index({ language: 1, section: 1, lesson: 1 });

export const Video = mongoose.model<IVideo>('Video', VideoSchema);