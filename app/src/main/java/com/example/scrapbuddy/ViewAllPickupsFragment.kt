package com.example.scrapbuddy

import MySQLiteHelper
import Pickup
import PickupAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class ViewAllPickupsFragment : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_view_all_pickups, container, false)

        //Load Server Pickups
        loadServerPickups()


        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)

        // Schedule periodic sync
        //val syncScheduler = PickupSyncScheduler(requireContext())
        //syncScheduler.schedulePeriodicSync()

        //Retrieve data from db
        val db: MySQLiteHelper =
            MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)


        val pickups = db.getAllPickups()


        if (pickups != null) {

            // the context and arrayList created above
            val adapter = PickupAdapter(requireContext(), ArrayList(pickups))

            // Create the instance of the ListView to set the numbersViewAdapter
            val list = view.findViewById<ListView>(R.id.new_pickup_list_view)

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
        return view
    }

    private fun SyncPickups(pickups: List<Pickup>?) {
        val db = MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)
        if (pickups != null) {
            for (pickup in pickups) {
                val response = db.getPickupById(pickup.id!!)
                if (response == null)
                    db.insertPickup(pickup)
                else
                    db.updatePickup(pickup)
            }
        }
    }

    private fun loadServerPickups()
    {
        GlobalScope.launch {
            try {
                val response = RetrofitInstance.api.getAllPickups()
                requireActivity().runOnUiThread {
                    if (response.isSuccessful) {
                        val pickups = response.body()
                        if (pickups != null) {
                            val db = MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)

                            SyncPickups(pickups)

                            Log.d("View All Fragment:", "Pickups successfully synced.")
                        }
                    } else {
                        Log.d("View All Fragment:", "Server response was not successful.")
                    }
                }

            } catch (e: Exception) {
                Log.e("View All Fragment:", "Exception during sync: ${e.message}", e)
            }
        }
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.rider_frame_container, fragment)
        transaction.commit()
    }
}