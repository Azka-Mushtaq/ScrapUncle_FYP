package com.example.scrapbuddy

import User
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayout

class PickUpFragment : Fragment() {

    private var containerName: Int? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_pickup, container, false)


        loadFragment(UpcomingFragment())


        //Retrieve data from SharedPrefrences
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userRole = sharedPreferences.getString("userRole", "")

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                when (tab?.position) {
                    0 -> loadFragment(UpcomingFragment())
                    1 -> loadFragment(CompletedFragment())
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        return view

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.pickup_frame_container, fragment)
        transaction.commit()
    }
}