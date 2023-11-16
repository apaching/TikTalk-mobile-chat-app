package com.example.tiktalk.adapter

import com.example.tiktalk.model.MessageModel

sealed class ChatStates {

    data class Default(val data : ArrayList<String>?) : ChatStates()
    data class ChatAdded(val data : ArrayList<MessageModel>?) : ChatStates()

}