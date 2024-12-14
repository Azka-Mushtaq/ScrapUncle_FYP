package com.example.scrapbuddy

import MySQLiteHelper
import User
import android.content.Context
import android.content.Intent
import android.os.Build.USER
import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class SplashScreen : AppCompatActivity() {
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences = getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val userRole = sharedPreferences.getString("userRole", "")
        if(userRole=="")
        {
            android.os.Handler(Looper.getMainLooper()).postDelayed({

                val intent = Intent(this, ShareLocation::class.java)
                startActivity(intent)
            }, 1000)
        }
        else if (userRole == "user") {
            android.os.Handler(Looper.getMainLooper()).postDelayed({

                val intent = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(intent)
            }, 1000)
        } else {
            android.os.Handler(Looper.getMainLooper()).postDelayed({

                val intent = Intent(this@SplashScreen, RiderMainActivity::class.java)
                startActivity(intent)
            }, 1000)
        }
    }
}
