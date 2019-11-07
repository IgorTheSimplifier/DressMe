package com.example.dressme

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG: String = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        login_button_main.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent);
        }

        signup_button_main.setOnClickListener{
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent);
        }
    }
}
