package com.example.dressme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_button.setOnClickListener{
            performLogin();
        }
    }

    private fun performLogin() {
        val email       = email_edittext.text.toString()
        val password    = password_edittext.text.toString()

        Log.d("LoginActivity", "Email is  $email")
        Log.d("LoginActivity", "Password is  $password")
    }
}
