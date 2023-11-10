package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiktalk.adapter.FriendRequestsAdapter
import com.example.tiktalk.databinding.ActivityFriendRequestBinding
import com.example.tiktalk.state.FriendStates
import com.example.tiktalk.viewmodel.FriendsViewModel

class FriendRequestsActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityFriendRequestBinding
    private lateinit var adapter : FriendRequestsAdapter
    private lateinit var viewModel : FriendsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFriendRequestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = true
        layoutManager.stackFromEnd = true
        binding.rvFriendRequests.layoutManager = layoutManager

        viewModel = FriendsViewModel()
        viewModel.getState().observe(this@FriendRequestsActivity) {
            handleState(it)
        }

        viewModel.getFriendRequestList()
    }

    private fun handleState(it : FriendStates) {
        when(it) {
            is FriendStates.Default -> {
                if(it.list.isEmpty()) {
                    binding.tvNoFriendRequest.visibility = View.VISIBLE
                    binding.rvFriendRequests.visibility = View.GONE
                } else {
                    adapter = FriendRequestsAdapter(this@FriendRequestsActivity, it.list, viewModel)
                    binding.rvFriendRequests.adapter = adapter
                    binding.tvNoFriendRequest.visibility = View.GONE
                }
            }

            else -> {}
        }
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, FriendRequestsActivity::class.java))
        }
    }
}