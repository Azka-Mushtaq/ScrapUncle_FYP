package com.example.scrapbuddy

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.scrapbuddy.databinding.ActivityPhNoRegistrationBinding

class GetStarted : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_get_started)
        val getOtpBtn = findViewById<Button>(R.id.GetStartedBtn)
        getOtpBtn.setOnClickListener()
        {
            val intent = Intent(this,PhNoRegistration::class.java)
            startActivity(intent)
        }
    }
}