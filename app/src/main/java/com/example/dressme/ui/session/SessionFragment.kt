package com.example.dressme.ui.session

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.dressme.R


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


        // TODO Retrieve items via Firebase

        /* Setting up the recycler with its reference, adapter and orientation. */
        val objectsRecycler : RecyclerView = root.findViewById<RecyclerView>(R.id.session_objectList_recycler)
        val adapter = SalesObjectAdapter()

        objectsRecycler.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        objectsRecycler.adapter = adapter

        /* UX:  Recycler SnapHelper. */
        //val snapHelper : SnapHelperOneByOne = SnapHelperOneByOne()
        val snapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(objectsRecycler)

        /* Recycler cosmetics. */
        val dividerItemDecoration = DividerItemDecoration(
            objectsRecycler.context,
            LinearLayoutManager.HORIZONTAL
        )
        val horizontalSpaceItemDecoration =
            HorizontalSpaceItemDecoration()
        objectsRecycler.addItemDecoration(dividerItemDecoration)
        objectsRecycler.addItemDecoration(horizontalSpaceItemDecoration)

        // TODO add interactive links

        /*root.session_objectList_recycler.setOnClickListener{
            //val intent = Intent(context, InspectItemActivity::class.java)
            val intent = Intent(context, FirebaseActivity::class.java)
            startActivity(intent);
        }*/

        return root
    }
}