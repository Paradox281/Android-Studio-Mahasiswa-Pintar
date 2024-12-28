package com.semenam.mahasiswapintar.Model

import com.google.gson.annotations.SerializedName


data class Jurusan(
    @SerializedName("id_jurusan") val id: Int,
    @SerializedName("kode") val kode: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("kaprodi") val kaprodi: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)
