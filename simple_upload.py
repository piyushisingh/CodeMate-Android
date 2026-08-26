#!/usr/bin/env python3
"""
Simple Video Upload Script for CodeMate
Uploads JavaScript videos using Firebase Admin SDK
"""

import os
import json
from pathlib import Path

# JavaScript video mappings provided by user
JAVASCRIPT_VIDEOS = {
    "variables": "D:\\codemate videos\\javscript\\03. What is a Variable_.mp4",
    "operators": "D:\\codemate videos\\javscript\\04. Operators in JS.mp4", 
    "functions": "D:\\codemate videos\\javscript\\10. Array (Data Structure).mp4",
    "arrays": "D:\\codemate videos\\javscript\\04. Functions with Arguments.mp4"
}

def create_firebase_config():
    """Create Firebase configuration from google-services.json"""
    try:
        with open("google-services.json", "r") as f:
            config = json.load(f)
        
        project_info = config["project_info"]
        return {
            "project_id": project_info["project_id"],
            "storage_bucket": project_info["storage_bucket"]
        }
    except Exception as e:
        print(f"❌ Error reading google-services.json: {e}")
        return None

def upload_videos_manual():
    """Manual upload instructions"""
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
    
    # Get Firebase config
    firebase_config = create_firebase_config()
    if not firebase_config:
        print("❌ Could not read Firebase configuration")
        return
    
    print(f"\n📋 Firebase Project: {firebase_config['project_id']}")
    print(f"📦 Storage Bucket: {firebase_config['storage_bucket']}")
    
    print("\n" + "=" * 50)
    print("📤 MANUAL UPLOAD INSTRUCTIONS")
    print("=" * 50)
    
    print("\n1️⃣ Go to Firebase Console:")
    print(f"   https://console.firebase.google.com/project/{firebase_config['project_id']}/storage")
    
    print("\n2️⃣ Navigate to Storage → videos/javascript/")
    print("   Create these folders if they don't exist:")
    print("   - videos/javascript/basics/")
    print("   - videos/javascript/data_structures/")
    
    print("\n3️⃣ Upload videos to correct folders:")
    print("\n   📁 videos/javascript/basics/")
    print("   ├── variables.mp4 (from: 03. What is a Variable_.mp4)")
    print("   └── operators.mp4 (from: 04. Operators in JS.mp4)")
    
    print("\n   📁 videos/javascript/data_structures/")
    print("   ├── functions.mp4 (from: 10. Array (Data Structure).mp4)")
    print("   └── arrays.mp4 (from: 04. Functions with Arguments.mp4)")
    
    print("\n4️⃣ After uploading, update Firestore database:")
    print("   Go to Firestore → lessons collection")
    print("   Create documents with these IDs:")
    
    print("\n   📄 Document IDs to create:")
    print("   - javascript_basics_variables")
    print("   - javascript_basics_operators") 
    print("   - javascript_data_structures_functions")
    print("   - javascript_data_structures_arrays")
    
    print("\n   📄 Document fields for each:")
    print("   ```json")
    print("   {")
    print('     "videoUrl": "https://firebasestorage.googleapis.com/...",')
    print('     "hasVideo": true,')
    print('     "language": "javascript",')
    print('     "section": "basics", // or "data_structures"')
    print('     "lesson": "variables", // or "operators", "functions", "arrays"')
    print('     "lastUpdated": "2024-01-01T00:00:00Z"')
    print("   }")
    print("   ```")
    
    print("\n5️⃣ Get video URLs:")
    print("   - Right-click each uploaded video in Firebase Storage")
    print("   - Select 'Copy download URL'")
    print("   - Use that URL in the videoUrl field")
    
    print("\n🎉 After completing these steps, videos will appear in your app!")
    
    # Generate a detailed guide file
    generate_upload_guide(firebase_config)

