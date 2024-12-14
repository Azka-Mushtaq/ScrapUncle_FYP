package com.example.scrapbuddy

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import androidx.core.app.ActivityCompat

class ShareLocation : AppCompatActivity() {

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_share_location)

        val continueBtn = findViewById<AppCompatButton>(R.id.continue_btn)
        continueBtn.setOnClickListener()
        {
            if(checkPermission())
            {
                permissionGranted()
            }
            else
            {
                takePermission()
            }

        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_LONG).show()
                permissionGranted()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_LONG).show()
                takePermission()
            }
        }
    }

    private fun checkPermission():Boolean {
        //if Permission granted or not granted
        val result1 = ActivityCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)

        return result1 == PackageManager.PERMISSION_GRANTED

    }
    private fun takePermission(){
        ActivityCompat.requestPermissions(this, arrayOf(ACCESS_FINE_LOCATION,
            ACCESS_COARSE_LOCATION),100)
    }
    private fun permissionGranted()
    {
        val intent = Intent(this,Login::class.java)
        startActivity(intent)
    }
}