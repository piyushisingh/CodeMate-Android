#!/usr/bin/env python3
"""
Demo script showing how to use the Node.js backend API
"""

import requests
import json

BASE_URL = "http://localhost:5000/api"

def create_sample_video():
    """Create a sample video record"""
    video_data = {
        "title": "Python Variables Tutorial",
        "description": "Learn about variables in Python programming language",
        "url": "https://example.com/videos/python/basics/variables.mp4",
        "language": "python",
        "section": "basics",
        "lesson": "variables",
        "duration": 300,
        "fileSize": 1024000
    }
    
    try:
        response = requests.post(f"{BASE_URL}/videos", json=video_data)
        if response.status_code == 201:
            print("✅ Video created successfully!")
            return response.json()
        else:
            print(f"❌ Failed to create video: {response.status_code}")
            print(response.text)
            return None
    except Exception as e:
        print(f"❌ Error creating video: {e}")
        return None

def get_all_videos():
    """Get all videos"""
    try:
        response = requests.get(f"{BASE_URL}/videos")
        if response.status_code == 200:
            videos = response.json()
            print(f"✅ Found {len(videos)} videos")
            return videos
        else:
            print(f"❌ Failed to get videos: {response.status_code}")
            return []
    except Exception as e:
        print(f"❌ Error getting videos: {e}")
        return []

def get_video_by_id(video_id):
    """Get a specific video by ID"""
    try:
        response = requests.get(f"{BASE_URL}/videos/{video_id}")
        if response.status_code == 200:
            video = response.json()
            print("✅ Video found:")
            print(json.dumps(video, indent=2))
            return video
        elif response.status_code == 404:
            print("❌ Video not found")
            return None
        else:
            print(f"❌ Failed to get video: {response.status_code}")
            return None
    except Exception as e:
        print(f"❌ Error getting video: {e}")
        return None

def check_video_exists(language, lesson):
    """Check if a video exists for a specific lesson"""
    try:
        response = requests.get(f"{BASE_URL}/videos/check/{language}/{lesson}")
        if response.status_code == 200:
            result = response.json()
            if result["hasVideo"]:
                print(f"✅ Video exists for {language}/{lesson}")
                print(json.dumps(result["video"], indent=2))
            else:
                print(f"❌ No video found for {language}/{lesson}")
            return result
        else:
            print(f"❌ Failed to check video: {response.status_code}")
            return None
    except Exception as e:
        print(f"❌ Error checking video: {e}")
        return None

def update_video(video_id, update_data):
    """Update a video record"""
    try:
        response = requests.put(f"{BASE_URL}/videos/{video_id}", json=update_data)
        if response.status_code == 200:
            print("✅ Video updated successfully!")
            return response.json()
        elif response.status_code == 404:
            print("❌ Video not found")
            return None
        else:
            print(f"❌ Failed to update video: {response.status_code}")
            print(response.text)
            return None
    except Exception as e:
        print(f"❌ Error updating video: {e}")
        return None

def delete_video(video_id):
    """Delete a video record"""
    try:
        response = requests.delete(f"{BASE_URL}/videos/{video_id}")
        if response.status_code == 200:
            print("✅ Video deleted successfully!")
            return True
        elif response.status_code == 404:
            print("❌ Video not found")
            return False
        else:
            print(f"❌ Failed to delete video: {response.status_code}")
            print(response.text)
            return False
    except Exception as e:
        print(f"❌ Error deleting video: {e}")
        return False

def demo():
    """Run a complete demo of the API"""
    print("🎬 Node.js Backend API Demo")
    print("=" * 30)
    
    # Test connection
    try:
        response = requests.get("http://localhost:5000")
        if response.status_code == 200:
            print("✅ Backend is running")
        else:
            print("❌ Backend is not responding")
            return
    except Exception as e:
        print(f"❌ Cannot connect to backend: {e}")
        return
    
    # Check if Python variables video exists
    print("\n1. Checking if Python variables video exists...")
    check_video_exists("python", "variables")
    
    # Create a sample video
    print("\n2. Creating a sample video...")
    video = create_sample_video()
    if not video:
        return
    
    video_id = video["_id"]
    
    # Get the video we just created
    print("\n3. Retrieving the created video...")
    get_video_by_id(video_id)
    
    # Get all videos
    print("\n4. Listing all videos...")
    get_all_videos()
    
    # Update the video
    print("\n5. Updating the video...")
    update_data = {
        "description": "Updated description: Comprehensive guide to Python variables",
        "duration": 360
    }
    update_video(video_id, update_data)
    
    # Check again
    print("\n6. Verifying the update...")
    get_video_by_id(video_id)
    
    # Clean up (optional)
    print("\n7. Demo complete!")
    print("💡 To clean up, you can delete the sample video with:")
    print(f"   DELETE {BASE_URL}/videos/{video_id}")

if __name__ == "__main__":
    demo()