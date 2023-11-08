package com.example.tiktalk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktalk.model.FriendModel
import com.example.tiktalk.state.FriendStates
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendsViewModel : ViewModel() {

    private val friendStates = MutableLiveData<FriendStates>()

    private var database = Firebase.database.reference

    fun getState() : LiveData<FriendStates> = friendStates

    fun sendFriendRequest(senderId : String?, recipientId : String?) {
        val senderObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendModel = FriendModel (
                    recipientId,
                    "pending"
                )

                database.child("users_list/$senderId/friends_list").setValue(friendModel)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        database.child("users_list/$senderId/friends_list").addListenerForSingleValueEvent(senderObjectListener)

        val recipientObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendModel = FriendModel (
                    senderId,
                    "pending"
                )

                database.child("users_list/$recipientId/friends_list").setValue(friendModel)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        database.child("users_list/$recipientId/friends_list").addListenerForSingleValueEvent(recipientObjectListener)

        friendStates.value = FriendStates.RequestSent
    }

    fun updateFriendRequestStatus (senderId : String?, recipientId: String?, status : String?) {
        ///
    }
}