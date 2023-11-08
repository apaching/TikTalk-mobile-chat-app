package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityHomeBinding


class HomeActivity : AppCompatActivity() {

    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)


        toggle = ActionBarDrawerToggle(this@HomeActivity, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)



        binding.navView.setNavigationItemSelectedListener {

                when (it.itemId) {
                    R.id.nav_profile -> Toast.makeText(
                        this@HomeActivity,
                        "Item 1 clicked",
                        Toast.LENGTH_SHORT
                    ).show()

                    R.id.nav_add_friend -> Toast.makeText(
                        this@HomeActivity,
                        "Item 2 clicked",
                        Toast.LENGTH_SHORT
                    ).show()

                    R.id.nav_qr_code -> Toast.makeText(
                        this@HomeActivity,
                        "Item 3 clicked",
                        Toast.LENGTH_SHORT
                    ).show()

                    R.id.nav_settings -> Toast.makeText(
                        this@HomeActivity,
                        "Item 2 clicked",
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> false
                }

                true

            }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if(toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        fun launch(activity : Activity) {
            activity.startActivity(Intent(activity, HomeActivity::class.java))
        }
    }
}