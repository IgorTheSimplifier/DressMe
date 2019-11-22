package com.example.dressme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val TAG: String = "SignupActivity"
    private lateinit var spinner: ProgressBar

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        button_create_account.setOnClickListener {
            spinner = loading_spinner
            spinner.setVisibility(View.VISIBLE)

            performSignup()
        }

        button_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent);
        }
    }


    private fun performSignup() {
        if (!valid()) {
            spinner.setVisibility(View.GONE)
            return
        }

        val email           = textEdit_email.text.toString()
        val password        = textEdit_password.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    spinner.setVisibility(View.GONE)
                    return@addOnCompleteListener
                }

                Log.d(TAG, "Signup has succeeded ${it.result?.user?.uid}")
                saveUserToFirebaseFirestore()

                val intent = Intent(this, ProfileMainActivity::class.java)

                spinner.setVisibility(View.GONE)
                startActivity(intent);
            }
            .addOnFailureListener {
                Log.d(TAG, "Signup has failed ${it.message}")
            }
    }


    private fun saveUserToFirebaseFirestore() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseFirestore.getInstance().collection("users")

        val name            = textEdit_name.text.toString()
        val email           = textEdit_email.text.toString()

        val user = User(name, email)

        ref.document(uid).set(user)
            .addOnSuccessListener {
                Log.d(TAG, "Our user has been saved to Firebase Database")
            }
            .addOnFailureListener {
                Log.w(TAG, "Error adding document ", it)
            }

    }

    // $todo: signup validation update
    private fun valid(): Boolean {
        val name            = textEdit_name.text.toString()
        val email           = textEdit_email.text.toString()
        val password        = textEdit_password.text.toString()
        val passwordReenter = textEdit_password_confirmation.text.toString()

        Log.d(TAG, "Name is  $name")
        Log.d(TAG, "Email is  $email")
        Log.d(TAG, "Password is  $password")
        Log.d(TAG, "Password Reenter is  $passwordReenter")

        if (name.isEmpty() ||
            email.isEmpty() ||
            password.isEmpty() ||
            password != passwordReenter) return false

        return true
    }
}

data class User(val name: String = "", val email: String = "")