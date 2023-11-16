package com.example.tiktalk.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.example.tiktalk.R
import com.example.tiktalk.databinding.FriendProfileBinding
import com.example.tiktalk.model.UserInfoModel
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.state.FriendStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel
import com.example.tiktalk.viewmodel.FriendsViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.auth.ktx.auth

class FriendProfileActivity : AppCompatActivity() {

    private lateinit var binding : FriendProfileBinding
    private lateinit var viewModel : FriendsViewModel

    private var userInfoModel : UserInfoModel? = null

    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = FriendProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val customTitle = TextView(this@FriendProfileActivity)
        customTitle.text = getString(R.string.search)
        customTitle.setTextAppearance(R.style.ActionBarTitleText)

        supportActionBar?.customView = customTitle
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)


        viewModel = FriendsViewModel()
        viewModel.getState().observe(this@FriendProfileActivity) {
            handleState(it)
        }

        userInfoModel = intent.getParcelableExtra("user_info_model")

        viewModel.getFriendUserInfo(userInfoModel)

        binding.btnAddFriend.setOnClickListener {
            binding.btnAddFriend.visibility = View.GONE
            binding.btnCancelRequest.visibility = View.VISIBLE

            viewModel.sendFriendRequest(auth.currentUser?.uid, userInfoModel?.uid)
        }

        binding.btnAccept.setOnClickListener {
            binding.btnAccept.visibility = View.GONE
            binding.btnDecline.visibility = View.GONE
            viewModel.updateFriendRequestStatus(userInfoModel?.uid, auth.currentUser?.uid, "friend")
        }

        binding.btnDecline.setOnClickListener {
            binding.btnAddFriend.visibility = View.GONE
            binding.btnAccept.visibility = View.GONE
            binding.btnDecline.visibility = View.GONE
            viewModel.updateFriendRequestStatus(userInfoModel?.uid, auth.currentUser?.uid, "not_friend")
        }

        binding.btnCancelRequest.setOnClickListener {
            binding.btnAddFriend.visibility = View.VISIBLE
            binding.btnCancelRequest.visibility = View.GONE
            viewModel.updateFriendRequestStatus(auth.currentUser?.uid, userInfoModel?.uid,"not_friend")
        }
    }

    private fun handleState(it : FriendStates) {
        when(it) {
            is FriendStates.InformationRetrieved -> {
                binding.tvFriendUsername.text = it.user?.name
                binding.tvAboutMe.text = it.user?.aboutUser
                viewModel.checkFriendshipStatus(userInfoModel)
            }

            is FriendStates.RequestSent -> {

            }

            is FriendStates.FriendshipStatusRetrieved -> {
                // Checks if this is the first interaction of both users. The current user will
                // be able to send a friend request to the other user

                // Also used for checking if "not_friend"
                if ((it.status.isNullOrEmpty() && it.sender.isNullOrEmpty())
                    || it.status == "not_friend") {
                    binding.btnAddFriend.visibility = View.VISIBLE
                }

                // Checks if the status is pending and checks if the sender of the friend request
                // is the current user. If that is the case, the sender will be able to see a
                // cancel button to cancel the request.
                if (it.status == "pending" && it.sender == auth.currentUser?.uid) {
                    binding.btnCancelRequest.visibility = View.VISIBLE
                }

                // Checks if the status is pending and checks if the sender of the friend request
                // is not the current user. If that is the case, the current user will be able to
                // accept or decline the request
                if (it.status == "pending" && it.sender != auth.currentUser?.uid) {
                    binding.btnAccept.visibility = View.VISIBLE
                    binding.btnDecline.visibility = View.VISIBLE
                }

                 if (it.status == "friend") {
                     binding.btnUnfriend.visibility = View.GONE
                     binding.btnAccept.visibility = View.GONE
                     binding.btnDecline.visibility = View.GONE
                }

                if (it.status == "not_friend") {
                    binding.btnAddFriend.visibility = View.VISIBLE
                    binding.btnUnfriend.visibility = View.GONE
                }
            }


            else -> {}
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