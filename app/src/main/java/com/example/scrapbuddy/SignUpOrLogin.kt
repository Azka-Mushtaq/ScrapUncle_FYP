package com.example.scrapbuddy

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class SignUpOrLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_or_login)

        val email = findViewById<CardView>(R.id.CardViewforEmail)
        email.setOnClickListener()
        {
            val intent = Intent(this,Login::class.java)
            startActivity(intent)
        }

    }
}