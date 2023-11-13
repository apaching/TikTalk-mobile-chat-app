package com.example.tiktalk.state

import com.example.tiktalk.model.FriendModel
import com.example.tiktalk.model.UserInfoModel

sealed class FriendStates {

    data class Default(val list : ArrayList<FriendModel>) : FriendStates()
    data class InformationRetrieved(val user : UserInfoModel?) : FriendStates()
    data class FriendshipStatusRetrieved(val status : String?, val sender : String?) : FriendStates()
    data object RequestSent : FriendStates()
    data class SearchFinished (val results : ArrayList<UserInfoModel>) : FriendStates()

}
