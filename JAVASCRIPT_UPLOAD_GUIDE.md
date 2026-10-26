
# 🎥 JavaScript Video Upload Guide

## Firebase Project Details
- **Project ID**: codemate-harsh
- **Storage Bucket**: codemate-harsh.appspot.com

## Video Files to Upload

### From Your Computer:
1. `D:\codemate videos\javscript\03. What is a Variable_.mp4` → `variables.mp4`
2. `D:\codemate videos\javscript\04. Operators in JS.mp4` → `operators.mp4`
3. `D:\codemate videos\javscript\10. Array (Data Structure).mp4` → `functions.mp4`
4. `D:\codemate videos\javscript\04. Functions with Arguments.mp4` → `arrays.mp4`

## Step-by-Step Instructions

### Step 1: Access Firebase Console
1. Go to: https://console.firebase.google.com/project/codemate-harsh/storage
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
{
  "videoUrl": "[PASTE_URL_HERE]",
  "hasVideo": true,
  "language": "javascript",
  "section": "basics",
  "lesson": "variables",
  "lastUpdated": "2024-01-01T00:00:00Z"
}
```

#### Document 2: `javascript_basics_operators`
```json
{
  "videoUrl": "[PASTE_URL_HERE]",
  "hasVideo": true,
  "language": "javascript",
  "section": "basics",
  "lesson": "operators",
  "lastUpdated": "2024-01-01T00:00:00Z"
}
```

#### Document 3: `javascript_data_structures_functions`
```json
{
  "videoUrl": "[PASTE_URL_HERE]",
  "hasVideo": true,
  "language": "javascript",
  "section": "data_structures",
  "lesson": "functions",
  "lastUpdated": "2024-01-01T00:00:00Z"
}
```

#### Document 4: `javascript_data_structures_arrays`
```json
{
  "videoUrl": "[PASTE_URL_HERE]",
  "hasVideo": true,
  "language": "javascript",
  "section": "data_structures",
  "lesson": "arrays",
  "lastUpdated": "2024-01-01T00:00:00Z"
}
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
