package com.harsh.codo.firebase

import android.content.Context
import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.io.File
import java.io.FileInputStream
import java.io.InputStream

class FirebaseVideoService {
    private val storage = FirebaseStorage.getInstance()
    private val firestore = FirebaseFirestore.getInstance()
    
    // Video lesson structure mapping
    private val videoLessonMap = mapOf(
        // Python Lessons
        "python_basics_variables" to "videos/python/basics/variables.mp4",
        "python_basics_data_types" to "videos/python/basics/data_types.mp4",
        "python_basics_operators" to "videos/python/basics/operators.mp4",
        "python_basics_input_output" to "videos/python/basics/input_output.mp4",
        
        "python_control_flow_if_statements" to "videos/python/control_flow/if_statements.mp4",
        "python_control_flow_loops" to "videos/python/control_flow/loops.mp4",
        "python_control_flow_functions" to "videos/python/control_flow/functions.mp4",
        
        "python_data_structures_lists" to "videos/python/data_structures/lists.mp4",
        "python_data_structures_dictionaries" to "videos/python/data_structures/dictionaries.mp4",
        "python_data_structures_tuples" to "videos/python/data_structures/tuples.mp4",
        
        // JavaScript Lessons
        "javascript_basics_variables" to "videos/javascript/basics/variables.mp4",
        "javascript_basics_data_types" to "videos/javascript/basics/data_types.mp4",
        "javascript_basics_operators" to "videos/javascript/basics/operators.mp4",
        "javascript_basics_functions" to "videos/javascript/basics/functions.mp4",
        
        "javascript_dom_manipulation_selectors" to "videos/javascript/dom_manipulation/selectors.mp4",
        "javascript_dom_manipulation_events" to "videos/javascript/dom_manipulation/events.mp4",
        "javascript_dom_manipulation_modifying_elements" to "videos/javascript/dom_manipulation/modifying_elements.mp4",
        
        // C++ Lessons
        "cpp_basics_variables" to "videos/cpp/basics/variables.mp4",
        "cpp_basics_data_types" to "videos/cpp/basics/data_types.mp4",
        "cpp_basics_operators" to "videos/cpp/basics/operators.mp4",
        "cpp_basics_functions" to "videos/cpp/basics/functions.mp4",
        
        "cpp_oop_classes" to "videos/cpp/oop/classes.mp4",
        "cpp_oop_inheritance" to "videos/cpp/oop/inheritance.mp4",
        "cpp_oop_polymorphism" to "videos/cpp/oop/polymorphism.mp4"
    )

    /**
     * Upload a video file to Firebase Storage and update database
     */
    suspend fun uploadVideo(
        lessonId: String,
        language: String,
        videoUri: Uri,
        context: Context
    ): Result<String> {
        return try {
            val storagePath = videoLessonMap[lessonId] ?: "videos/$language/$lessonId.mp4"
            val storageRef = storage.reference.child(storagePath)
            
            // Upload the video
            val uploadTask = storageRef.putFile(videoUri)
            uploadTask.await()
            
            // Get download URL
            val downloadUrl = storageRef.downloadUrl.await()
            
            // Update Firestore database
            updateVideoInDatabase(lessonId, language, downloadUrl.toString())
            
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Upload video from local file path
     */
    suspend fun uploadVideoFromPath(
        lessonId: String,
        language: String,
        filePath: String
    ): Result<String> {
        return try {
            val file = File(filePath)
            if (!file.exists()) {
                return Result.failure(Exception("File not found: $filePath"))
            }
            
            val storagePath = videoLessonMap[lessonId] ?: "videos/$language/$lessonId.mp4"
            val storageRef = storage.reference.child(storagePath)
            
            // Upload the file
            val inputStream = FileInputStream(file)
            val uploadTask = storageRef.putStream(inputStream)
            uploadTask.await()
            
            // Get download URL
            val downloadUrl = storageRef.downloadUrl.await()
            
            // Update Firestore database
            updateVideoInDatabase(lessonId, language, downloadUrl.toString())
            
            Result.success(downloadUrl.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Bulk upload videos from a directory
     */
    suspend fun bulkUploadVideos(
        videosDirectory: String,
        language: String
    ): Result<Map<String, String>> {
        val results = mutableMapOf<String, String>()
        val directory = File(videosDirectory)
        
        if (!directory.exists() || !directory.isDirectory) {
            return Result.failure(Exception("Directory not found: $videosDirectory"))
        }
        
        val videoFiles = directory.listFiles { file ->
            file.isFile && file.extension.lowercase() == "mp4"
        } ?: emptyArray()
        
        for (videoFile in videoFiles) {
            val lessonId = videoFile.nameWithoutExtension
            val fullLessonId = "${language}_${lessonId}"
            
            try {
                val result = uploadVideoFromPath(fullLessonId, language, videoFile.absolutePath)
                if (result.isSuccess) {
                    results[fullLessonId] = result.getOrNull() ?: ""
                } else {
                    println("Failed to upload ${videoFile.name}: ${result.exceptionOrNull()?.message}")
                }
            } catch (e: Exception) {
                println("Error uploading ${videoFile.name}: ${e.message}")
            }
        }
        
        return Result.success(results)
    }

    /**
     * Update video URL in Firestore database
     */
    private suspend fun updateVideoInDatabase(
        lessonId: String,
        language: String,
        videoUrl: String
    ) {
        try {
            val lessonData = mapOf(
                "videoUrl" to videoUrl,
                "hasVideo" to true,
                "lastUpdated" to com.google.firebase.Timestamp.now()
            )
            
            firestore.collection("lessons")
                .document("${language}_$lessonId")
                .set(lessonData, com.google.firebase.firestore.SetOptions.merge())
                .await()
                
            println("✅ Updated database for lesson: $lessonId")
        } catch (e: Exception) {
            println("❌ Failed to update database for lesson: $lessonId - ${e.message}")
        }
    }

    /**
     * Get video URL for a lesson
     */
    suspend fun getVideoUrl(lessonId: String, language: String): String? {
        return try {
            val document = firestore.collection("lessons")
                .document("${language}_$lessonId")
                .get()
                .await()
            
            document.getString("videoUrl")
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Check if lesson has video
     */
    suspend fun hasVideo(lessonId: String, language: String): Boolean {
        return try {
            val document = firestore.collection("lessons")
                .document("${language}_$lessonId")
                .get()
                .await()
            
            document.getBoolean("hasVideo") ?: false
        } catch (e: Exception) {
            false
        }
    }

    /**
     * Delete video from storage and database
     */
    suspend fun deleteVideo(lessonId: String, language: String): Result<Unit> {
        return try {
            val storagePath = videoLessonMap[lessonId] ?: "videos/$language/$lessonId.mp4"
            val storageRef = storage.reference.child(storagePath)
            
            // Delete from storage
            storageRef.delete().await()
            
            // Remove from database
            firestore.collection("lessons")
                .document("${language}_$lessonId")
                .update(
                    mapOf(
                        "videoUrl" to null,
                        "hasVideo" to false
                    )
                )
                .await()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Get all video URLs for a language
     */
    suspend fun getAllVideosForLanguage(language: String): Map<String, String> {
        return try {
            val snapshot = firestore.collection("lessons")
                .whereEqualTo("hasVideo", true)
                .get()
                .await()
            
            snapshot.documents.associate { doc ->
                val lessonId = doc.id.removePrefix("${language}_")
                lessonId to (doc.getString("videoUrl") ?: "")
            }
        } catch (e: Exception) {
            emptyMap()
        }
    }
} 