def generate_upload_guide(firebase_config):
    """Generate a detailed upload guide"""
    guide = f"""
# 🎥 JavaScript Video Upload Guide

## Firebase Project Details
- **Project ID**: {firebase_config['project_id']}
- **Storage Bucket**: {firebase_config['storage_bucket']}

## Video Files to Upload

### From Your Computer:
1. `D:\\codemate videos\\javscript\\03. What is a Variable_.mp4` → `variables.mp4`
2. `D:\\codemate videos\\javscript\\04. Operators in JS.mp4` → `operators.mp4`
3. `D:\\codemate videos\\javscript\\10. Array (Data Structure).mp4` → `functions.mp4`
4. `D:\\codemate videos\\javscript\\04. Functions with Arguments.mp4` → `arrays.mp4`

## Step-by-Step Instructions

### Step 1: Access Firebase Console
1. Go to: https://console.firebase.google.com/project/{firebase_config['project_id']}/storage
2. Sign in with your Google account

### Step 2: Create Storage Folders
1. Click on "Storage" in the left sidebar
2. Navigate to the `videos` folder (create if it doesn't exist)
3. Create `javascript` folder inside `videos`
4. Create `basics` and `data_structures` folders inside `javascript`

### Step 3: Upload Videos
1. **For Basics Section:**
   - Go to `videos/javascript/basics/`
   - Upload `03. What is a Variable_.mp4` and rename to `variables.mp4`
   - Upload `04. Operators in JS.mp4` and rename to `operators.mp4`

2. **For Data Structures Section:**
   - Go to `videos/javascript/data_structures/`
   - Upload `10. Array (Data Structure).mp4` and rename to `functions.mp4`
   - Upload `04. Functions with Arguments.mp4` and rename to `arrays.mp4`

### Step 4: Get Video URLs
1. Right-click each uploaded video
2. Select "Copy download URL"
3. Save these URLs for the next step

### Step 5: Update Firestore Database
1. Go to Firestore Database in Firebase Console
2. Create a collection called `lessons` if it doesn't exist
3. Create these documents:

#### Document 1: `javascript_basics_variables`
```json
{{
  "videoUrl": "[PASTE_URL_HERE]",
  "hasVideo": true,
  "language": "javascript",
  "section": "basics",
  "lesson": "variables",
  "lastUpdated": "2024-01-01T00:00:00Z"
}}
```

#### Document 2: `javascript_basics_operators`
```json
{{
  "videoUrl": "[PASTE_URL_HERE]",
  "hasVideo": true,
  "language": "javascript",
  "section": "basics",
  "lesson": "operators",
  "lastUpdated": "2024-01-01T00:00:00Z"
}}
```

#### Document 3: `javascript_data_structures_functions`
```json
{{
  "videoUrl": "[PASTE_URL_HERE]",
  "hasVideo": true,
  "language": "javascript",
  "section": "data_structures",
  "lesson": "functions",
  "lastUpdated": "2024-01-01T00:00:00Z"
}}
```

#### Document 4: `javascript_data_structures_arrays`
```json
{{
  "videoUrl": "[PASTE_URL_HERE]",
  "hasVideo": true,
  "language": "javascript",
  "section": "data_structures",
  "lesson": "arrays",
  "lastUpdated": "2024-01-01T00:00:00Z"
}}
```

## Testing
After completing all steps:
1. Open your CodeMate app
2. Go to Learn → JavaScript
3. Click on any lesson (Variables, Operators, Functions, Arrays)
4. The video should appear automatically

## Troubleshooting
- If videos don't appear, check that the document IDs match exactly
- Ensure video URLs are accessible (try opening in browser)
- Verify that `hasVideo` is set to `true`
"""
    
    with open("JAVASCRIPT_UPLOAD_GUIDE.md", "w", encoding="utf-8") as f:
        f.write(guide)
    
    print(f"\n📄 Detailed guide saved to: JAVASCRIPT_UPLOAD_GUIDE.md")

if __name__ == "__main__":
    upload_videos_manual() 