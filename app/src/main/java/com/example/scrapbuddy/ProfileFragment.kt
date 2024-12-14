package com.example.scrapbuddy

import User
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.properties.Delegates


class ProfileFragment : Fragment() {


    private var containerName: Int? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        //Retrieve data from SharedPrefrences
        val sharedPreferences =
            requireContext().getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userId = sharedPreferences.getInt("userId", -1)
        val userName = sharedPreferences.getString("userName", "")
        val userRole = sharedPreferences.getString("userRole", "")


        val profileView = view.findViewById<LinearLayout>(R.id.profile)
        val logoutBtn = view.findViewById<LinearLayout>(R.id.logout)

        val shareView = view.findViewById<LinearLayout>(R.id.share)

        val aboutUsView = view.findViewById<LinearLayout>(R.id.about_us)
        val addressView = view.findViewById<LinearLayout>(R.id.my_addresses)
        val username = view.findViewById<TextView>(R.id.username)
        val createdAtDate = view.findViewById<TextView>(R.id.user_created_at)

        //Switch User
        val loginAsOther = view.findViewById<Button>(R.id.switch_login_btn)
        //Toast.makeText(requireContext(),userRole,Toast.LENGTH_LONG).show()
        if (userRole!!.trim() == "user") {
            containerName = R.id.frame_container
            loginAsOther.setText(R.string.login_as_scrap_picker)
        } else {
            containerName = R.id.rider_frame_container
            loginAsOther.setText(R.string.login_as_user)
        }


        //val userCreatedAt = sharedPreferences.getString("userCreatedAt", "")

        if (userId != -1) {
            username.text = userName

            //val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            //val outputFormat = SimpleDateFormat("MMMM dd, yyyy", Locale.getDefault())

            //val date: Date? = inputFormat.parse(userCreatedAt)
            //val formatedDate =outputFormat.format(date)
            //createdAtDate.text = formatedDate
        }
        // val editBtn = view.findViewById<Button>(R.id.editbtn)
        aboutUsView.setOnClickListener()
        {
            loadFragment(SupportFragment())
        }
        profileView.setOnClickListener()
        {
            loadFragment(EditProfileFragment())
        }
        addressView.setOnClickListener() {
            loadFragment(SavedAdressesFragment())
        }
        shareView.setOnClickListener() {
            loadFragment(ReferAFriendFragment())
        }
        logoutBtn.setOnClickListener() {
            // Get a reference to SharedPreferences

            // Get an editor to make changes
            val editor = sharedPreferences.edit()

            // Clear all data
            editor.clear()

            // Apply changes (or use commit() if you need synchronous operation)
            editor.apply()
            val intent = Intent(requireContext(), Login::class.java)
            startActivity(intent)
        }
        return view
    }

    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(containerName!!, fragment)
        transaction.commit()
    }
}