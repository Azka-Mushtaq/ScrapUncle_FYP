package com.example.scrapbuddy

import Address
import CustomAdapter
import MySQLiteHelper
import Pickup
import SavedAddressAdapter
import ScrapTypeAdapter
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.GridLayout
import android.widget.GridView
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ListView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import android.widget.TextView
import android.widget.TimePicker
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class SchedulePickUpFragment : Fragment() {

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("MissingInflatedId", "ResourceAsColor")
    private lateinit var address: Address

    private lateinit var scrapButton: LinearLayout
    private lateinit var selectedScrap: LinearLayout
    private lateinit var pickupButton: LinearLayout
    private lateinit var pickupView: LinearLayout
    private lateinit var scheduleBtn: Button
    private lateinit var warningtext: TextView

    private lateinit var estimatedWeightTick: ImageView
    private lateinit var selectedDateTick: ImageView
    private lateinit var selectedTimeTick: ImageView
    private lateinit var pickupAddressTick: ImageView

    private lateinit var estimatedWeightView: LinearLayout
    private lateinit var estimatedWeight: LinearLayout
    private lateinit var addAddressButton: Button
    private lateinit var selectedDateButton: LinearLayout
    private lateinit var selectedDate: DatePicker


    //View of estimated text color:
    private lateinit var estimatedWeightText: TextView
    private lateinit var selectedDateText: TextView
    private lateinit var selectedTimeText: TextView
    private lateinit var selectedAddressText: TextView

    private lateinit var radioGroup: RadioGroup

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("ResourceAsColor")

    //Toggle the visibility of any view
    private fun toggleVisibility(toggleItem: View) {
        if (toggleItem.visibility == View.GONE)

            toggleItem.visibility = View.VISIBLE
        else
            toggleItem.visibility = View.GONE

    }

    @SuppressLint("ResourceAsColor", "MissingInflatedId")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_schedule_pick_up, container, false)


        //selected scrap item type
        scrapButton = view.findViewById(R.id.selected_scrap_item_arrow)
        selectedScrap = view.findViewById(R.id.selected_scrap)
        val selectedScrapItemTick = view.findViewById<ImageView>(R.id.selected_scrap_item_tick)
        val selectedScrapItemText = view.findViewById<TextView>(R.id.selected_scrap_item_text)


        //Toggle visibility on button click
        scrapButton.setOnClickListener {
            toggleVisibility(selectedScrap)

        }


        //pickup address
        pickupButton = view.findViewById(R.id.pickup_address_arrow)
        pickupView = view.findViewById(R.id.pickup_address_layout)
        selectedAddressText = view.findViewById(R.id.pickup_address_text)
        pickupAddressTick = view.findViewById(R.id.pickup_address_tick)

        pickupButton.setOnClickListener {
            toggleVisibility(pickupView)
        }
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)

        val db: MySQLiteHelper = MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)
        val addresses = db.getAddressesForUser(userId)

        if (addresses.isNotEmpty()) {
            // Toast.makeText(requireContext(), pickups.toString(), Toast.LENGTH_LONG).show()
            // the context and arrayList created above
            val adapter = SavedAddressAdapter(requireContext(), ArrayList(addresses))

            // Create the instance of the ListView to set the numbersViewAdapter
            val list = view.findViewById<ListView>(R.id.show_addresses_list_view)

            // Set the numbersViewAdapter for ListView
            list.adapter = adapter

            // Set the OnItemClickListener
            list.setOnItemClickListener { parent, view, position, id ->
                // Get the clicked item from the adapter
                address = adapter.getItem(position)!!
                pickupAddressTick.visibility = View.VISIBLE
                pickupView.visibility = View.GONE
                val addr = address.street + ", " + address.city
                if (addr.isNotEmpty())
                    selectedAddressText.text = addr
                // You can also perform other actions with the clicked item here
            }
        }
        // Add & display pickup address dialogue box
        // addAddressButton = view.findViewById(R.id.add_address)
        //addAddressButton.setOnClickListener()
        //{
        //  showCustomDialog(view)
        //}


        //Selected Date
        selectedDateButton = view.findViewById(R.id.selected_date_arrow)
        selectedDate = view.findViewById(R.id.selected_date)

        //Display hidden view on click and vice versa
        selectedDateButton.setOnClickListener {
            toggleVisibility(selectedDate)

        }

        //display the date selected by user and mark it as done(show tick)
        selectedDateTick = view.findViewById<ImageView>(R.id.selected_date_tick)
        val selectedDateText = view.findViewById<TextView>(R.id.selected_date_text)
        // Format the date
        var date: String = ""
        selectedDate.setOnDateChangedListener { view, year, monthOfYear, dayOfMonth ->
            val date1 = "$year-$monthOfYear-$dayOfMonth"

            var month: String?
            var day: String?
            if (monthOfYear < 9)
                month = "0" + monthOfYear.toString()
            else
                month = monthOfYear.toString()

            if (dayOfMonth < 9)
                day = "0" + dayOfMonth.toString()
            else
                day = dayOfMonth.toString()


            val formatedDate = "$year-$month-$day"
            //createdAtDate.text = formatedDate
            selectedDateText.setText(formatedDate)
            date = formatedDate
            selectedDateTick.visibility = View.VISIBLE
            selectedDate.visibility = View.GONE
        }


        //Selected time
        val selectedTimeButton = view.findViewById<LinearLayout>(R.id.selected_time_arrow)
        val selectedTime = view.findViewById<TimePicker>(R.id.time_selected)
        selectedTimeButton.setOnClickListener {
            toggleVisibility(selectedTime)
        }

        var time: String = ""
        //Selected Time by user
        selectedTimeTick = view.findViewById<ImageView>(R.id.selected_time_tick)
        selectedTimeText = view.findViewById<TextView>(R.id.selected_time_text)
        selectedTime.setOnTimeChangedListener { view, hour, minute ->

            // Format hours and minutes
            var hourString: String?
            var minuteString: String?
            if (hour < 9)
                hourString = "0" + hour.toString()
            else
                hourString = hour.toString()

            if (minute < 9)
                minuteString = "0" + minute.toString()
            else
                minuteString = minute.toString()


            val secondString = "00"  // Assuming seconds are always 00 for simplicity

            // Construct the formatted time string
            val formattedTime = "$hourString:$minuteString:$secondString"
            selectedTimeText.setText(formattedTime)
            time = formattedTime

            selectedTimeTick.visibility = View.VISIBLE
            selectedTime.visibility = View.GONE
        }


        //Estimated Weight View
        estimatedWeightView = view.findViewById(R.id.estimated_weight_arrow)
        estimatedWeight = view.findViewById(R.id.estimated_weight)
        estimatedWeightView.setOnClickListener {
            toggleVisibility(estimatedWeight)

        }


        //Radio buttons for Estimated weight
        estimatedWeightTick = view.findViewById(R.id.estimated_weight_tick)

        var weight: String = ""

        //View of estimated text color:
        estimatedWeightText = view.findViewById(R.id.estimated_weight_text)
        radioGroup = view.findViewById<RadioGroup>(R.id.radiogroup)
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Loop through all RadioButtons in the RadioGroup
            for (i in 0 until radioGroup.childCount) {
                val rb = radioGroup.getChildAt(i) as RadioButton
                // If the current RadioButton is the selected one, change its background to green
                if (rb.id == checkedId) {
                    rb.setBackgroundResource(R.drawable.radio_button_selected)
                    rb.setTextColor(R.color.white)
                    estimatedWeightText.setText(rb.text as String)
                    estimatedWeightTick.visibility = View.VISIBLE

                    weight = rb.text as String

                    estimatedWeight.visibility = View.GONE

                } else {
                    // Otherwise, change the background to white
                    rb.setBackgroundResource(R.drawable.radio_button_not_selected)
                }
            }
        }


        //Button Schedule pickup
        scheduleBtn = view.findViewById<Button>(R.id.SchedulePickupBtn)
        warningtext = view.findViewById<TextView>(R.id.incomplete_fields_warning)
        scheduleBtn.setOnClickListener()
        {
            if (checkVisibility()) {

                val pickup: Pickup =
                    Pickup(0, userId, null, address.id, date, time, "pending", weight)
                GlobalScope.launch {
                    try {
                        val response = RetrofitInstance.api.postPickup(pickup)
                        requireActivity().runOnUiThread {
                            if (response.isSuccessful) {
                                val pickupId = response.body()
                                pickup.id = pickupId!!

                                // Insert into db
                                val db: MySQLiteHelper =
                                    MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)

                                db.insertPickup(pickup)

                                Toast.makeText(
                                    requireContext(),
                                    "Pickup Added Successfully",
                                    Toast.LENGTH_LONG
                                ).show()

                                //loadFragment(SavedAdressesFragment())

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
            }
        }


        return view
    }

    //Check that if all ticks are visible
    private fun checkVisibility(): Boolean {
        if (estimatedWeightTick.visibility == View.VISIBLE && selectedDateTick.visibility == View.VISIBLE
            && selectedTimeTick.visibility == View.VISIBLE && pickupAddressTick.visibility == View.VISIBLE
        ) {
            warningtext.visibility = View.GONE // Hide the warning text if fields are complete
            return true
        } else {
            warningtext.visibility =
                View.VISIBLE // Show the warning text if fields are incomplete
            return false
        }

    }


    //Custom Dialogue box for taking address input
    /*  fun showCustomDialog(view: View) {

             var triple = Triple<String, Int, Boolean>("address", 0, false)

          val dialogView = LayoutInflater.from(requireContext())
              .inflate(R.layout.fragment_add_pick_up_address, null)
          val dialogBuilder = AlertDialog.Builder(requireContext())
              .setView(dialogView)

          val dialog = dialogBuilder.create()
          dialog.show()

          // Optional: Setup event listeners for dialog components
          val saveAndProceedButton = dialogView.findViewById<Button>(R.id.save_and_proceed_button)
          val fullAddressEditText = dialogView.findViewById<EditText>(R.id.city)
          val pincodeZipcodeEditText = dialogView.findViewById<EditText>(R.id.pincode_zipcode)
          val incompleteFieldWarning =

              dialogView.findViewById<TextView>(R.id.incomplete_fields_warning)
          val addressDisplay = view.findViewById<TextView>(R.id.full_address_text_display)
          val zipcodeDisplay = view.findViewById<TextView>(R.id.pincode_text_display)
          val noAddressAddedView = view.findViewById<TextView>(R.id.no_address_added)
          val showAddedAddresses = view.findViewById<LinearLayout>(R.id.show_added_address)
          pickupAddressTick = view.findViewById<ImageView>(R.id.pickup_address_tick)
          val pickupView = view.findViewById<LinearLayout>(R.id.pickup_address_layout)
          val addAddress = view.findViewById<Button>(R.id.add_address)
          saveAndProceedButton.setOnClickListener {

              // Find dialog components

              val address = fullAddressEditText.text.toString().trim()
              val zipcode = pincodeZipcodeEditText.text.toString().trim()

              if (address.isEmpty() || zipcode.isEmpty()) {
                  incompleteFieldWarning.visibility = View.VISIBLE

              } else {


                  val zpcd = zipcode?.toIntOrNull()
                  if (zpcd == null) {
                      incompleteFieldWarning.setText("Please fill the correct ZipCode/PinCode (5 digit code)")
                      incompleteFieldWarning.visibility = View.VISIBLE

                  } else {

                      // Create an instance of the destination fragment
                      //Log.i("TAG", "In The Show CustomDialog...")

                      //Log.i("TAG", "$triple")

                      addressDisplay.setText(address)
                      zipcodeDisplay.setText(zipcode)

                      dialog.dismiss() // Close the dialog
                      noAddressAddedView.visibility = View.GONE
                      showAddedAddresses.visibility = View.VISIBLE
                      pickupAddressTick.visibility = View.VISIBLE
                      pickupView.visibility = View.GONE
                      addAddress.visibility = View.GONE
                  }
              }
          }
      }*/

}