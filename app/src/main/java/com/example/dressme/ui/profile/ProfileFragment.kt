package com.example.dressme.ui.profile

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.example.dressme.AboutActivity
import com.example.dressme.SignInActivity
import com.example.dressme.R
import com.example.dressme.SettingsActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.view.*


class ProfileFragment : Fragment() {

    private val TAG: String = "ProfileFragment"

    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        profileViewModel    = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        val root            = inflater.inflate(R.layout.fragment_profile, container, false)
        profileViewModel.text.observe(this, Observer {

        })

        root.button_logout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            Log.d(TAG, "Successfully logged out")
            val intent = Intent(context, SignInActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                                Intent.FLAG_ACTIVITY_NEW_TASK or
                                Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity?.finish() //To "Destroy" The activity
            startActivity(intent)

        }

        root.button_settings.setOnClickListener {
            val intent = Intent(context, SettingsActivity::class.java)
            startActivity(intent)

        }

        root.button_about.setOnClickListener {
            val intent = Intent(context, AboutActivity::class.java)
            startActivity(intent)
        }

        return root


    }

}
