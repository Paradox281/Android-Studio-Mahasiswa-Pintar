package com.semenam.mahasiswapintar.API

import com.semenam.mahasiswapintar.Model.MataKuliah
import com.semenam.mahasiswapintar.Response.ResponseMataKuliah
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface API_MataKuliah {
    @GET("matakuliah")
    fun getMatakuliah(): Call<ResponseMataKuliah>

    @POST("matakuliah")
    fun addMatakuliah(@Body matakuliah: MataKuliah): Call<MataKuliah>

    @PUT("matakuliah/{id}")
    fun updateMatakuliah(@Path("id") id: Int, @Body matakuliah: MataKuliah): Call<MataKuliah>

    @DELETE("matakuliah/{id}")
    fun deleteMatakuliah(@Path("id") id: Int): Call<Void>

    companion object {
        private const val BASE_URL = "https://72f0-223-255-227-24.ngrok-free.app/api/"

        fun create(): API_MataKuliah {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(API_MataKuliah::class.java)
        }
    }
}