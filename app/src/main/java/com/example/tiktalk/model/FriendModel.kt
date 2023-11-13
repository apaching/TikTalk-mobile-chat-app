package com.example.tiktalk.model

data class FriendModel(

    val sender : String? = null,
    val recipient : String? = null,
    // possible status: friend, not_friend...
    val status : String ? = null

)
