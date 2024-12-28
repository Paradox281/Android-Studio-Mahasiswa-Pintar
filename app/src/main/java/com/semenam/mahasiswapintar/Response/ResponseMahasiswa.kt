package com.semenam.mahasiswapintar.Response

import com.google.gson.annotations.SerializedName
import com.semenam.mahasiswapintar.Model.Mahasiswa

data class ResponseMahasiswa(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Mahasiswa>
)