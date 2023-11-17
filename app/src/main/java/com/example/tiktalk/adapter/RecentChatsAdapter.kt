package com.example.tiktalk.adapter

import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.os.Message
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tiktalk.R
import com.example.tiktalk.databinding.EachChatBinding
import com.example.tiktalk.model.MessageModel
import com.example.tiktalk.ui.ChatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class RecentChatsAdapter(val context : Context, val chatList : ArrayList<String>?) : RecyclerView.Adapter<RecentChatsAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(EachChatBinding.inflate(LayoutInflater.from(parent.context), parent, false), context)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        chatList?.get(position).let {
            if (it != null) {
                holder.bind(it, position)
            }
        }
    }

    override fun getItemCount(): Int = this.chatList?.size!!

    class ViewHolder(val binding : EachChatBinding, val context : Context) : RecyclerView.ViewHolder(binding.root) {
        fun bind(userId : String, position : Int) {
            binding.ll.setOnClickListener {
                val intent = Intent(context, ChatActivity::class.java)
                intent.putExtra("friend_uid", userId)
                context.startActivity(intent)
            }

            val auth = Firebase.auth
            val database = Firebase.database.reference

            val objectListener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val lastMessage = snapshot.children.last().getValue(MessageModel::class.java)

                    binding.tvTimestamp.text = lastMessage?.timeStamp

                    if (lastMessage?.senderUid == auth.currentUser?.uid) {
                        binding.tvPreviewMessage.text = "You: " + lastMessage?.message.toString()
                    } else {

                        // Unclean code incoming(?)
                        val listener  = object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val fullName = snapshot.child("name").getValue(String::class.java)
                                val firstName = fullName?.split(" ")?.get(0)

                                binding.tvPreviewMessage.text = firstName + ": " + lastMessage?.message.toString()
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                        }

                        database.child("users_list/${lastMessage?.senderUid}/user_information").addValueEventListener(listener)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }

            database.child("users_list/${auth.currentUser?.uid}/chat_list/$userId/messages")
                .orderByKey()
                .limitToLast(1)
                .addValueEventListener(objectListener)

            val objectListenerTwo  = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    binding.tvUsername.text = snapshot.child("name").getValue(String::class.java)

                    val imageUrl = snapshot.child("imageUrl").getValue(String::class.java)
                    if (imageUrl != null) {
                        Glide.with(context)
                            .load(imageUrl)
                            .apply(RequestOptions().centerCrop().override(150, 150))
                            .into(binding.ivUsersImg)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }

            database.child("users_list/$userId/user_information").addValueEventListener(objectListenerTwo)

            val objectListenerThree = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val readStatus = snapshot.child("read").getValue(Boolean::class.java)
                    if (readStatus == false) {
                        val customFont: Typeface? = ResourcesCompat.getFont(context, R.font.montserrat_bold)
                        binding.tvPreviewMessage.typeface = customFont
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            }

            database.child("users_list/${auth.currentUser?.uid}/chat_list/$userId/chat_information").addValueEventListener(objectListenerThree)
        }
    }
}