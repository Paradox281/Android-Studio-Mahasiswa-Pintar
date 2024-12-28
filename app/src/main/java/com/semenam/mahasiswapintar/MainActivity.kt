package com.semenam.mahasiswapintar

import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import android.view.MotionEvent
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.semenam.mahasiswapintar.API.API
import com.semenam.mahasiswapintar.Model.LoginModel
import com.semenam.mahasiswapintar.Response.SessionManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private val api by lazy { API.create() }
    private val session by lazy { SessionManager.init(applicationContext) }

    private lateinit var nim : EditText
    private lateinit var password : EditText
    private lateinit var tombol : Button

    private var passwordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (session.isLoggin){
            direktomenu()
        }

        nim = findViewById(R.id.Email)
        password = findViewById(R.id.Pass)
        tombol = findViewById(R.id.tombol)

        val eyeDrawable: Drawable? = ContextCompat.getDrawable(this, R.drawable.eye_off)
        eyeDrawable?.setBounds(0, 0, eyeDrawable.intrinsicWidth, eyeDrawable.intrinsicHeight)
        password.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (password.right - password.compoundDrawables[2].bounds.width())) {
                    togglePasswordVisibility()
                    return@setOnTouchListener true
                }
            }
            false
        }
        tombol.setOnClickListener {
            val usernameyangdiketikan = nim.text.toString()
            val passwordyangdiketikan = password.text.toString()

            if(usernameyangdiketikan.isEmpty()){
                nim.error = "Username Tidak Boleh Kosong"
            }else if (passwordyangdiketikan.isEmpty()) {
                password.error = "Password Tidak Boleh Kosong"
            }
            api.login(usernameyangdiketikan,passwordyangdiketikan).enqueue(object : Callback<LoginModel>{
                override fun onResponse(p0: Call<LoginModel>, response: Response<LoginModel>) {
                    val loginModel = response.body()
                    if (response.isSuccessful&&loginModel!=null) {
                        if (loginModel.response_code == 201) {
                            session.loginSession=loginModel
                            if (loginModel.conntent.role == "Administrator") {
                                direktomenuAdmin()
                            } else if (loginModel.conntent.role == "Pegawai") {
                                direktomenuPegawai()
                            }
                            Toast.makeText(this@MainActivity, "${loginModel.message}", Toast.LENGTH_SHORT).show()
                            Log.e("Hasil REspon API", "onResponse: ${response.body()}",)
                        } else {
                            Toast.makeText(this@MainActivity, "${loginModel?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                override fun onFailure(p0: Call<LoginModel>, p1: Throwable) {
                    Log.e("TAG", "onFailure: $p1", )
                }
            })

        }
    }

    private fun direktomenu() {
        startActivity(Intent(this, MenuActivity::class.java))
        this.finish()
    }
    private fun direktomenuAdmin() {
        startActivity(Intent(this, MenuActivity::class.java))
        this.finish()
    }

    private fun direktomenuPegawai() {
        startActivity(Intent(this, MenuActivity::class.java))
        this.finish()
    }
    private fun togglePasswordVisibility() {
        if (passwordVisible) {
            // Hide password
            password.transformationMethod = android.text.method.PasswordTransformationMethod.getInstance()
            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_off, 0)
        } else {
            // Show password
            password.transformationMethod = null
            password.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.eye_on_show_svgrepo_com, 0)
        }
        passwordVisible = !passwordVisible
    }
}
