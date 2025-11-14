package com.servicehub.remote

import com.servicehub.model.ChatRequest
import com.servicehub.model.ChatResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("chatbot")
    fun sendMessage(@Body request: ChatRequest): Call<ChatResponse>
}
