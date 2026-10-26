import os
import argparse
import firebase_admin
from firebase_admin import credentials, storage, firestore
import json

def initialize_firebase():
    """Initializes the Firebase Admin SDK"""
    try:
        # Check for service account key file
        if not os.path.exists('google-services.json'):
            raise FileNotFoundError("Firebase service account key ('google-services.json') not found.")

        # Get storage bucket from google-services.json
        with open('google-services.json', 'r') as f:
            project_info = json.load(f)['project_info']
            storage_bucket = project_info['storage_bucket']

        cred = credentials.Certificate('google-services.json')
        firebase_admin.initialize_app(cred, {
            'storageBucket': storage_bucket
        })
        print("✅ Firebase initialized successfully.")
        return True
    except Exception as e:
        print(f"❌ Error initializing Firebase: {e}")
        return False

def upload_video(language, section, lesson_id, file_path):
    """Uploads a video to Firebase Storage and updates Firestore."""
    if not os.path.exists(file_path):
        print(f"❌ Video file not found at: {file_path}")
        return

    # Construct the destination path in Firebase Storage
    storage_path = f"videos/{language}/{section}/{lesson_id}.mp4"
    
    bucket = storage.bucket()
    blob = bucket.blob(storage_path)

    print(f"🔄 Uploading '{file_path}' to '{storage_path}'...")
    try:
        blob.upload_from_filename(file_path)
        print("✅ Upload successful!")

        # Make the blob publicly accessible and get the URL
        blob.make_public()
        download_url = blob.public_url
        
        print(f"🔗 Video URL: {download_url}")
        
        # Update Firestore
        update_firestore(language, section, lesson_id, download_url)

    except Exception as e:
        print(f"❌ Upload failed: {e}")

def update_firestore(language, section, lesson_id, video_url):
    """Updates the corresponding lesson document in Firestore."""
    db = firestore.client()
    
    # Document ID format: language_section_lessonId
    document_id = f"{language}_{section}_{lesson_id}"
    doc_ref = db.collection('lessons').document(document_id)
    
    print(f"🔄 Updating Firestore document: {document_id}...")
    try:
        doc_ref.set({
            'videoUrl': video_url,
            'hasVideo': True,
            'lastUpdated': firestore.SERVER_TIMESTAMP
        }, merge=True)
        print("✅ Firestore updated successfully!")
    except Exception as e:
        print(f"❌ Firestore update failed: {e}")

def main():
    parser = argparse.ArgumentParser(description="Upload a single video to Firebase for CodeMate.")
    parser.add_argument("language", help="The programming language (e.g., python, javascript, cpp).")
    parser.add_argument("section", help="The lesson section (e.g., basics, control_flow).")
    parser.add_argument("lesson_id", help="The ID of the lesson (e.g., if_statements, variables).")
    parser.add_argument("file_path", help="The local path to the video file.")
    
    args = parser.parse_args()

    if initialize_firebase():
        upload_video(args.language, args.section, args.lesson_id, args.file_path)

if __name__ == "__main__":
    main() 