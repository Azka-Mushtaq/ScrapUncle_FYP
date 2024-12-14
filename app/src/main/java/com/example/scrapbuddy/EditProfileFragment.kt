package com.example.scrapbuddy

import MySQLiteHelper
import User
import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class EditProfileFragment : Fragment() {

    private var containerName: Int? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        //Input fields
        val nameEditText = view.findViewById<EditText>(R.id.edit_name_edit_text)
        val emailEditText = view.findViewById<EditText>(R.id.edit_email_edit_text)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.edit_phone_edit_text)

        //warning field
        val warning = view.findViewById<TextView>(R.id.edit_profile_warning)

        //Button to save changes
        val saveChangesBtn = view.findViewById<Button>(R.id.save_changes_btn)


        //Retrieve data from Shared Preferences
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        val userName = sharedPreferences.getString("userName", "")
        val userEmail = sharedPreferences.getString("userEmail", "")
        val userPhone = sharedPreferences.getString("userPhoneNumber", "")
        val userRole = sharedPreferences.getString("userRole", "")

        //Show already existing details in input fields
        nameEditText.setText(userName)
        emailEditText.setText(userEmail)
        phoneNumberEditText.setText(userPhone)

        //If user want to save changes
        saveChangesBtn.setOnClickListener() {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneNumberEditText.text.toString()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                warning.visibility = View.VISIBLE
            } else {
                if (name == userName && email == userEmail && phone == userPhone) {
                    Toast.makeText(requireContext(), "No new changes are made", Toast.LENGTH_LONG)
                        .show()
                } else {

                    val user: User = User(userId, name, email, null, phone, null)
                    GlobalScope.launch {
                        try {
                            val response = RetrofitInstance.api.updateUser(user)
                            requireActivity().runOnUiThread {
                                if (response.isSuccessful) {

                                    // Insert into db
                                    val db: MySQLiteHelper =
                                        MySQLiteHelper(requireContext(), "ScrapBuddy", null, 1)

                                    db.updateUser(user)

                                    //Show toast to user
                                    Toast.makeText(
                                        requireContext(),
                                        "Details Updated Successfully",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    //Update Preferences
                                    val editor = sharedPreferences.edit()
                                    editor.putString("userName", name)
                                    editor.putString("userEmail", email)
                                    editor.putString("userPhoneNumber", phone)
                                    editor.apply()

                                    loadFragment(EditProfileFragment())

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
        }

        containerName = if (userRole == "user")
            R.id.frame_container
        else
            R.id.rider_frame_container

        //Return back
        val returnBack = view.findViewById<ImageButton>(R.id.edit_profile_back)
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
}