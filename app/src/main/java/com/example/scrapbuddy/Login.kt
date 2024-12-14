package com.example.scrapbuddy

import Address
import LoginRequest
import MySQLiteHelper
import Pickup
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class Login : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        val emailEditText = findViewById<EditText>(R.id.login_email_edit_text)
        val passwordEditText = findViewById<EditText>(R.id.login_password_edit_text)
        val warning = findViewById<TextView>(R.id.signup_warning)
        val signupBtn = findViewById<TextView>(R.id.Signup_text_view)
        val loginBtn = findViewById<Button>(R.id.login_button)

        //Don't have account so move to sign up page
        signupBtn.setOnClickListener()
        {
            val intent = Intent(this, SignUp::class.java)
            startActivity(intent)
        }
        //Submit or Sign Up button
        loginBtn.setOnClickListener()
        {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                warning.visibility = View.VISIBLE
            } else {

                val loginCredentials = LoginRequest(email, password)
                GlobalScope.launch {
                    try {
                        val response = RetrofitInstance.api.authenticateUser(loginCredentials)
                        runOnUiThread {
                            if (response.isSuccessful) {
                                val user = response.body()

                                val db: MySQLiteHelper =
                                    MySQLiteHelper(this@Login, "ScrapBuddy", null, 1)

                                //Insert into db
                                val tempUser = db.getUser(user!!.id)
                                if (tempUser == null)
                                    db.insertUser(user!!)
                                else
                                    db.updateUser(user!!)

                                if (user?.addresses != null)
                                    SyncAddresses(user.addresses)

                                if (user?.pickups != null)
                                    SyncPickups(user.pickups)



                                Toast.makeText(
                                    this@Login,
                                    "Login Successfull}",
                                    Toast.LENGTH_LONG
                                ).show()

                                //shared preferences to store user id:
                                val sharedPreferences =
                                    getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putInt("userId", user!!.id)
                                editor.putString("userName", user.name)
                                editor.putString("userEmail", user.email)
                                editor.putString("userPhoneNumber", user.phoneNumber)
                                editor.putString("userCreatedAt", user.createdAt)
                                editor.putString("userRole", user.role)
                                editor.apply()


                                if (user.role == "user") {
                                    val intent = Intent(this@Login, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(this@Login, RiderMainActivity::class.java)
                                    startActivity(intent)
                                }


                            } else {
                                // Handle error response
                                Toast.makeText(
                                    this@Login,
                                    "Incorrect Credentials",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    } catch (e: Exception) {
                        runOnUiThread {
                            // Handle exceptions such as network errors
                            Toast.makeText(
                                this@Login,
                                "Server Connection error: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }
            }
        }
    }

    private fun SyncAddresses(addresses: List<Address>?) {
        val db = MySQLiteHelper(this, "ScrapBuddy", null, 1)
        if (addresses != null) {
            for (address in addresses) {
                val response = db.getAddress(address.id!!)
                if (response == null)
                    db.insertAddress(address)
                else
                    db.updateAddress(address)
            }
        }

    }

    private fun SyncPickups(pickups: List<Pickup>?) {
        val db = MySQLiteHelper(this, "ScrapBuddy", null, 1)
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

}