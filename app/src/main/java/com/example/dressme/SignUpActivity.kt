package com.example.dressme


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import com.example.dressme.util.KeyboardAPI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*
import android.app.Activity
import android.view.inputmethod.InputMethodManager

class SignUpActivity : AppCompatActivity() {

   // val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    private val TAG: String = "SignUpActivity"
    private lateinit var spinner: ProgressBar

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        signUp_createAccount_button.setOnClickListener {
            // TODO refactor this code: Redundant with SIGNUP/SIGNUP
            spinner = signUp_spinner_progressBar
            spinner.setVisibility(View.VISIBLE)
            KeyboardAPI.hideKeyboard(this)

            signMeUp()
        }

        signUp_signIn_textClickable.setOnClickListener {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }

    private fun signMeUp() {
        if (!valid()) {
            spinner.setVisibility(View.GONE)
            return
        }

        val email           = signUp_email_textEdit.text.toString()
        val password        = signUp_password_textEdit.text.toString()

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (!it.isSuccessful) {
                    spinner.setVisibility(View.GONE)
                    return@addOnCompleteListener
                }

                Log.d(TAG, "SignUp has succeeded ${it.result?.user?.uid}")
                saveUserToFirebaseFirestore()

                val intent = Intent(this, ProfileMainActivity::class.java)

                spinner.setVisibility(View.GONE)
                startActivity(intent);
            }
            .addOnFailureListener {
                Log.d(TAG, "SignUp has failed ${it.message}")
            }
    }


    private fun saveUserToFirebaseFirestore() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseFirestore.getInstance().collection("users")

        val name            = signUp_name_textEdit.text.toString()
        val email           = signUp_email_textEdit.text.toString()

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
        val name            = signUp_name_textEdit.text.toString()
        val email           = signUp_email_textEdit.text.toString()
        val password        = signUp_password_textEdit.text.toString()
        val passwordReenter = signUp_passwordConf_textEdit.text.toString()

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