package com.semenam.mahasiswapintar.Splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.semenam.mahasiswapintar.MainActivity
import com.semenam.mahasiswapintar.R

class Splash : AppCompatActivity() {
    private val SPLASH_TIME_OUT: Long = 2000 // Durasi splash screen dalam milidetik
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash)
        Handler().postDelayed({
            // Aksi yang akan dijalankan setelah SPLASH_TIME_OUT
            startActivity(Intent(this, MainActivity::class.java))
            finish() // Menutup activity splash agar tidak bisa dikembalikan
        }, SPLASH_TIME_OUT)
    }
}