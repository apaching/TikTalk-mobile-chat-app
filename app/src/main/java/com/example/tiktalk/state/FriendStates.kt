package com.example.tiktalk.state

import com.example.tiktalk.model.FriendModel

sealed class FriendStates {

    data class Default(val list : ArrayList<FriendModel>) : FriendStates()
    data object RequestSent : FriendStates()
    data object Test : FriendStates()
}
