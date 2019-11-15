package com.example.dressme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    private val TAG: String = "SignupActivity"

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        confirm_button_setting.setOnClickListener {
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
                saveUserToFirebaseDatabase()

                val intent = Intent(this, ProfileMainActivity::class.java)
                startActivity(intent);
            }
            .addOnFailureListener {
                Log.d(TAG, "Signup has failed ${it.message}")
            }
    }


    private fun saveUserToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseFirestore.getInstance().collection("users")

        val name            = name_edittext_signup.text.toString()
        val email           = email_edittext_signup.text.toString()

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
        val name            = name_edittext_signup.text.toString()
        val email           = email_edittext_signup.text.toString()
        val password        = password_edittext_signup.text.toString()
        val passwordReenter = password_reenter_edittext_signup.text.toString()

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

class User(val name: String, val email: String)