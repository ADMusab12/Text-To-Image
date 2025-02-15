package com.codetech.texttoimage.abstraction

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.codetech.texttoimage.abstraction.model.HuggingFaceRequest
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit

class HuggingFaceImageGenerator {


    private val api: HuggingFaceApi
    private val TAG = "HuggingFaceImageGeneratorInfo"
    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(object : Interceptor {
            override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
                val request = chain.request()
                var response: okhttp3.Response? = null
                var tryCount = 0
                val maxTries = 3

                while (tryCount < maxTries) {
                    try {
                        response = chain.proceed(request)
                        if (response.isSuccessful) return response
                    } catch (e: IOException) {
                        Log.d(TAG, "Request failed - retrying (${tryCount + 1}/$maxTries)")
                    }
                    tryCount++
                }

                return response ?: okhttp3.Response.Builder()
                    .request(request)
                    .protocol(Protocol.HTTP_1_1)
                    .code(500)
                    .message("Failed after $maxTries retries")
                    .body(ResponseBody.create(null, ""))
                    .build()
            }
        })
        .build()

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api-inference.huggingface.co/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        api = retrofit.create(HuggingFaceApi::class.java)
    }

    fun generateImage(prompt: String,model:String, callback: (Bitmap?) -> Unit) {
        val request = HuggingFaceRequest(inputs = prompt)

        val call: Call<ResponseBody>? = when (model) {
            "flux" -> api.generateImage("Bearer //YOUR ACCESS TOKEN", request)
            "president"-> api.generatePresidentImage("Bearer //YOUR ACCESS TOKEN", request)
            "stable"-> api.generateStableDiffusionImage("Bearer //YOUR ACCESS TOKEN", request)
             else -> null
        }

        Log.d(TAG, "generateImage:Model Name: $model")

        call?.enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                if (response.isSuccessful) {
                    val imageBytes = response.body()?.use { responseBody ->
                        // Read the bytes and automatically close the ResponseBody
                        responseBody.bytes()
                    }

                    if (imageBytes != null) {
                        val bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
                        callback(bitmap)
                    } else {
                        Log.d(TAG, "onResponse: Image bytes are null")
                        callback(null)
                    }
                } else {
                    // Log the error response
                    val errorBody = response.errorBody()?.use { it.string() }
                    Log.d(TAG, "onResponse:fail $errorBody")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(TAG, "onFailure: ${t.message}")
                if (t is HttpException) {
                    val errorResponse = t.response()?.errorBody()?.use { it.string() }
                    Log.d(TAG, "onFailure: Error Response: $errorResponse")
                }
                callback(null)
            }
        })
    }


}