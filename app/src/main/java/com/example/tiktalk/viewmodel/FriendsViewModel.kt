package com.example.tiktalk.viewmodel

import android.util.Log
import kotlin.collections.toMutableList
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktalk.model.FriendModel
import com.example.tiktalk.model.UserInfoModel
import com.example.tiktalk.state.AuthenticationStates
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
    private var friendsList = ArrayList<String>()



    fun getState() : LiveData<FriendStates> = friendStates

    // Function for sending a friend request to another user
    fun sendFriendRequest(senderId : String?, recipientId : String?) {
        val senderObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isUserAlreadyInList = false

                for (data in snapshot.children) {
                    if (senderId == data.key) {
                        val friendModel = FriendModel(
                            senderId,
                            recipientId,
                            "pending"
                        )
                        database.child("users_list/$senderId/friends_list/$recipientId").setValue(friendModel)
                        isUserAlreadyInList = true
                        break
                    }
                }

                if (!isUserAlreadyInList) {
                    val friendModel = FriendModel(
                        senderId,
                        recipientId,
                        "pending"
                    )

                    database.child("users_list/$senderId/friends_list/$recipientId").setValue(friendModel)

                }

                friendStates.value = FriendStates.FriendsRetrieved(friendsList)
            }

            override fun onCancelled(error: DatabaseError) {

            }
        }

        database.child("users_list/$senderId/friends_list").addListenerForSingleValueEvent(senderObjectListener)

        val recipientObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isUserAlreadyInList = false

                for (data in snapshot.children) {
                    if (senderId == data.key) {
                        val friendModel = FriendModel(
                            senderId,
                            recipientId,
                            "pending"
                        )

                        database.child("users_list/$recipientId/friends_list/$senderId").setValue(friendModel)
                        isUserAlreadyInList = true
                        break
                    }
                }

                if (!isUserAlreadyInList) {
                    val friendModel = FriendModel(
                        senderId,
                        recipientId,
                        "pending"
                    )

                    database.child("users_list/$recipientId/friends_list/$senderId").setValue(friendModel)

                    friendStates.value = FriendStates.FriendsRetrieved(friendsList)
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

        }

        database.child("users_list/$recipientId/friends_list").addListenerForSingleValueEvent(recipientObjectListener)

    }

    // Gets the list of friend requests of the user
    fun getFriendRequestList() {
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

                friendStates.value = FriendStates.FriendRequestsRetrieved(friendRequestsList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        database.child("users_list/${auth.currentUser?.uid}/friends_list").addValueEventListener(listListener)

    }

    fun getFriendsList() {
        //
        friendsList.clear()

        val listListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (data in snapshot.children) {
                    val status = data.child("status").getValue(String::class.java)

                    if(status == "friend") {
                        friendsList.add(data.key.toString())

                    }
                }

                friendStates.value = FriendStates.FriendsRetrieved(friendsList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.child("users_list/${auth.currentUser?.uid}/friends_list").addValueEventListener(listListener)
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
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }
        database.child("users_list/$recipientId/friends_list").addListenerForSingleValueEvent(recipientObjectListener)
    }

    fun performSearch(query : String) {
        val objectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val results = mutableListOf<UserInfoModel>()

                if(query.isNotEmpty()) {
                    for(data in snapshot.children) {
                        val userInformationSnapshot = data.child("user_information")
                        val userInfo = userInformationSnapshot.getValue<UserInfoModel>()

                        if (userInfo != null
                            && userInfo.name!!.contains(query, ignoreCase = true)
                            && auth.currentUser?.uid != userInfo.uid) {

                            results.add(userInfo)
                        }
                    }
                }

                friendStates.value = FriendStates.SearchFinished(results as ArrayList<UserInfoModel>)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        database.child("users_list").addValueEventListener(objectListener)

    }

    fun getFriendUserInfo(userInfoModel : UserInfoModel?) {
        friendStates.value = FriendStates.InformationRetrieved(userInfoModel)
    }



    fun checkFriendshipStatus(userInfoModel : UserInfoModel?) {
        val objectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isUserAlreadyInList = false

                for(data in snapshot.children) {
                    if(userInfoModel?.uid == data.key) {
                        val status = data.child("status").getValue(String::class.java)
                        val sender = data.child("sender").getValue(String::class.java)

                        friendStates.value = FriendStates.FriendshipStatusRetrieved(status, sender)
                        isUserAlreadyInList = true
                        Log.d("TEST-FriendsViewModel", "User existing")
                        break
                    }
                }

                if(!isUserAlreadyInList) {
                    Log.d("TEST-FriendsViewModel", "User not existing")
                    friendStates.value = FriendStates.FriendshipStatusRetrieved(null, null)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        database.child("users_list/${auth.currentUser?.uid}/friends_list").addValueEventListener(objectListener)
    }
}