package com.semenam.mahasiswapintar.Model

import com.google.gson.annotations.SerializedName

data class MataKuliah(
    @SerializedName("id") val id: Int,
    @SerializedName("kode") val kode: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("semester") val semester: String,
    @SerializedName("sks") val sks: Int,
    @SerializedName("jurusan") val jurusan: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)