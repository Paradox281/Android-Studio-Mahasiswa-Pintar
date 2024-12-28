package com.semenam.mahasiswapintar.API

import com.semenam.mahasiswapintar.Model.Mahasiswa
import com.semenam.mahasiswapintar.Response.ResponseMahasiswa
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface API_Mahasiswa {
    @GET("siswa")
    fun getMahasiswa(): Call<ResponseMahasiswa>

    @POST("siswa")
    fun addMahasiswa(@Body mahasiswa: Mahasiswa): Call<Mahasiswa>

    @PUT("siswa/{id}")
    fun updateMahasiswa(@Path("id") id: Int, @Body mahasiswa: Mahasiswa): Call<Mahasiswa>

    @DELETE("siswa/{id}")
    fun deleteMahasiswa(@Path("id") id: Int): Call<Void>

    companion object {
        private const val BASE_URL = "https://72f0-223-255-227-24.ngrok-free.app/api/"

        fun create(): API_Mahasiswa {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(API_Mahasiswa::class.java)
        }
    }
}