package com.example.tiktalk.ui

import android.app.Activity
import androidx.appcompat.app.ActionBar
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityHomeBinding
import com.example.tiktalk.databinding.NavHeaderBinding
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel
import com.google.android.material.navigation.NavigationView


class HomeActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityHomeBinding
    private lateinit var viewModel : AuthenticationViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = AuthenticationViewModel()
        viewModel.getState().observe(this@HomeActivity) {
            handleState(it)
        }

        // Get user info to be displayed sa information upon clicking burger menu
        viewModel.getCurrentUserInfo()

        toggle = ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val customTitle = TextView(this@HomeActivity)
        customTitle.text = "Chats"
        customTitle.setTextAppearance(R.style.ActionBarTitleText)

        // Set custom view for the action bar
        supportActionBar?.customView = customTitle
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        val toggleDrawable = toggle.drawerArrowDrawable
        toggleDrawable.colorFilter = PorterDuffColorFilter(
            ContextCompat.getColor(this, R.color.scarlet),
            PorterDuff.Mode.SRC_ATOP
        )

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.navView.setNavigationItemSelectedListener {

                when (it.itemId) {
                    R.id.nav_profile -> UserProfileActivity.launch(this@HomeActivity)

                    R.id.nav_add_friend -> AddFriendActivity.launch(this@HomeActivity)

                    R.id.nav_friend_requests -> FriendRequestsActivity.launch(this@HomeActivity)

                    R.id.nav_settings -> Toast.makeText(
                        this@HomeActivity,
                        "Item 2 clicked",
                        Toast.LENGTH_SHORT
                    ).show()

                    else -> false
                }
                true
        }

        binding.btnLogout.setOnClickListener {
            viewModel.signOut()
        }
    }

    private fun handleState (it : AuthenticationStates) {
        when(it) {
            is AuthenticationStates.Default -> {
                binding.navView.getHeaderView(0).findViewById<TextView>(R.id.tv_username).text = it.user?.name.toString()
            }

            is AuthenticationStates.SignedOut -> {
                LoginActivity.launch(this@HomeActivity)
                finish()
            }

            else -> {}
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

