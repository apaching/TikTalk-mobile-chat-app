package com.example.tiktalk.adapter

import android.content.Context
import android.text.Layout
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.tiktalk.databinding.ItemContainerReceivedMessageBinding
import com.example.tiktalk.databinding.ItemContainerSentMessageBinding

import com.example.tiktalk.model.MessageModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth


class ChatAdapter(val context : Context, val messageList : ArrayList<MessageModel>?) : RecyclerView.Adapter<ChatAdapter.ViewHolder>(){

    private var auth = Firebase.auth

    override fun getItemViewType(position: Int) : Int {
        return if (messageList?.get(position)?.senderUid == auth.currentUser?.uid) {
            Log.d("user id", messageList?.get(position)?.senderUid.toString())
            1
        } else {
            Log.d("user id", messageList?.get(position)?.senderUid.toString())
            2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (viewType == 1) {
            ViewHolder(
                ItemContainerSentMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                context
            )
        } else {
            ViewHolder(
                ItemContainerReceivedMessageBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),
                context
            )
        }

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        messageList?.get(position).let {
            if (it != null){
                holder.bind(it, position)
            }
        }
    }

    override fun getItemCount(): Int = this.messageList?.size!!
    class ViewHolder (val binding : ViewBinding, val context : Context) : RecyclerView.ViewHolder(binding.root){
        fun bind(message : MessageModel, position: Int) {
            if (binding is ItemContainerSentMessageBinding) {
                binding.tvMessage.text = message.message
            } else if (binding is ItemContainerReceivedMessageBinding) {
                binding.tvMessage.text = message.message
            }
        }
    }
}