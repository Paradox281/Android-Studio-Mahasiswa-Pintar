package com.semenam.mahasiswapintar.Model

import com.google.gson.annotations.SerializedName

data class Mahasiswa(
    @SerializedName("id") val id: Int,
    @SerializedName("nim") val nim: String,
    @SerializedName("nama") val nama: String,
    @SerializedName("alamat") val alamat: String,
    @SerializedName("jeniskelamin") val jeniskelamin: String,
    @SerializedName("nohp") val nohp: String,
    @SerializedName("jurusan") val jurusan: String,
    @SerializedName("tanggallahir") val tanggallahir: String,
    @SerializedName("foto") val foto: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)