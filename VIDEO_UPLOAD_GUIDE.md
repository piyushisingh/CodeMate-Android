# 🎥 Video Upload Guide for CodeMate

## 📁 Required Video Structure

### PYTHON
#### Basics
- `variables.mp4` → `videos/python/basics/variables.mp4`
- `data_types.mp4` → `videos/python/basics/data_types.mp4`
- `operators.mp4` → `videos/python/basics/operators.mp4`
- `input_output.mp4` → `videos/python/basics/input_output.mp4`

#### Control Flow
- `if_statements.mp4` → `videos/python/control_flow/if_statements.mp4`
- `loops.mp4` → `videos/python/control_flow/loops.mp4`
- `functions.mp4` → `videos/python/control_flow/functions.mp4`

#### Data Structures
- `lists.mp4` → `videos/python/data_structures/lists.mp4`
- `dictionaries.mp4` → `videos/python/data_structures/dictionaries.mp4`
- `tuples.mp4` → `videos/python/data_structures/tuples.mp4`

### JAVASCRIPT
#### Basics
- `variables.mp4` → `videos/javascript/basics/variables.mp4`
- `data_types.mp4` → `videos/javascript/basics/data_types.mp4`
- `operators.mp4` → `videos/javascript/basics/operators.mp4`
- `functions.mp4` → `videos/javascript/basics/functions.mp4`

#### Dom Manipulation
- `selectors.mp4` → `videos/javascript/dom_manipulation/selectors.mp4`
- `events.mp4` → `videos/javascript/dom_manipulation/events.mp4`
- `modifying_elements.mp4` → `videos/javascript/dom_manipulation/modifying_elements.mp4`

### CPP
#### Basics
- `variables.mp4` → `videos/cpp/basics/variables.mp4`
- `data_types.mp4` → `videos/cpp/basics/data_types.mp4`
- `operators.mp4` → `videos/cpp/basics/operators.mp4`
- `functions.mp4` → `videos/cpp/basics/functions.mp4`

#### Oop
- `classes.mp4` → `videos/cpp/oop/classes.mp4`
- `inheritance.mp4` → `videos/cpp/oop/inheritance.mp4`
- `polymorphism.mp4` → `videos/cpp/oop/polymorphism.mp4`

## 🚀 Upload Methods

### Method 1: App Upload (Recommended)
1. Open CodeMate app
2. Go to Learn → Select language
3. Click on lesson
4. Click upload icon (☁️)
5. Select video file

### Method 2: Firebase Console
1. Go to [Firebase Console](https://console.firebase.google.com/)
2. Select project: `codemate-harsh`
3. Go to Storage
4. Navigate to `videos/` folder
5. Upload videos to correct subfolders

### Method 3: Local Development
1. Place videos in `app/src/main/res/raw/`
2. Update `LearnViewModel.kt` to reference local videos
3. Build and test locally

## 📋 Video Requirements
- **Format**: MP4 (H.264)
- **Resolution**: 720p or 1080p
- **Duration**: 2-10 minutes per lesson
- **Audio**: Clear narration
- **Content**: Code examples + explanations

## 🎯 Priority Lessons (Start Here)
1. **Python If Statements** - `videos/python/control_flow/if_statements.mp4`
2. **Python Variables** - `videos/python/basics/variables.mp4`
3. **JavaScript Variables** - `videos/javascript/basics/variables.mp4`
4. **C++ Variables** - `videos/cpp/basics/variables.mp4`

## 🔧 Troubleshooting
- **Video not showing**: Check Firebase Storage permissions
- **Upload fails**: Verify file size < 100MB
- **Wrong lesson**: Ensure filename matches lesson ID exactly
- **Loading issues**: Check internet connection