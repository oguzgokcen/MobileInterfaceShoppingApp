package com.example.letgocloneapp.requestUrl

import com.example.letgocloneapp.Models.Login
import com.example.letgocloneapp.Models.User
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface LoginRegisterApiService {
    @POST("user/register")
    fun registerUser(@Body user: User): Call<User>

    @POST("user/login")
    fun loginUser(@Body login: Login): Call<User>
}