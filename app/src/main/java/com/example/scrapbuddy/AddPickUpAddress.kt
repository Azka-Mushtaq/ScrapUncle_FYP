package com.example.scrapbuddy

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class AddPickUpAddress : Fragment() {

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_add_pick_up_address, container, false)


        val fullAddress= view.findViewById<EditText>(R.id.city)  //Full Address entered by user
        val zipcodeOrPincode = view.findViewById<EditText>(R.id.pincode_zipcode)  //ZipCode of the area
        val saveAndProceedButton = view.findViewById<Button>(R.id.save_and_proceed_button)
        val incompletefieldWarning= view.findViewById<TextView>(R.id.incomplete_fields_warning)
        saveAndProceedButton.setOnClickListener()
        {
            val address=fullAddress.text.toString().trim()
            val zipcode =zipcodeOrPincode.text.toString().trim()

            if(address.isEmpty() || zipcode.isEmpty())
                incompletefieldWarning.visibility=View.VISIBLE
            else
            {


                val zpcd=zipcode?.toIntOrNull()
                if(zpcd==null)
                {
                    incompletefieldWarning.setText("Please fill the correct ZipCode/PinCode (5 digit code)")
                    incompletefieldWarning.visibility=View.VISIBLE
                }
                else
                {
                    val bundle = Bundle().apply {
                        putString("address", address)
                        putString("zipcode", zipcode)
                    }

                    // Create an instance of the destination fragment


                }
            }
        }



        return view
    }
    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }
}