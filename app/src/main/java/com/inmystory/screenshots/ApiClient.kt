package com.inmystory.screenshots

import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import java.io.File
import java.util.concurrent.TimeUnit

data class ApiResponse(
    val success: Boolean,
    val action: String? = null,
    val data: Map<String, Any>? = null,
    val folderPath: String? = null,
    val suggestedFilename: String? = null,
    val calendarIntent: CalendarIntent? = null,
    val shareText: String? = null,
    val searchQuery: String? = null,
    val error: String? = null
)

data class CalendarIntent(
    val title: String? = null,
    val location: String? = null,
    val description: String? = null,
    val begin: String? = null,
    val time: String? = null
)

interface ApiService {
    @Multipart
    @POST("/api/screenshot")
    suspend fun uploadScreenshot(
        @Part screenshot: MultipartBody.Part,
        @Part("action") action: okhttp3.RequestBody
    ): ApiResponse
}

object ApiClient {
    private const val BASE_URL = "https://screenshots.lethimbuild.com/"

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun processScreenshot(file: File, action: String): ApiResponse {
        val requestFile = file.asRequestBody("image/png".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("screenshot", file.name, requestFile)
        val actionBody = action.toRequestBody("text/plain".toMediaTypeOrNull())
        
        return apiService.uploadScreenshot(body, actionBody)
    }

    fun create() = this
}
