package com.semenam.mahasiswapintar.Model

data class LoginModel(
    val conntent: Conntent,
    val message: String,
    val response_code: Int
) {
    data class Conntent(
        val active: Int,
        val api_token: String,
        val created_at: String,
        val email: Any,
        val id_user: Int,
        val password: String,
        val role: String,
        val updated_at: String,
        val username: String,
        val verify_key: Any
    )
}