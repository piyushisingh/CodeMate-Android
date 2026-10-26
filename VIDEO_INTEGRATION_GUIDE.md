# 🎥 Video Integration Guide for CodeMate

## 📋 What You Need to Do

You want to **pre-upload videos to the database** so that when someone unlocks a lesson (like "Loops and Iteration" after completing "If Statements"), the video is already there waiting for them.

## 🚀 How to Upload Your Videos

### Method 1: Using the Python Script (Recommended)

1. **Prepare your videos** in a folder (e.g., `C:\my_videos\`)
2. **Name your videos** according to the lesson structure:
   - `if_statements.mp4`
   - `loops.mp4`
   - `functions.mp4`
   - `variables.mp4`
   - etc.

3. **Run the bulk upload script**:
   ```bash
   python bulk_video_upload.py
   ```

4. **Enter your videos directory path** when prompted
5. **Confirm the upload** when asked

### Method 2: Using the App's Video Upload Manager

1. **Open your CodeMate app**
2. **Navigate to the Video Upload Manager** (you may need to add this to your navigation)
3. **Select the language** (Python, JavaScript, or C++)
4. **Click "Bulk Upload"**
5. **Enter the directory path** containing your videos
6. **Wait for upload to complete**

### Method 3: Manual Firebase Console Upload

1. **Go to [Firebase Console](https://console.firebase.google.com/)**
2. **Select your project** (`codemate-harsh`)
3. **Go to Storage** → `videos/` folder
4. **Create the structure**:
   ```
   videos/
   ├── python/
   │   ├── basics/
   │   │   ├── variables.mp4
   │   │   ├── data_types.mp4
   │   │   └── operators.mp4
   │   ├── control_flow/
   │   │   ├── if_statements.mp4
   │   │   ├── loops.mp4
   │   │   └── functions.mp4
   │   └── data_structures/
   │       ├── lists.mp4
   │       ├── dictionaries.mp4
   │       └── tuples.mp4
   ├── javascript/
   │   └── ...
   └── cpp/
       └── ...
   ```

## 📁 Required Video Structure

### Python Videos
```
python/
├── basics/
│   ├── variables.mp4
│   ├── data_types.mp4
│   ├── operators.mp4
│   └── input_output.mp4
├── control_flow/
│   ├── if_statements.mp4
│   ├── loops.mp4
│   └── functions.mp4
└── data_structures/
    ├── lists.mp4
    ├── dictionaries.mp4
    └── tuples.mp4
```

### JavaScript Videos
```
javascript/
├── basics/
│   ├── variables.mp4
│   ├── data_types.mp4
│   ├── operators.mp4
│   └── functions.mp4
└── dom_manipulation/
    ├── selectors.mp4
    ├── events.mp4
    └── modifying_elements.mp4
```

### C++ Videos
```
cpp/
├── basics/
│   ├── variables.mp4
│   ├── data_types.mp4
│   ├── operators.mp4
│   └── functions.mp4
└── oop/
    ├── classes.mp4
    ├── inheritance.mp4
    └── polymorphism.mp4
```

## 🎯 Priority Videos (Start Here)

1. **Python If Statements** - `if_statements.mp4`
2. **Python Loops** - `loops.mp4`
3. **Python Functions** - `functions.mp4`
4. **JavaScript Variables** - `variables.mp4`
5. **C++ Variables** - `variables.mp4`

## 📱 How It Works in the App

1. **User completes "If Statements"** → Gets XP and unlocks "Loops"
2. **User clicks on "Loops"** → Video is already loaded from database
3. **Video plays automatically** → User learns about loops
4. **After video** → Quiz starts
5. **User completes quiz** → Unlocks next lesson

## 🔧 Technical Details

### Database Structure
```json
{
  "lessons": {
    "python_control_flow_if_statements": {
      "videoUrl": "https://firebasestorage.googleapis.com/...",
      "hasVideo": true,
      "language": "python",
      "section": "control_flow",
      "lesson": "if_statements",
      "lastUpdated": "2024-01-01T00:00:00Z"
    }
  }
}
```

### Storage Structure
```
Firebase Storage/
└── videos/
    ├── python/
    │   ├── basics/
    │   ├── control_flow/
    │   └── data_structures/
    ├── javascript/
    └── cpp/
```

## ✅ What Happens After Upload

1. **Videos are uploaded** to Firebase Storage
2. **Database is updated** with video URLs
3. **App automatically detects** videos for each lesson
4. **Users see video icon** (▶️) next to lessons with videos
5. **Videos play automatically** when lesson is opened

## 🎉 Result

When someone unlocks "Loops and Iteration" after completing "If Statements", they will:
1. **See the video immediately** (no waiting)
2. **Learn from the video** before the quiz
3. **Have a smooth learning experience**

## 📞 Need Help?

If you have issues:
1. Check the `upload_report.md` file for errors
2. Verify your `google-services.json` is in the project root
3. Ensure your videos are MP4 format
4. Check Firebase Storage permissions

Your videos will be ready for users as soon as they unlock each lesson! 🚀 