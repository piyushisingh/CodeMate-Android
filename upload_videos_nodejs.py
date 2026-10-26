#!/usr/bin/env python3
"""
Video Upload Script for CodeMate with Node.js Backend
This script uploads videos to the new Node.js backend with MongoDB database.
"""

import os
import json
import requests
from pathlib import Path
from typing import Dict, List, Optional

# Base URL for the Node.js backend
BASE_URL = "http://localhost:5000/api"

# Lesson structure for video uploads
LESSONS = {
    "python": {
        "basics": [
            "variables",
            "data_types", 
            "operators",
            "input_output"
        ],
        "control_flow": [
            "if_statements",
            "loops",
            "functions"
        ],
        "data_structures": [
            "lists",
            "dictionaries",
            "tuples"
        ]
    },
    "javascript": {
        "basics": [
            "variables",
            "data_types",
            "operators", 
            "functions"
        ],
        "dom_manipulation": [
            "selectors",
            "events",
            "modifying_elements"
        ]
    },
    "cpp": {
        "basics": [
            "variables",
            "data_types",
            "operators",
            "functions"
        ],
        "oop": [
            "classes",
            "inheritance",
            "polymorphism"
        ]
    }
}

def create_video_record(video_data: Dict) -> Optional[Dict]:
    """
    Create a video record in the Node.js backend
    """
    try:
        response = requests.post(f"{BASE_URL}/videos", json=video_data)
        if response.status_code == 201:
            print(f"✅ Created video record for {video_data['lesson']}")
            return response.json()
        else:
            print(f"❌ Failed to create video record: {response.status_code} - {response.text}")
            return None
    except Exception as e:
        print(f"❌ Error creating video record: {str(e)}")
        return None

def update_video_record(video_id: str, video_data: Dict) -> Optional[Dict]:
    """
    Update an existing video record
    """
    try:
        response = requests.put(f"{BASE_URL}/videos/{video_id}", json=video_data)
        if response.status_code == 200:
            print(f"✅ Updated video record for {video_data['lesson']}")
            return response.json()
        else:
            print(f"❌ Failed to update video record: {response.status_code} - {response.text}")
            return None
    except Exception as e:
        print(f"❌ Error updating video record: {str(e)}")
        return None

def get_video_record(language: str, lesson: str) -> Optional[Dict]:
    """
    Check if a video record already exists
    """
    try:
        response = requests.get(f"{BASE_URL}/videos/check/{language}/{lesson}")
        if response.status_code == 200:
            result = response.json()
            return result.get('video') if result.get('hasVideo') else None
        else:
            print(f"❌ Failed to check video record: {response.status_code} - {response.text}")
            return None
    except Exception as e:
        print(f"❌ Error checking video record: {str(e)}")
        return None

def upload_single_video(
    language: str, 
    section: str, 
    lesson: str, 
    video_file_path: str,
    title: str = None,
    description: str = ""
) -> bool:
    """
    Upload a single video to the backend
    """
    # In a real implementation, you would upload the actual video file
    # For now, we're just creating the metadata record
    
    # Check if video already exists
    existing_video = get_video_record(language, lesson)
    
    video_data = {
        "title": title or f"{language.title()} - {lesson.replace('_', ' ').title()}",
        "description": description,
        "url": f"/videos/{language}/{section}/{lesson}.mp4",  # Placeholder URL
        "language": language,
        "section": section,
        "lesson": lesson,
        "hasVideo": True
    }
    
    if existing_video:
        # Update existing record
        result = update_video_record(existing_video['_id'], video_data)
        return result is not None
    else:
        # Create new record
        result = create_video_record(video_data)
        return result is not None

