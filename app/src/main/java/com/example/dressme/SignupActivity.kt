package com.example.dressme

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val TAG: String = "SignupActivity"

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signup_button_signup.setOnClickListener {
            performSignup()
        }

        login_button_signup.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent);
        }
    }


    private fun performSignup() {
        if (!valid())
            return

        val email           = email_edittext_signup.text.toString()
        val password        = password_edittext_signup.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) return@addOnCompleteListener

                Log.d(TAG, "Signup has succeeded ${it.result?.user?.uid}")

                val intent = Intent(this, ProfileMainActivity::class.java)
                startActivity(intent);
            }
            .addOnFailureListener {
                Log.d(TAG, "Signup has failed ${it.message}")
            }
    }


    // $todo: signup validation update
    private fun valid(): Boolean {
        val email           = email_edittext_signup.text.toString()
        val password        = password_edittext_signup.text.toString()
        val passwordReenter = password_reenter_edittext_signup.text.toString()

        Log.d(TAG, "Email is  $email")
        Log.d(TAG, "Password is  $password")
        Log.d(TAG, "Password Reenter is  $passwordReenter")

        if (email.isEmpty() || password.isEmpty() || password != passwordReenter)
            return false

        return true
    }
}