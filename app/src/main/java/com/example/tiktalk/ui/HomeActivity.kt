package com.example.tiktalk.ui

import android.app.Activity
import androidx.appcompat.app.ActionBar
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiktalk.R
import com.example.tiktalk.state.ChatStates
import com.example.tiktalk.adapter.RecentChatsAdapter
import com.example.tiktalk.databinding.ActivityHomeBinding
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel
import com.example.tiktalk.viewmodel.ChatViewModel
import com.google.android.material.floatingactionbutton.FloatingActionButton


class HomeActivity : AppCompatActivity() {

    private lateinit var toggle : ActionBarDrawerToggle
    private lateinit var binding : ActivityHomeBinding

    private lateinit var authenticationViewModel : AuthenticationViewModel
    private lateinit var chatViewModel : ChatViewModel

    private lateinit var adapter : RecentChatsAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        authenticationViewModel = AuthenticationViewModel()
        authenticationViewModel.getState().observe(this@HomeActivity) {
            handleState(it)
        }
        // Get user info to be displayed sa information upon clicking burger menu
        authenticationViewModel.getCurrentUserInfo()

        chatViewModel = ChatViewModel()
        chatViewModel.getState().observe(this@HomeActivity) {
            handleState(it)
        }
        chatViewModel.retrieveRecentChats()

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

                    R.id.nav_friendList -> UserFriendsListActivity.launch(this@HomeActivity)

                    R.id.nav_add_friend -> AddFriendActivity.launch(this@HomeActivity)

                    R.id.nav_friend_requests -> FriendRequestsActivity.launch(this@HomeActivity)

                    R.id.nav_settings -> SettingsActivity.launch(this@HomeActivity)


                    else -> false
                }
                true
        }

        binding.btnLogout.setOnClickListener {
            authenticationViewModel.signOut()
        }

        val fab : FloatingActionButton = findViewById(R.id.fab_to_friends_list)
        val drawable = ContextCompat.getDrawable(this, R.drawable.edit_square_24px)
        drawable?.mutate()?.setColorFilter(ContextCompat.getColor(this, R.color.beige), PorterDuff.Mode.SRC_ATOP);
        fab.setImageDrawable(drawable);

        binding.fabToFriendsList.setOnClickListener {
            UserFriendsListActivity.launch(this@HomeActivity)
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

    private fun handleState (it : ChatStates) {
        when(it) {
            is ChatStates.Default -> {
                val layoutManager = LinearLayoutManager(this)
                layoutManager.reverseLayout = true
                layoutManager.stackFromEnd = true
                binding.rvChatList.layoutManager = layoutManager


                adapter = RecentChatsAdapter(this@HomeActivity, it.data)
                binding.rvChatList.adapter = adapter
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

