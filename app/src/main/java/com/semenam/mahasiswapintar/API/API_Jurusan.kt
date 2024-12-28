import com.semenam.mahasiswapintar.Model.Jurusan
import com.semenam.mahasiswapintar.Response.ResponseJurusan
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


interface API_Jurusan {
    @GET("jurusan")
    fun getJurusan(): Call<ResponseJurusan>

    @POST("jurusan")
    fun addJurusan(@Body jurusan: Jurusan): Call<Jurusan>

    @PUT("jurusan/{id}")
    fun updateJurusan(@Path("id") id: Int, @Body jurusan: Jurusan): Call<Jurusan>

    @DELETE("jurusan/{id}")
    fun deleteJurusan(@Path("id") id: Int): Call<Void>

    companion object {
        private const val BASE_URL = "https://72f0-223-255-227-24.ngrok-free.app/api/"

        fun create(): API_Jurusan {
            val retrofit = Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(BASE_URL)
                .build()
            return retrofit.create(API_Jurusan::class.java)
        }
    }
}
