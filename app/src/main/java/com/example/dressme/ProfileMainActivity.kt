package com.example.dressme

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController

class ProfileMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_add, R.id.navigation_search,R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // YAN's part
        /* Compatibility problem with firebase
           val navView: BottomNavigationView = findViewById(R.id.nav_view)
           val navController = findNavController(R.id.nav_host_fragment)
           // Passing each menu ID as a set of Ids because each
           // menu should be considered as top level destinations.
           val appBarConfiguration = AppBarConfiguration(setOf(
               R.id.navigation_home,
               R.id.navigation_home,
               R.id.navigation_home)
           )
           setupActionBarWithNavController(navController, appBarConfiguration)
           navView.setupWithNavController(navController) */
    }
}
