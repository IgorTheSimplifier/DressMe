package com.example.dressme

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class InspectItemActivity : AppCompatActivity() {

    companion object {
        val TAG: String = "InspectItemActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inspect)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

}
