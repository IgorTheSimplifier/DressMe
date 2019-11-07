package com.example.dressme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
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

        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password)
            .addOnCompleteListener {
                if (!it.isSuccessful)
                {
                    Log.d("LoginActivity", "Some kind of fuck up")
                    return@addOnCompleteListener
                }
                // else if successful
                Log.d("LoginActivity", "Successfully logged in ${it.result?.user?.uid}")
            }
    }
}
