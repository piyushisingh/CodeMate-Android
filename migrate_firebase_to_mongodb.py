#!/usr/bin/env python3
"""
Migration Script: Firebase to MongoDB
This script helps migrate video data from Firebase Firestore to MongoDB.
"""

import json
import requests
from typing import Dict, List, Optional

# Configuration
FIREBASE_EXPORT_FILE = "firebase-export.json"  # Exported Firebase data
NODEJS_BACKEND_URL = "http://localhost:5000/api/videos"

# Sample Firebase data structure
SAMPLE_FIREBASE_DATA = {
    "lessons": {
        "python_basics_variables": {
            "videoUrl": "https://storage.googleapis.com/codemate-harsh.appspot.com/videos/python/basics/variables.mp4",
            "hasVideo": True,
            "language": "python",
            "section": "basics",
            "lesson": "variables",
            "lastUpdated": "2024-01-01T00:00:00Z"
        },
        "javascript_basics_variables": {
            "videoUrl": "https://storage.googleapis.com/codemate-harsh.appspot.com/videos/javascript/basics/variables.mp4",
            "hasVideo": True,
            "language": "javascript",
            "section": "basics",
            "lesson": "variables",
            "lastUpdated": "2024-01-01T00:00:00Z"
        }
    }
}

def load_firebase_data(file_path: str) -> Dict:
    """
    Load exported Firebase data from JSON file
    """
    try:
        with open(file_path, 'r') as f:
            return json.load(f)
    except FileNotFoundError:
        print(f"⚠️  Firebase export file not found: {file_path}")
        print("💡 Using sample data for demonstration")
        return SAMPLE_FIREBASE_DATA
    except Exception as e:
        print(f"❌ Error loading Firebase data: {str(e)}")
        return {}

def convert_firebase_to_mongodb(firebase_data: Dict) -> List[Dict]:
    """
    Convert Firebase document structure to MongoDB format
    """
    mongodb_videos = []
    
    lessons = firebase_data.get("lessons", {})
    
    for doc_id, lesson_data in lessons.items():
        # Extract language and lesson from document ID
        # Format: language_section_lesson (e.g., python_basics_variables)
        parts = doc_id.split('_')
        if len(parts) >= 3:
            language = parts[0]
            section = parts[1]
            lesson = parts[2]
        else:
            # Fallback if naming convention is different
            language = lesson_data.get("language", "unknown")
            section = lesson_data.get("section", "unknown")
            lesson = lesson_data.get("lesson", doc_id)
        
        # Convert to MongoDB format
        mongodb_video = {
            "title": f"{language.title()} - {lesson.replace('_', ' ').title()}",
            "description": f"Video lesson for {lesson.replace('_', ' ')} in {language}",
            "url": lesson_data.get("videoUrl", ""),
            "language": language,
            "section": section,
            "lesson": lesson,
            "hasVideo": lesson_data.get("hasVideo", False)
        }
        
        mongodb_videos.append(mongodb_video)
    
    return mongodb_videos

def create_video_in_mongodb(video_data: Dict) -> Optional[Dict]:
    """
    Create a video record in MongoDB via Node.js backend
    """
    try:
        response = requests.post(NODEJS_BACKEND_URL, json=video_data)
        if response.status_code == 201:
            print(f"✅ Created video: {video_data['title']}")
            return response.json()
        else:
            print(f"❌ Failed to create video: {response.status_code} - {response.text}")
            return None
    except Exception as e:
        print(f"❌ Error creating video: {str(e)}")
        return None

def migrate_videos():
    """
    Main migration function
    """
    print("🔄 Starting Firebase to MongoDB migration...")
    
    # Test connection to Node.js backend
    try:
        response = requests.get(NODEJS_BACKEND_URL.replace('/videos', ''))
        if response.status_code == 200:
            print("✅ Connected to Node.js backend")
        else:
            print(f"⚠️  Backend responded with status: {response.status_code}")
    except Exception as e:
        print(f"❌ Could not connect to Node.js backend: {str(e)}")
        print("💡 Make sure the backend server is running on port 5000")
        return
    
    # Load Firebase data
    firebase_data = load_firebase_data(FIREBASE_EXPORT_FILE)
    
    if not firebase_data:
        print("❌ No Firebase data to migrate")
        return
    
    # Convert data format
    print("🔄 Converting Firebase data to MongoDB format...")
    mongodb_videos = convert_firebase_to_mongodb(firebase_data)
    
    print(f"📋 Found {len(mongodb_videos)} videos to migrate")
    
    # Import to MongoDB
    successful = 0
    for video in mongodb_videos:
        result = create_video_in_mongodb(video)
        if result:
            successful += 1
    
    print(f"✅ Migration complete: {successful}/{len(mongodb_videos)} videos migrated successfully")
    
    if successful < len(mongodb_videos):
        print("⚠️  Some videos failed to migrate. Check error messages above.")

def export_sample_firebase_data():
    """
    Export sample Firebase data for testing
    """
    with open(FIREBASE_EXPORT_FILE, 'w') as f:
        json.dump(SAMPLE_FIREBASE_DATA, f, indent=2)
    print(f"📝 Sample Firebase data exported to {FIREBASE_EXPORT_FILE}")

if __name__ == "__main__":
    print("🔧 Firebase to MongoDB Migration Tool")
    print("=" * 40)
    
    print("\nSelect an option:")
    print("1. Run migration")
    print("2. Export sample Firebase data")
    print("3. Exit")
    
    choice = input("\nEnter your choice (1-3): ").strip()
    
    if choice == "1":
        migrate_videos()
    elif choice == "2":
        export_sample_firebase_data()
    elif choice == "3":
        print("👋 Goodbye!")
    else:
        print("❌ Invalid choice")