package com.example.tiktalk.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktalk.model.FriendModel
import com.example.tiktalk.model.UserInfoModel
import com.example.tiktalk.state.FriendStates
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FriendsViewModel : ViewModel() {

    private val friendStates = MutableLiveData<FriendStates>()

    private var database = Firebase.database.reference
    private var auth = Firebase.auth

    private var friendRequestsList = ArrayList<FriendModel>()
    private var userInfoList = ArrayList<UserInfoModel>()

    fun getState() : LiveData<FriendStates> = friendStates

    fun sendFriendRequest(senderId : String?, recipientId : String?) {
        val senderObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendModel = FriendModel (
                    senderId,
                    recipientId,
                    "pending"
                )

                database.child("users_list/$senderId/friends_list").push().setValue(friendModel)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        database.child("users_list/$senderId/friends_list").addListenerForSingleValueEvent(senderObjectListener)

        val recipientObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val friendModel = FriendModel (
                    senderId,
                    recipientId,
                    "pending"
                )

                database.child("users_list/$recipientId/friends_list").push().setValue(friendModel)
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        database.child("users_list/$recipientId/friends_list").addListenerForSingleValueEvent(recipientObjectListener)

        friendStates.value = FriendStates.RequestSent
    }

    // Gets the list of friend requests of the user
    fun getFriendRequestList () {
        val listListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                friendRequestsList.clear()

                // Goes through the list of friends of the user
                // Checks the status of each friend. If the friend is pending, that means it's a friend request
                // Checks if the sender of the request is != with the current user. If it's the same, it won't
                // get displayed

                for(data in snapshot.children) {
                    val sender = data.child("sender").getValue(String::class.java)
                    val status = data.child("status").getValue(String::class.java)

                    if(sender != auth.currentUser?.uid && status == "pending") {
                        data.getValue<FriendModel>()?.let { friendRequestsList.add(it) }
                    }
                }

                friendStates.value = FriendStates.Default(friendRequestsList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        database.child("users_list/${auth.currentUser?.uid}/friends_list").addListenerForSingleValueEvent(listListener)
    }

    fun updateFriendRequestStatus (senderId : String?, recipientId: String?, status : String?) {
        val senderObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children) {
                    val snapshotSenderId = data.child("sender").getValue(String::class.java)
                    val snapshotRecipientId= data.child("recipient").getValue(String::class.java)

                    if(snapshotSenderId == senderId && snapshotRecipientId == recipientId) {
                        val entryId = data.key

                        val updatedFriend = FriendModel(
                            senderId,
                            recipientId,
                            status
                        )

                        database.child("users_list/$senderId/friends_list/$entryId").setValue(updatedFriend)
                            .addOnSuccessListener {
                                getFriendRequestList()
                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.child("users_list/$senderId/friends_list").addListenerForSingleValueEvent(senderObjectListener)

        val recipientObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for(data in snapshot.children) {
                    val snapshotSenderId = data.child("sender").getValue(String::class.java)
                    val snapshotRecipientId= data.child("recipient").getValue(String::class.java)

                    if(snapshotSenderId == senderId && snapshotRecipientId == recipientId) {
                        val entryId = data.key

                        val updatedFriend = FriendModel(
                            senderId,
                            recipientId,
                            status
                        )

                        database.child("users_list/$recipientId/friends_list/$entryId").setValue(updatedFriend)
                            .addOnSuccessListener {
                                getFriendRequestList()
                            }
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.child("users_list/$recipientId/friends_list").addListenerForSingleValueEvent(recipientObjectListener)
    }
}