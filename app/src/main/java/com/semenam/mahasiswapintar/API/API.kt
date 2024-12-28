package com.semenam.mahasiswapintar.API

import com.semenam.mahasiswapintar.Model.LoginModel
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface API {
    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("username") nama: String,
        @Field("password") password: String
    ): Call<LoginModel>
    companion object {
        fun create(): API {
            val httpClient = OkHttpClient.Builder()
            var logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY
            httpClient.addInterceptor(logging)

            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                //.baseUrl("http://10.0.2.2:8000/api/") // ini jika emulator hp di laptop
                .baseUrl("https://72f0-223-255-227-24.ngrok-free.app/api/")//Menggunakan ngrok
                .client(httpClient.build())
                .build()

            return retrofit.create(API::class.java)
        }
    }
}