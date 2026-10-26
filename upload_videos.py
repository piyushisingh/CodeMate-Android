#!/usr/bin/env python3
"""
Video Upload Helper Script for CodeMate
This script helps you upload videos for all lessons systematically.
"""

import os
import json
from pathlib import Path

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

def generate_upload_guide():
    """Generate a comprehensive upload guide"""
    guide = []
    guide.append("# 🎥 Video Upload Guide for CodeMate")
    guide.append("")
    guide.append("## 📁 Required Video Structure")
    guide.append("")
    
    for language, sections in LESSONS.items():
        guide.append(f"### {language.upper()}")
        for section, lessons in sections.items():
            guide.append(f"#### {section.replace('_', ' ').title()}")
            for lesson in lessons:
                guide.append(f"- `{lesson}.mp4` → `videos/{language}/{section}/{lesson}.mp4`")
            guide.append("")
    
    guide.append("## 🚀 Upload Methods")
    guide.append("")
    guide.append("### Method 1: App Upload (Recommended)")
    guide.append("1. Open CodeMate app")
    guide.append("2. Go to Learn → Select language")
    guide.append("3. Click on lesson")
    guide.append("4. Click upload icon (☁️)")
    guide.append("5. Select video file")
    guide.append("")
    
    guide.append("### Method 2: Firebase Console")
    guide.append("1. Go to [Firebase Console](https://console.firebase.google.com/)")
    guide.append("2. Select project: `codemate-harsh`")
    guide.append("3. Go to Storage")
    guide.append("4. Navigate to `videos/` folder")
    guide.append("5. Upload videos to correct subfolders")
    guide.append("")
    
    guide.append("### Method 3: Local Development")
    guide.append("1. Place videos in `app/src/main/res/raw/`")
    guide.append("2. Update `LearnViewModel.kt` to reference local videos")
    guide.append("3. Build and test locally")
    guide.append("")
    
    guide.append("## 📋 Video Requirements")
    guide.append("- **Format**: MP4 (H.264)")
    guide.append("- **Resolution**: 720p or 1080p")
    guide.append("- **Duration**: 2-10 minutes per lesson")
    guide.append("- **Audio**: Clear narration")
    guide.append("- **Content**: Code examples + explanations")
    guide.append("")
    
    guide.append("## 🎯 Priority Lessons (Start Here)")
    guide.append("1. **Python If Statements** - `videos/python/control_flow/if_statements.mp4`")
    guide.append("2. **Python Variables** - `videos/python/basics/variables.mp4`")
    guide.append("3. **JavaScript Variables** - `videos/javascript/basics/variables.mp4`")
    guide.append("4. **C++ Variables** - `videos/cpp/basics/variables.mp4`")
    guide.append("")
    
    guide.append("## 🔧 Troubleshooting")
    guide.append("- **Video not showing**: Check Firebase Storage permissions")
    guide.append("- **Upload fails**: Verify file size < 100MB")
    guide.append("- **Wrong lesson**: Ensure filename matches lesson ID exactly")
    guide.append("- **Loading issues**: Check internet connection")
    
    return "\n".join(guide)

def create_upload_checklist():
    """Create a checklist for tracking uploads"""
    checklist = []
    checklist.append("# 📋 Video Upload Checklist")
    checklist.append("")
    checklist.append("## Python Videos")
    checklist.append("")
    
    for section, lessons in LESSONS["python"].items():
        checklist.append(f"### {section.replace('_', ' ').title()}")
        for lesson in lessons:
            checklist.append(f"- [ ] `{lesson}.mp4`")
        checklist.append("")
    
    checklist.append("## JavaScript Videos")
    checklist.append("")
    for section, lessons in LESSONS["javascript"].items():
        checklist.append(f"### {section.replace('_', ' ').title()}")
        for lesson in lessons:
            checklist.append(f"- [ ] `{lesson}.mp4`")
        checklist.append("")
    
    checklist.append("## C++ Videos")
    checklist.append("")
    for section, lessons in LESSONS["cpp"].items():
        checklist.append(f"### {section.replace('_', ' ').title()}")
        for lesson in lessons:
            checklist.append(f"- [ ] `{lesson}.mp4`")
        checklist.append("")
    
    return "\n".join(checklist)

if __name__ == "__main__":
    # Generate upload guide
    with open("VIDEO_UPLOAD_GUIDE.md", "w", encoding="utf-8") as f:
        f.write(generate_upload_guide())
    
    # Generate checklist
    with open("VIDEO_CHECKLIST.md", "w", encoding="utf-8") as f:
        f.write(create_upload_checklist())
    
    print("✅ Generated video upload guide and checklist!")
    print("📖 Read VIDEO_UPLOAD_GUIDE.md for detailed instructions")
    print("📋 Use VIDEO_CHECKLIST.md to track your progress")
    print("")
    print("🎯 Quick Start:")
    print("1. Create a video for 'if_statements' lesson")
    print("2. Name it 'if_statements.mp4'")
    print("3. Upload via app or Firebase Console")
    print("4. Test in the app!") 