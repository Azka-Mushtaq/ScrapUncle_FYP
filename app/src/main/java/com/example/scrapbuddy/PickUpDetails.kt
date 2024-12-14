package com.example.scrapbuddy

import MySQLiteHelper
import Pickup
import PickupAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class PickUpDetails : Fragment() {

    private var containerName: Int? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pick_up_details, container, false)
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        val userRole = sharedPreferences.getString("userRole", "")
        containerName = if (userRole!!.trim() == "user") {
            R.id.frame_container
        } else {
            R.id.rider_frame_container
        }

//        // Local db
        val db: MySQLiteHelper =
            MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)
//
//        //Views
        val pickup = arguments?.getParcelable<Pickup>("pickupDetails")

        val pickupId = view.findViewById<TextView>(R.id.pickup_id)
        val pickupDate = view.findViewById<TextView>(R.id.pickup_date)
        val pickupTime = view.findViewById<TextView>(R.id.pickup_time)
        val pickupStatus = view.findViewById<TextView>(R.id.status)
        val pickupTotalWeight = view.findViewById<TextView>(R.id.total_weight)
        val DetailsText = view.findViewById<TextView>(R.id.user_or_scrap_picker_text)
        val customerName = view.findViewById<TextView>(R.id.user_or_scrap_picker_name)
        val customerAddress = view.findViewById<TextView>(R.id.customer_address)
        val goBackBtn = view.findViewById<ImageView>(R.id.pickup_details_back)
        val confirmBtn = view.findViewById<Button>(R.id.confirm_pickup)


//        //set Total Weight
        pickupTotalWeight.setText(pickup?.totalWeight)
        //Customer address
        val pickupAddress = db.getAddress(pickup?.addressId!!)
        customerAddress.setText("${pickupAddress?.street}, ${pickupAddress?.city!!}. (Zipcode: ${pickupAddress?.zipcode!!})")

//        //set Status
        pickupStatus.setText(pickup?.status)
        if (userRole != "user") {
            DetailsText.setText("Customer Details")
            val user = db.getUser(pickup.customerId!!)
            customerName.setText(user?.name!!)


            if (pickup.status == "pending") {
                pickup.status = "confirmed"
                pickup.riderId = userId
                confirmBtn.visibility = View.VISIBLE
                Toast.makeText(requireContext(), pickup.toString(), Toast.LENGTH_LONG).show()
            }

        } else if (pickup?.status == "confirmed") {
            DetailsText.setText("Scrap Picker Details")
            val rider = db.getUser(pickup.riderId!!)
            customerName.setText(rider?.name!!)
        } else {
            DetailsText.setText("Scrap Picker Details")
            customerName.setText("Scrap Picker not Assigned Yet")
        }
//
//        //set time
        pickupTime.setText(pickup?.pickupTime)
//        //Set Id
        pickupId.setText("# ${pickup?.id.toString()}")
//
//        //Set Date
        val inputFormat = SimpleDateFormat("yyyy-mm-dd", Locale.getDefault())
        val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())
//
        val date: Date? = inputFormat.parse(pickup!!.pickupDate)
        val formatedDate = outputFormat.format(date)
        pickupDate.setText(formatedDate)
//
//
//
        confirmBtn.setOnClickListener()
        {
            showCustomDialog(pickup!!, db)

        }
        goBackBtn.setOnClickListener()
        {
            if (userRole == "user")
                loadFragment(PickUpFragment())
            else
                loadFragment(ViewAllPickupsFragment())
        }
        return view
    }

    @SuppressLint("MissingInflatedId")
    fun showCustomDialog(pickup: Pickup, db: MySQLiteHelper) {


        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialogue_box, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.show()

        // Optional: Setup event listeners for dialog components
        val confirmBtn = dialogView.findViewById<Button>(R.id.dialog_confirm_button)
        val cancelBtn = dialogView.findViewById<Button>(R.id.dialog_cancel_button)


        confirmBtn.setOnClickListener {

            GlobalScope.launch {
                try {
                    val response = RetrofitInstance.api.updatePickup(pickup)
                    requireActivity().runOnUiThread {
                        if (response.isSuccessful) {


                            // Insert into db
                            val db: MySQLiteHelper =
                                MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)

                            db.updatePickup(pickup)

                            Toast.makeText(
                                requireContext(),
                                "Confirmed Pickup Successfully",
                                Toast.LENGTH_LONG
                            ).show()

                            loadFragment(ViewAllPickupsFragment())

                        } else {
                            // Handle error response
                            Toast.makeText(
                                requireContext(),
                                "Error: ${response.message()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                } catch (e: Exception) {
                    // Handle exceptions such as network errors
                    requireActivity().runOnUiThread {
                        Toast.makeText(
                            requireContext(),
                            "Server Connection Error: ${e.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }

            dialog.dismiss() // Close the dialog


        }
        cancelBtn.setOnClickListener()
        {
            dialog.dismiss()
        }
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(containerName!!, fragment)
        transaction.commit()
    }
}


