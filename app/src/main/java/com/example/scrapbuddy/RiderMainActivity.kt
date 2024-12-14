package com.example.scrapbuddy

import SyncWorker
import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.concurrent.TimeUnit

class RiderMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_rider_main)

        //Work Manager
        schedulePeriodicSync()


        //Loading default home fragment
        loadFragment((HomeFragment()))



        scrapPickerNavigation()


    }

    //Rider Navigation
    private fun scrapPickerNavigation() {
        // Initialize the BottomNavigationView
        val bottom_nav = findViewById<BottomNavigationView>(R.id.rider_bottom_nav)

        // Set up the BottomNavigationView listener
        bottom_nav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.bottom_nav_home_rider -> {
                    loadFragment(HomeFragment())
                    true
                }

                R.id.bottom_nav_rates_rider -> {
                    loadFragment(RatesFragment())
                    true
                }

                R.id.bottom_nav_in_schedule_rider -> {
                    loadFragment(ViewAllPickupsFragment())
                    true
                }

                R.id.bottom_nav_pickup_rider -> {
                    loadFragment(PickUpFragment())

                    true
                }

                R.id.bottom_nav_profile_rider -> {
                    loadFragment(ProfileFragment())
                    true
                }

                else -> false
            }
        }

    }


    private fun loadFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.rider_frame_container, fragment)
        transaction.commit()
    }

    fun schedulePeriodicSync() {
        val syncWorkRequest = PeriodicWorkRequestBuilder<SyncWorker>(5, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext)
            .enqueueUniquePeriodicWork(
                "SyncWork",
                ExistingPeriodicWorkPolicy.KEEP,
                syncWorkRequest
            )
    }
}