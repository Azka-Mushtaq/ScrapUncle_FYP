package com.example.scrapbuddy

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class ReferAFriendFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_refer_a_friend, container, false)

        val returnBack= view.findViewById<ImageButton>(R.id.refer_a_friend_back)

        returnBack.setOnClickListener()
        {
            loadFragment(ProfileFragment())
        }
        return view

    }

    private fun loadFragment(fragment:Fragment)
    {
        parentFragmentManager.beginTransaction().replace(R.id.frame_container,fragment).commit()
    }
}