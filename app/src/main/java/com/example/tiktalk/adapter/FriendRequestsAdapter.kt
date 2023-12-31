package com.example.tiktalk.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.provider.ContactsContract.Profile
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tiktalk.databinding.ItemFriendRequestBinding
import com.example.tiktalk.model.FriendModel
import com.example.tiktalk.model.UserInfoModel
import com.example.tiktalk.ui.FriendProfileActivity
import com.example.tiktalk.ui.FriendRequestsActivity
import com.example.tiktalk.ui.SignupActivity
import com.example.tiktalk.viewmodel.FriendsViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database

class FriendRequestsAdapter(val context : Context, val friendRequestsList : ArrayList<FriendModel>?, val viewModel : FriendsViewModel) : RecyclerView.Adapter<FriendRequestsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendRequestsAdapter.ViewHolder {
        return ViewHolder(ItemFriendRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false), context, viewModel)
    }

    override fun onBindViewHolder(holder: FriendRequestsAdapter.ViewHolder, position: Int) {
        friendRequestsList?.get(position).let {
            if (it != null){
                holder.bind(it, position)
            }
        }
    }

    override fun getItemCount() : Int = this.friendRequestsList?.size!!

    class ViewHolder(val binding : ItemFriendRequestBinding, val context : Context, val viewModel : FriendsViewModel) : RecyclerView.ViewHolder(binding.root) {

        fun bind(friend : FriendModel , position : Int) {
            val database = Firebase.database.reference

            val objectListenerOne = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val name = snapshot.child("name").getValue(String::class.java)
                    binding.tvUsername.text = name

                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
                    if (imageUrl != null) {
                        Glide.with(context)
                            .load(imageUrl)
                            .apply(RequestOptions().centerCrop().override(150, 150))
                            .into(binding.ivRoundedImage)
                    }


                    val userInfoModel = snapshot.getValue<UserInfoModel>()
                    binding.ll.setOnClickListener {
                        val intent = Intent(context, FriendProfileActivity::class.java)
                        intent.putExtra("user_info_model", userInfoModel)
                        context.startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }

            database.child("users_list/${friend.sender}/user_information").addListenerForSingleValueEvent(objectListenerOne)



            val objectListenerTwo = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for(data in snapshot.children) {
                        val senderId = data.child("sender").getValue(String::class.java)
                        val recipientId = data.child("recipient").getValue(String::class.java)

                        if (senderId == friend.sender && recipientId == friend.recipient) {
                            binding.btnConfirm.setOnClickListener {
                                viewModel.updateFriendRequestStatus(senderId, recipientId, "friend")
                            }

                            binding.btnDelete.setOnClickListener {
                                viewModel.updateFriendRequestStatus(senderId, recipientId, "not_friend")
                            }
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }

            database.child("users_list/${friend.recipient}/friends_list").addListenerForSingleValueEvent(objectListenerTwo)
        }
    }
}