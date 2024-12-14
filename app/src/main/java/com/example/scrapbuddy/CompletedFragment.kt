package com.example.scrapbuddy

import MySQLiteHelper
import Pickup
import PickupAdapter
import User
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView


class CompletedFragment : Fragment() {
    private var containerName: Int? = null
    private var user: User? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_completed, container, false)


        //Retrieve data from SharedPrefrences
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        val userRole = sharedPreferences.getString("userRole", "")

        //Retrieve data from db
        val db: MySQLiteHelper =
            MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)

        val pickups = if (userRole == "user") {
            db.getPickupsForCustomer(userId, true)
        } else {
            db.getPickupsForRider(userId, "completed")
        }

        if (pickups != null) {

            // the context and arrayList created above
            val adapter = PickupAdapter(requireContext(), ArrayList(pickups))

            // Create the instance of the ListView to set the numbersViewAdapter
            val list = view.findViewById<ListView>(R.id.completed_pickup_list_view)

            // Set the numbersViewAdapter for ListView
            list.adapter = adapter
            list.setOnItemClickListener { _, _, position, _ ->
                // Get the clicked pickup
                val selectedPickup = pickups[position]

                // Create a Bundle to pass data to the new fragment
                val bundle = Bundle().apply {
                    putParcelable(
                        "pickupDetails",
                        selectedPickup
                    ) // Assuming Pickup implements parcelable
                }
                val fragment = PickUpDetails().apply {
                    arguments = bundle
                }
                loadFragment(fragment)

            }
        }

        containerName = if (userRole == "user")
            R.id.frame_container
        else
            R.id.rider_frame_container
        return view

    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(containerName!!, fragment)
        transaction.commit()
    }

}