def bulk_upload_videos(videos_directory: str, language: str) -> Dict[str, bool]:
    """
    Bulk upload videos from a directory
    """
    results = {}
    directory = Path(videos_directory)
    
    if not directory.exists() or not directory.is_dir():
        print(f"❌ Directory not found: {videos_directory}")
        return results
    
    # Get all MP4 files
    video_files = list(directory.glob("*.mp4"))
    
    if not video_files:
        print(f"❌ No MP4 files found in {videos_directory}")
        return results
    
    print(f"📁 Found {len(video_files)} video files to upload")
    
    for video_file in video_files:
        lesson_name = video_file.stem  # Filename without extension
        
        # Try to determine section based on lesson name
        section = "unknown"
        for sec, lessons in LESSONS.get(language, {}).items():
            if lesson_name in lessons:
                section = sec
                break
        
        print(f"📤 Uploading {lesson_name} ({language}/{section})...")
        success = upload_single_video(
            language=language,
            section=section,
            lesson=lesson_name,
            video_file_path=str(video_file),
            title=f"{language.title()} - {lesson_name.replace('_', ' ').title()}"
        )
        
        results[lesson_name] = success
    
    return results

def generate_sample_video_data():
    """
    Generate sample video data for testing
    """
    sample_videos = []
    
    for language, sections in LESSONS.items():
        for section, lessons in sections.items():
            for lesson in lessons:
                sample_videos.append({
                    "title": f"{language.title()} - {lesson.replace('_', ' ').title()}",
                    "description": f"Learn about {lesson.replace('_', ' ')} in {language}",
                    "url": f"https://example.com/videos/{language}/{section}/{lesson}.mp4",
                    "language": language,
                    "section": section,
                    "lesson": lesson,
                    "hasVideo": True
                })
    
    return sample_videos

def populate_sample_data():
    """
    Populate the database with sample video records
    """
    print("🌱 Populating database with sample video data...")
    
    sample_videos = generate_sample_video_data()
    successful = 0
    
    for video_data in sample_videos:
        result = create_video_record(video_data)
        if result:
            successful += 1
    
    print(f"✅ Successfully created {successful}/{len(sample_videos)} sample video records")

def main():
    """
    Main function to demonstrate video upload functionality
    """
    print("🎥 CodeMate Video Upload Tool (Node.js Backend)")
    print("=" * 50)
    
    # Test connection to backend
    try:
        response = requests.get(BASE_URL.replace('/api', ''))
        if response.status_code == 200:
            print("✅ Connected to Node.js backend successfully")
        else:
            print("⚠️  Backend responded with status:", response.status_code)
    except Exception as e:
        print("❌ Could not connect to Node.js backend:", str(e))
        print("💡 Make sure the backend server is running on port 5000")
        return
    
    # Show menu
    print("\nSelect an option:")
    print("1. Upload single video")
    print("2. Bulk upload videos")
    print("3. Populate with sample data")
    print("4. Exit")
    
    choice = input("\nEnter your choice (1-4): ").strip()
    
    if choice == "1":
        # Single video upload
        language = input("Enter language (python/javascript/cpp): ").strip().lower()
        section = input("Enter section: ").strip().lower()
        lesson = input("Enter lesson name: ").strip().lower()
        title = input("Enter video title (optional): ").strip()
        description = input("Enter video description (optional): ").strip()
        
        success = upload_single_video(
            language=language,
            section=section,
            lesson=lesson,
            video_file_path="",  # In real implementation, this would be the file path
            title=title or None,
            description=description
        )
        
        if success:
            print("✅ Video uploaded successfully!")
        else:
            print("❌ Failed to upload video")
            
    elif choice == "2":
        # Bulk upload
        videos_dir = input("Enter path to videos directory: ").strip()
        language = input("Enter language (python/javascript/cpp): ").strip().lower()
        
        if not videos_dir:
            print("❌ Videos directory path is required")
            return
            
        results = bulk_upload_videos(videos_dir, language)
        successful = sum(1 for success in results.values() if success)
        print(f"\n📊 Upload Summary: {successful}/{len(results)} videos uploaded successfully")
        
    elif choice == "3":
        # Populate sample data
        populate_sample_data()
        
    elif choice == "4":
        print("👋 Goodbye!")
        return
        
    else:
        print("❌ Invalid choice")

if __name__ == "__main__":
    main()