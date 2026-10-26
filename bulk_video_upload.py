#!/usr/bin/env python3
"""
Bulk Video Upload Script for CodeMate
This script helps you organize and upload videos to Firebase Storage and database.
"""

import os
import json
import shutil
from pathlib import Path
import firebase_admin
from firebase_admin import credentials, storage, firestore
import asyncio
import aiofiles

# Firebase configuration
FIREBASE_CONFIG = {
    "project_id": "codemate-harsh",
    "storage_bucket": "codemate-harsh.appspot.com"
}

# Video structure mapping
VIDEO_STRUCTURE = {
    "python": {
        "basics": ["variables", "data_types", "operators", "input_output"],
        "control_flow": ["if_statements", "loops", "functions"],
        "data_structures": ["lists", "dictionaries", "tuples"]
    },
    "javascript": {
        "basics": ["variables", "data_types", "operators", "functions"],
        "dom_manipulation": ["selectors", "events", "modifying_elements"]
    },
    "cpp": {
        "basics": ["variables", "data_types", "operators", "functions"],
        "oop": ["classes", "inheritance", "polymorphism"]
    }
}

class VideoUploader:
    def __init__(self, videos_directory: str):
        self.videos_dir = Path(videos_directory)
        self.organized_dir = Path("organized_videos")
        self.upload_results = []
        
        # Initialize Firebase
        try:
            cred = credentials.Certificate("google-services.json")
            firebase_admin.initialize_app(cred, FIREBASE_CONFIG)
            self.bucket = storage.bucket()
            self.db = firestore.client()
            print("✅ Firebase initialized successfully")
        except Exception as e:
            print(f"❌ Firebase initialization failed: {e}")
            print("Please ensure google-services.json is in the current directory")
    
    def organize_videos(self):
        """Organize videos into the correct structure"""
        print("📁 Organizing videos...")
        
        # Create organized directory structure
        for language, sections in VIDEO_STRUCTURE.items():
            for section, lessons in sections.items():
                section_dir = self.organized_dir / language / section
                section_dir.mkdir(parents=True, exist_ok=True)
                
                # Look for videos in the source directory
                for lesson in lessons:
                    # Try different possible filenames
                    possible_names = [
                        f"{lesson}.mp4",
                        f"{lesson}.MP4",
                        f"{lesson}_video.mp4",
                        f"{lesson}_lesson.mp4",
                        f"{lesson}_tutorial.mp4"
                    ]
                    
                    source_file = None
                    for name in possible_names:
                        potential_file = self.videos_dir / name
                        if potential_file.exists():
                            source_file = potential_file
                            break
                    
                    if source_file:
                        target_file = section_dir / f"{lesson}.mp4"
                        shutil.copy2(source_file, target_file)
                        print(f"✅ Copied {source_file.name} → {target_file}")
                    else:
                        print(f"⚠️  No video found for lesson: {lesson}")
        
        print(f"📂 Organized videos saved to: {self.organized_dir}")
    
    async def upload_to_firebase(self):
        """Upload organized videos to Firebase Storage and update database"""
        print("☁️ Uploading to Firebase...")
        
        for language, sections in VIDEO_STRUCTURE.items():
            for section, lessons in sections.items():
                for lesson in lessons:
                    video_path = self.organized_dir / language / section / f"{lesson}.mp4"
                    
                    if video_path.exists():
                        try:
                            # Upload to Firebase Storage
                            storage_path = f"videos/{language}/{section}/{lesson}.mp4"
                            blob = self.bucket.blob(storage_path)
                            
                            print(f"📤 Uploading {video_path.name}...")
                            blob.upload_from_filename(str(video_path))
                            
                            # Make public and get URL
                            blob.make_public()
                            video_url = blob.public_url
                            
                            # Update Firestore database
                            lesson_id = f"{language}_{section}_{lesson}"
                            doc_ref = self.db.collection("lessons").document(lesson_id)
                            
                            doc_ref.set({
                                "videoUrl": video_url,
                                "hasVideo": True,
                                "language": language,
                                "section": section,
                                "lesson": lesson,
                                "lastUpdated": firestore.SERVER_TIMESTAMP
                            }, merge=True)
                            
                            self.upload_results.append({
                                "lesson": lesson_id,
                                "success": True,
                                "url": video_url
                            })
                            
                            print(f"✅ Uploaded {lesson_id}")
                            
                        except Exception as e:
                            print(f"❌ Failed to upload {lesson}: {e}")
                            self.upload_results.append({
                                "lesson": f"{language}_{section}_{lesson}",
                                "success": False,
                                "error": str(e)
                            })
                    else:
                        print(f"⚠️  Video not found: {video_path}")
    
    def generate_upload_report(self):
        """Generate a report of the upload results"""
        successful = [r for r in self.upload_results if r["success"]]
        failed = [r for r in self.upload_results if not r["success"]]
        
        report = f"""
# 📊 Video Upload Report

## Summary
- Total videos processed: {len(self.upload_results)}
- Successful uploads: {len(successful)}
- Failed uploads: {len(failed)}

## ✅ Successful Uploads
"""
        
        for result in successful:
            report += f"- {result['lesson']}: {result['url']}\n"
        
        if failed:
            report += "\n## ❌ Failed Uploads\n"
            for result in failed:
                report += f"- {result['lesson']}: {result['error']}\n"
        
        # Save report
        with open("upload_report.md", "w", encoding="utf-8") as f:
            f.write(report)
        
        print("📄 Upload report saved to: upload_report.md")
        return report

def main():
    print("🎥 CodeMate Bulk Video Upload Tool")
    print("=" * 50)
    
    # Get videos directory
    videos_dir = input("Enter the path to your videos directory: ").strip()
    
    if not os.path.exists(videos_dir):
        print(f"❌ Directory not found: {videos_dir}")
        return
    
    # Create uploader
    uploader = VideoUploader(videos_dir)
    
    # Organize videos
    uploader.organize_videos()
    
    # Ask for confirmation
    print("\n" + "=" * 50)
    confirm = input("Do you want to upload these videos to Firebase? (y/N): ").strip().lower()
    
    if confirm == 'y':
        # Upload to Firebase
        asyncio.run(uploader.upload_to_firebase())
        
        # Generate report
        report = uploader.generate_upload_report()
        print("\n" + report)
        
        print("\n🎉 Upload process completed!")
        print("📱 Videos are now available in your CodeMate app")
    else:
        print("❌ Upload cancelled")

if __name__ == "__main__":
    main() 