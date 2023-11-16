package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiktalk.R
import com.example.tiktalk.adapter.FriendRequestsAdapter
import com.example.tiktalk.adapter.FriendsAdapter
import com.example.tiktalk.databinding.ActivityUserFriendsListBinding
import com.example.tiktalk.state.FriendStates
import com.example.tiktalk.viewmodel.FriendsViewModel

class UserFriendsListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserFriendsListBinding
    private lateinit var adapter : FriendsAdapter
    private lateinit var viewModel : FriendsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserFriendsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = FriendsViewModel()
        viewModel.getState().observe(this@UserFriendsListActivity){
            handleState(it)
        }

        viewModel.getFriendsList()

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvFriendsList.layoutManager = layoutManager

        val customTitle = TextView(this@UserFriendsListActivity)
        customTitle.text = getString(R.string.friends_list)
        customTitle.setTextAppearance(R.style.ActionBarTitleText)

        supportActionBar?.customView = customTitle
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)
    }

    private fun handleState(it : FriendStates) {
        when(it) {
            is FriendStates.FriendsRetrieved -> {
                if(it.list.isEmpty()) {
                    binding.tvNoFriendRequest.visibility = View.VISIBLE
                    binding.rvFriendsList.visibility = View.GONE
                } else {
                    adapter = FriendsAdapter(this@UserFriendsListActivity, it.list, viewModel)
                    binding.rvFriendsList.adapter = adapter
                    binding.tvNoFriendRequest.visibility = View.GONE
                }

            }


            else -> {}
        }
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, UserFriendsListActivity::class.java))
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }
}