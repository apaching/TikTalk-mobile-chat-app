package com.example.tiktalk.state

import com.example.tiktalk.model.FriendModel
import com.example.tiktalk.model.UserInfoModel

sealed class FriendStates {

    data class FriendRequestsRetrieved(val list : ArrayList<FriendModel>) : FriendStates()
    data class FriendsRetrieved(val list : ArrayList<String>) : FriendStates()
    data class InformationRetrieved(val user : UserInfoModel?) : FriendStates()
    data class FriendshipStatusRetrieved(val status : String?, val sender : String?) : FriendStates()
    data class RequestSent(val status : String?, val sender : String?) : FriendStates()
    data class SearchFinished (val results : ArrayList<UserInfoModel>) : FriendStates()

}
