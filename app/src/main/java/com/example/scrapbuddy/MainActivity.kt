package com.example.scrapbuddy

import SyncWorker
import User
import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity

import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        //Work Manager
        schedulePeriodicSync()

        //Loading default home fragment
        loadFragment((HomeFragment()))




        //User navigation
        userNavigation()

    }

    private fun userNavigation() {
        // Initialize the BottomNavigationView
        val bottom_nav = findViewById<BottomNavigationView>(R.id.bottom_nav)

        // Set up the BottomNavigationView listener
        bottom_nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.bottom_nav_rates -> {
                    loadFragment(RatesFragment())
                    true
                }

                R.id.bottom_nav_schedule_pickup -> {
                    loadFragment(SchedulePickUpFragment())
                    true
                }

                R.id.bottom_nav_pickup -> {
                    loadFragment(PickUpFragment())

                    true
                }

                R.id.bottom_nav_profile -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }

    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_container, fragment)
        transaction.commit()
    }

    fun schedulePeriodicSync() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(30, TimeUnit.SECONDS)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "SyncWork",
                ExistingPeriodicWorkPolicy.KEEP,
                syncWorkRequest
            )
    }
}
