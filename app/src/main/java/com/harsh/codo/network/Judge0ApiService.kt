package com.harsh.codo.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.Call

data class SubmissionRequest(
    val source_code: String,
    val language_id: Int, // e.g., 71 for Python 3
    val stdin: String? = null
)

data class ResultResponse(
    val stdout: String?,
    val stderr: String?,
    val compile_output: String?,
    val status: Status?
)

data class Status(
    val id: Int,
    val description: String
)

interface Judge0ApiService {
    @Headers("Content-Type: application/json")
    @POST("submissions/?base64_encoded=false&wait=true")
    fun createSubmission(@Body request: SubmissionRequest): Call<ResultResponse>
} 