package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.AutoCompleteTextView
import android.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiktalk.adapter.FriendSearchAdapter
import com.example.tiktalk.databinding.ActivityAddFriendBinding
import com.example.tiktalk.state.FriendStates
import com.example.tiktalk.viewmodel.FriendsViewModel

class AddFriendActivity : AppCompatActivity() {

    private lateinit var binding : ActivityAddFriendBinding
    private lateinit var viewModel : FriendsViewModel
    private lateinit var adapter : FriendSearchAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddFriendBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = FriendsViewModel()
        viewModel.getState().observe(this@AddFriendActivity) {
            handleState(it)
        }

        val layoutManager = LinearLayoutManager(this)
        layoutManager.reverseLayout = false
        layoutManager.stackFromEnd = true
        binding.rvAddFriend.layoutManager = layoutManager

        binding.searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    viewModel.performSearch(query)
                }

                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.performSearch(newText.orEmpty())
                return true
            }

        })

    }

    private fun handleState(it : FriendStates) {
        when(it) {
            is FriendStates.SearchFinished -> {
                adapter = FriendSearchAdapter(this@AddFriendActivity, it.results)
                binding.rvAddFriend.adapter = adapter
            }

            else -> {}
        }
    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, AddFriendActivity::class.java))
        }
    }
}