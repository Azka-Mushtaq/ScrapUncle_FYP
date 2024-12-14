package com.example.scrapbuddy

import MySQLiteHelper
import User
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
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.materialswitch.MaterialSwitch
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_up)
        val db: MySQLiteHelper = MySQLiteHelper(this, "ScrapBuddy", null, 1)

        val nameEditText = findViewById<EditText>(R.id.nameEditText)
        val emailEditText = findViewById<EditText>(R.id.emailEditText)
        val phoneEditText = findViewById<EditText>(R.id.phoneNumberEditText)
        val passwordEditText = findViewById<EditText>(R.id.passwordEditText)
        val warning = findViewById<TextView>(R.id.signup_warning)
        val loginBtn = findViewById<TextView>(R.id.loginTextView)

        val signUpBtn = findViewById<Button>(R.id.signUpButton)
        // Find the Spinner
        val spinnerRole = findViewById<Spinner>(R.id.userOrRider)

        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.role_array,
            android.R.layout.simple_spinner_item
        )

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        spinnerRole.adapter = adapter

        //if user already has an account than login
        loginBtn.setOnClickListener()
        {
            startActivity(Intent(this,Login::class.java))
        }

        //Submit or Sign Up button
        signUpBtn.setOnClickListener()
        {
            val name = nameEditText.text.toString()
            val email = emailEditText.text.toString()
            val phone = phoneEditText.text.toString()
            val password = passwordEditText.text.toString()
            val selectedItem = spinnerRole.selectedItem.toString()
            val role = selectedItem.lowercase()

            if (name.isEmpty() || email.isEmpty() || phone.isEmpty() || password.isEmpty()) {
                warning.visibility = View.VISIBLE
            } else {
                val user: User = User(0, name, email, password, phone, role)
                //Toast.makeText(this@SignUp, user.toString(), Toast.LENGTH_LONG).show()


                GlobalScope.launch {
                    try {
                        val response = RetrofitInstance.api.postUser(user)
                        runOnUiThread {
                            if (response.isSuccessful) {
                                val userId = response.body()
                                user.id = userId!!

                                //Insert into db
                                db.insertUser(user)

                                Toast.makeText(
                                    this@SignUp,
                                    "Sign Up Successfull}",
                                    Toast.LENGTH_LONG
                                ).show()

                                //shared preferences to store user id:
                                val sharedPreferences =
                                    getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
                                val editor = sharedPreferences.edit()
                                editor.putInt("userId", userId)
                                editor.putString("userName", user.name)
                                editor.putString("userEmail", user.email)
                                editor.putString("userPhoneNumber", user.phoneNumber)
                                editor.putString("userCreatedAt", user.createdAt)
                                editor.putString("userRole", user.role)

                                editor.apply()

                                if (user.role == "user") {
                                    val intent = Intent(this@SignUp, MainActivity::class.java)
                                    startActivity(intent)
                                } else {
                                    val intent = Intent(this@SignUp, RiderMainActivity::class.java)
                                    startActivity(intent)
                                }


                            } else {
                                // Handle error response
                                Toast.makeText(
                                    this@SignUp,
                                    "Error: ${response.message()}",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                        }

                    } catch (e: Exception) {
                        runOnUiThread {
                            // Handle exceptions such as network errors
                            Toast.makeText(
                                this@SignUp,
                                "Server Connection error: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }
                }


            }

        }

    }
}