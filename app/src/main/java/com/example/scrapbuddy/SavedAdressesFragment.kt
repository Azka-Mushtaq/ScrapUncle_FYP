package com.example.scrapbuddy

import Address
import MySQLiteHelper
import RetrofitInstance
import SavedAddressAdapter
import User
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class SavedAdressesFragment : Fragment() {

    private var containerName: Int? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_saved_adresses, container, false)

        val db: MySQLiteHelper = MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)

        //Retrieve data from Shared Preferences
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        val userRole = sharedPreferences.getString("userRole", "")

        containerName = if (userRole == "user")
            R.id.frame_container
        else
            R.id.rider_frame_container
        val addresses = db.getAddressesForUser(userId)

        if (addresses.isNotEmpty()) {

            // Toast.makeText(requireContext(), pickups.toString(), Toast.LENGTH_LONG).show()
            // the context and arrayList created above
            val adapter = SavedAddressAdapter(requireContext(), ArrayList(addresses))

            // Create the instance of the ListView to set the numbersViewAdapter
            val list = view.findViewById<ListView>(R.id.saved_addresses_list_view)

            // Set the numbersViewAdapter for ListView
            list.adapter = adapter


            list.setOnItemClickListener { parent, view, position, id ->
                // Get the selected address
                val selectedAddress = addresses[position]

                // Get the ID of the selected address
                val addressId = selectedAddress.id

                val deleteAddress = view.findViewById<ImageView>(R.id.address_delete)
                val updateButton = view.findViewById<ImageView>(R.id.address_edit)

                deleteAddress.setOnClickListener()
                {
                    DeleteConfirmation(addressId)
                }
                updateButton.setOnClickListener()
                {
                 updateAddress(userId,addressId)
                }
            }
        }

        //Add new Address
        val addAddress = view.findViewById<ImageView>(R.id.add_new_address)
        addAddress.setOnClickListener() {

            showCustomDialog(view, userId)
        }

        // Return back to previous page
        val returnBack = view.findViewById<ImageButton>(R.id.saved_address_back)
        returnBack.setOnClickListener()
        {
            loadFragment(ProfileFragment())
        }

        return view
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(containerName!!, fragment)
        transaction.commit()
    }

    @SuppressLint("MissingInflatedId")
    fun updateAddress(userId: Int, addressId: Int) {


        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_add_pick_up_address, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.show()

        // Optional: Setup event listeners for dialog components
        val saveAndProceedButton = dialogView.findViewById<Button>(R.id.save_and_proceed_button)
        val streetEditText = dialogView.findViewById<EditText>(R.id.street)
        val cityEditText = dialogView.findViewById<EditText>(R.id.city)
        val pincodeZipcodeEditText = dialogView.findViewById<EditText>(R.id.pincode_zipcode)
        val incompleteFieldWarning =
            dialogView.findViewById<TextView>(R.id.incomplete_fields_warning)
        val addressTitle= dialogView.findViewById<TextView>(R.id.address_dialog_title)
        addressTitle.text="Update Pickup Address"

        saveAndProceedButton.setOnClickListener {

            // Find dialog components

            val street = streetEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val zipcode = pincodeZipcodeEditText.text.toString().trim()

            if (street.isEmpty() || city.isEmpty() || zipcode.isEmpty()) {
                incompleteFieldWarning.visibility = View.VISIBLE

            } else {


                val zpcd = zipcode?.toIntOrNull()
                if (zpcd == null) {
                    incompleteFieldWarning.setText("Please fill the correct ZipCode/PinCode (5 digit code)")
                    incompleteFieldWarning.visibility = View.VISIBLE

                } else {

                    var address: Address = Address(addressId, userId, street, city, zipcode.toString())

                    GlobalScope.launch {
                        try {
                            val response = RetrofitInstance.api.updateAddress(address)
                            requireActivity().runOnUiThread {
                                if (response.isSuccessful) {

                                    // Insert into db
                                    val db: MySQLiteHelper =
                                        MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)

                                    db.updateAddress(address)
                                    Toast.makeText(
                                        requireContext(),
                                        "Address Updated Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    loadFragment(SavedAdressesFragment())

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
            }
        }
    }

    fun DeleteConfirmation(addressId:Int) {


        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.alert_dialogue_box, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.show()

        val message=dialogView.findViewById<TextView>(R.id.dialog_message)
        val title = dialogView.findViewById<TextView>(R.id.dialog_title)
        message.text = "Are you sure you want to delete address?"
        title.text="Delete Address"
        // Optional: Setup event listeners for dialog components
        val confirmBtn = dialogView.findViewById<Button>(R.id.dialog_confirm_button)
        confirmBtn.setBackgroundResource(R.drawable.delete_button)
        confirmBtn.text = "DELETE"

        val cancelBtn = dialogView.findViewById<Button>(R.id.dialog_cancel_button)


        confirmBtn.setOnClickListener {

            GlobalScope.launch {
                try {
                    val response = RetrofitInstance.api.deleteAddress(addressId)
                    requireActivity().runOnUiThread()
                    {
                        if (response.isSuccessful) {
                            // Insert into db
                            val db: MySQLiteHelper =
                                MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)

                            db.deleteAddress(addressId)

                            Toast.makeText(
                                requireContext(),
                                "Address deleted Successfully",
                                Toast.LENGTH_LONG
                            ).show()
                            loadFragment(SavedAdressesFragment())
                        }
                        else
                        {
                            Toast.makeText(
                                requireContext(),
                                "Error occurred",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                } catch (ex: Exception) {
                    requireActivity().runOnUiThread()
                    {
                        Toast.makeText(
                            requireContext(),
                            "Error: ${ex.message}",
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

    @SuppressLint("MissingInflatedId")
    fun showCustomDialog(view: View, userId: Int) {


        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.fragment_add_pick_up_address, null)
        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)

        val dialog = dialogBuilder.create()
        dialog.show()

        // Optional: Setup event listeners for dialog components
        val saveAndProceedButton = dialogView.findViewById<Button>(R.id.save_and_proceed_button)
        val streetEditText = dialogView.findViewById<EditText>(R.id.street)
        val cityEditText = dialogView.findViewById<EditText>(R.id.city)
        val pincodeZipcodeEditText = dialogView.findViewById<EditText>(R.id.pincode_zipcode)
        val incompleteFieldWarning =
            dialogView.findViewById<TextView>(R.id.incomplete_fields_warning)


        saveAndProceedButton.setOnClickListener {

            // Find dialog components

            val street = streetEditText.text.toString().trim()
            val city = cityEditText.text.toString().trim()
            val zipcode = pincodeZipcodeEditText.text.toString().trim()

            if (street.isEmpty() || city.isEmpty() || zipcode.isEmpty()) {
                incompleteFieldWarning.visibility = View.VISIBLE

            } else {


                val zpcd = zipcode?.toIntOrNull()
                if (zpcd == null) {
                    incompleteFieldWarning.setText("Please fill the correct ZipCode/PinCode (5 digit code)")
                    incompleteFieldWarning.visibility = View.VISIBLE

                } else {

                    var address: Address = Address(0, userId, street, city, zipcode.toString())

                    GlobalScope.launch {
                        try {
                            val response = RetrofitInstance.api.postAddress(address)
                            requireActivity().runOnUiThread {
                                if (response.isSuccessful) {
                                    val addressId = response.body()
                                    address.id = addressId!!

                                    // Insert into db
                                    val db: MySQLiteHelper =
                                        MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)

                                    db.insertAddress(address)
                                    Toast.makeText(
                                        requireContext(),
                                        "Address Added Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    loadFragment(SavedAdressesFragment())

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
            }
        }
    }


}