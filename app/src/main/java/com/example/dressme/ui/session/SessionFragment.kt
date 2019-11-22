package com.example.dressme.ui.session

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.dressme.ItemViewActivity
import com.example.dressme.R
import kotlinx.android.synthetic.main.fragment_session.view.*

class SessionFragment : Fragment() {

    // $todo: add dynamic generation of marketplace items

    private lateinit var sessionViewModel: SessionViewModel;

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        sessionViewModel =
            ViewModelProviders.of(this).get(SessionViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_session, container, false)
        sessionViewModel.text.observe(this, Observer {
        })

        root.imageView21.setOnClickListener{
            val intent = Intent(context, ItemViewActivity::class.java)
            startActivity(intent);
        }

        return root
    }
}