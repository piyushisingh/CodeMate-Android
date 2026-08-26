#!/usr/bin/env python3
"""
JavaScript Video Upload Script for CodeMate
Uploads specific JavaScript videos to Firebase Storage and database.
"""

import os
import shutil
from pathlib import Path
import firebase_admin
from firebase_admin import credentials, storage, firestore

# Firebase configuration
FIREBASE_CONFIG = {
    "project_id": "codemate-harsh",
    "storage_bucket": "codemate-harsh.appspot.com"
}

# JavaScript video mappings provided by user
JAVASCRIPT_VIDEOS = {
    "variables": "D:\\codemate videos\\javscript\\03. What is a Variable_.mp4",
    "operators": "D:\\codemate videos\\javscript\\04. Operators in JS.mp4", 
    "functions": "D:\\codemate videos\\javscript\\10. Array (Data Structure).mp4",
    "arrays": "D:\\codemate videos\\javscript\\04. Functions with Arguments.mp4"
}

class JavaScriptVideoUploader:
    def __init__(self):
        self.upload_results = []
        self.bucket = None
        self.db = None
        
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
    
    def upload_javascript_videos(self):
        """Upload JavaScript videos to Firebase"""
        if self.bucket is None or self.db is None:
            print("❌ Firebase not initialized. Cannot upload videos.")
            return
            
        print("📤 Uploading JavaScript videos...")
        
        for lesson, video_path in JAVASCRIPT_VIDEOS.items():
            if os.path.exists(video_path):
                try:
                    # Determine the correct section and storage path
                    if lesson in ["variables", "operators"]:
                        section = "basics"
                    elif lesson in ["functions", "arrays"]:
                        section = "data_structures"  # Adjusted based on content
                    
                    storage_path = f"videos/javascript/{section}/{lesson}.mp4"
                    blob = self.bucket.blob(storage_path)
                    
                    print(f"📤 Uploading {os.path.basename(video_path)} as {lesson}.mp4...")
                    blob.upload_from_filename(video_path)
                    
                    # Make public and get URL
                    blob.make_public()
                    video_url = blob.public_url
                    
                    # Update Firestore database
                    lesson_id = f"javascript_{section}_{lesson}"
                    doc_ref = self.db.collection("lessons").document(lesson_id)
                    
                    doc_ref.set({
                        "videoUrl": video_url,
                        "hasVideo": True,
                        "language": "javascript",
                        "section": section,
                        "lesson": lesson,
                        "originalFileName": os.path.basename(video_path),
                        "lastUpdated": firestore.SERVER_TIMESTAMP
                    }, merge=True)
                    
                    self.upload_results.append({
                        "lesson": lesson_id,
                        "success": True,
                        "url": video_url,
                        "originalFile": os.path.basename(video_path)
                    })
                    
                    print(f"✅ Uploaded {lesson_id}")
                    
                except Exception as e:
                    print(f"❌ Failed to upload {lesson}: {e}")
                    self.upload_results.append({
                        "lesson": f"javascript_{section}_{lesson}",
                        "success": False,
                        "error": str(e),
                        "originalFile": os.path.basename(video_path)
                    })
            else:
                print(f"⚠️  Video file not found: {video_path}")
                self.upload_results.append({
                    "lesson": lesson,
                    "success": False,
                    "error": "File not found",
                    "originalFile": os.path.basename(video_path)
                })
    
    def generate_report(self):
        """Generate upload report"""
        successful = [r for r in self.upload_results if r["success"]]
        failed = [r for r in self.upload_results if not r["success"]]
        
        report = f"""
# 📊 JavaScript Video Upload Report

## Summary
- Total videos processed: {len(self.upload_results)}
- Successful uploads: {len(successful)}
- Failed uploads: {len(failed)}

## ✅ Successful Uploads
"""
        
        for result in successful:
            report += f"- **{result['lesson']}**: {result['originalFile']} → {result['url']}\n"
        
        if failed:
            report += "\n## ❌ Failed Uploads\n"
            for result in failed:
                report += f"- **{result['lesson']}**: {result['originalFile']} → {result['error']}\n"
        
        # Save report
        with open("javascript_upload_report.md", "w", encoding="utf-8") as f:
            f.write(report)
        
        print("📄 Upload report saved to: javascript_upload_report.md")
        return report

def main():
    print("🎥 JavaScript Video Upload for CodeMate")
    print("=" * 50)
    
    # Check if videos exist
    missing_videos = []
    for lesson, path in JAVASCRIPT_VIDEOS.items():
        if not os.path.exists(path):
            missing_videos.append(f"{lesson}: {path}")
    
    if missing_videos:
        print("❌ Some video files are missing:")
        for missing in missing_videos:
            print(f"   - {missing}")
        return
    
    print("✅ All video files found!")
    print("\nVideos to upload:")
    for lesson, path in JAVASCRIPT_VIDEOS.items():
        print(f"   - {lesson}: {os.path.basename(path)}")
    
    # Create uploader
    uploader = JavaScriptVideoUploader()
    
    # Ask for confirmation
    print("\n" + "=" * 50)
    confirm = input("Do you want to upload these JavaScript videos? (y/N): ").strip().lower()
    
    if confirm == 'y':
        # Upload videos
        uploader.upload_javascript_videos()
        
        # Generate report
        report = uploader.generate_report()
        print("\n" + report)
        
        print("\n🎉 JavaScript video upload completed!")
        print("📱 Videos are now available in your CodeMate app")
        print("\n📋 Video Mappings:")
        print("   - Variables: '03. What is a Variable_.mp4'")
        print("   - Operators: '04. Operators in JS.mp4'")
        print("   - Functions: '10. Array (Data Structure).mp4'")
        print("   - Arrays: '04. Functions with Arguments.mp4'")
    else:
        print("❌ Upload cancelled")

if __name__ == "__main__":
    main() 