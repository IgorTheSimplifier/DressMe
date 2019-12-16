package com.example.dressme

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log

class SplashDialogActivity : Activity() {

    companion object {
        val TAG: String = "SplashDialogActivity"
    }
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splashscreen)

        val background = object : Thread() {
            override fun run() {
                try {
                    sleep(1000)
                    val intent = Intent(baseContext, ProfileMainActivity::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Log.d(TAG, e.toString())
                }

            }
        }

        background.start()
    }
}

