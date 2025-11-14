package com.servicehub.remote;

import com.servicehub.model.ChatRequest;
import com.servicehub.model.ChatResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("chatbot")
    Call<ChatResponse> sendMessage(@Body ChatRequest request);
}
