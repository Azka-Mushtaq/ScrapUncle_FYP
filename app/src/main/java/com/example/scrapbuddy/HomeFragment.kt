package com.example.scrapbuddy

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button


class HomeFragment : Fragment() {

    private var containerName:Int? = null
    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        //User preferences
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userRole = sharedPreferences.getString("userRole", "")


        val pickupBtn= view.findViewById<Button>(R.id.schedule_pickup_button)
        if(userRole=="user")
        {
            pickupBtn.setText(R.string.SchedulePickup)
        }
        else
            pickupBtn.setText(R.string.RiderViewSchedulePickup)


        containerName = if (userRole == "user")
            R.id.frame_container
        else
            R.id.rider_frame_container

        pickupBtn.setOnClickListener()
        {
            loadFragment(SchedulePickUpFragment())
        }

        return view
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(containerName!!, fragment)
        transaction.commit()
    }

}