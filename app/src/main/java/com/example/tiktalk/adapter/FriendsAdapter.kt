package com.example.tiktalk.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.tiktalk.databinding.EachUserBinding
import com.example.tiktalk.databinding.ItemFriendRequestBinding
import com.example.tiktalk.model.FriendModel
import com.example.tiktalk.model.UserInfoModel
import com.example.tiktalk.ui.ChatActivity
import com.example.tiktalk.ui.FriendProfileActivity
import com.example.tiktalk.viewmodel.ChatViewModel
import com.example.tiktalk.viewmodel.FriendsViewModel
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class FriendsAdapter(val context : Context, val friendsList : ArrayList<String>?, val viewModel : FriendsViewModel) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsAdapter.ViewHolder {
        return ViewHolder(EachUserBinding.inflate(LayoutInflater.from(parent.context), parent, false), context, viewModel
        )
    }

    override fun getItemCount(): Int = this.friendsList?.size!!
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        friendsList?.get(position).let {
            if (it != null){
                holder.bind(it, position)
            }
        }
    }

    class ViewHolder(val binding : EachUserBinding, val context : Context, val viewModel : ViewModel) : RecyclerView.ViewHolder(binding.root){
        fun bind(friendUid : String, position : Int) {
            val database = Firebase.database.reference

            val objectListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userInfoModel = snapshot.getValue<UserInfoModel>()

                    binding.tvUsername.text = userInfoModel?.name.toString()

                    binding.btnNewMessage.setOnClickListener {
                        val intent = Intent(context, ChatActivity::class.java)
                        intent.putExtra("friend_uid", friendUid)
                        context.startActivity(intent)
                    }

                    binding.ll.setOnClickListener {
                        val intent = Intent(context, FriendProfileActivity::class.java)
                        intent.putExtra("user_info_model", userInfoModel)
                        context.startActivity(intent)
                    }
                }

                override fun onCancelled(error: DatabaseError) {

                }

            }

            database.child("users_list/$friendUid/user_information").addListenerForSingleValueEvent(objectListener)
        }
    }
}