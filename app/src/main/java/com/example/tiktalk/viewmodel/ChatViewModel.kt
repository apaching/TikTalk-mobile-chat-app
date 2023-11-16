package com.example.tiktalk.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktalk.adapter.ChatStates
import com.example.tiktalk.model.ChatInformationModel
import com.example.tiktalk.model.MessageModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.database.getValue

class ChatViewModel : ViewModel() {

    private val chatStates = MutableLiveData<ChatStates>()

    private var database = Firebase.database.reference
    private var auth = Firebase.auth

    private var messageList = ArrayList<MessageModel>()
    private var recentChatsList = ArrayList<String>()

    fun getState() : LiveData<ChatStates> = chatStates

    fun sendMessage(messageModel : MessageModel, friendUid : String) {
        database.child("users_list/${auth.currentUser?.uid}/chat_list/$friendUid/messages").push().setValue(messageModel)
        database.child("users_list/$friendUid/chat_list/${auth.currentUser?.uid}/messages").push().setValue(messageModel)

        val chatInformationModel = ChatInformationModel(
            "open",
            "unarchived"
        )

        database.child("users_list/${auth.currentUser?.uid}/chat_list/$friendUid/chat_information").setValue(chatInformationModel)
        database.child("users_list/$friendUid/chat_list/${auth.currentUser?.uid}/chat_information").setValue(chatInformationModel)
    }

    fun retrieveRecentChats() {
        val objectLister = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                recentChatsList.clear()

                for (data in snapshot.children) {
                    val archiveStatus = data.child("chat_information/archiveStatus").getValue(String::class.java)
                    Log.d("status", archiveStatus.toString())
                    Log.d("data", data.key.toString())
                    if (archiveStatus == "unarchived") {
                        recentChatsList.add(data.key.toString())
                    }
                }

                chatStates.value = ChatStates.Default(recentChatsList)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        }

        database.child("users_list/${auth.currentUser?.uid}/chat_list").addValueEventListener(objectLister)
    }

    fun retrieveConversation(friendUid: String) {
        val childListListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                snapshot.getValue<MessageModel>()?.let { messageList.add(it) }
                chatStates.value = ChatStates.ChatAdded(messageList)
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                //
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                //
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }

        }

        database.child("users_list/${auth.currentUser?.uid}/chat_list/$friendUid/messages").addChildEventListener(childListListener)
    }

    fun newMessage(friendUid : String) {
        val senderObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isUserInList = false

                for (data in snapshot.children) {
                    if (friendUid == data.key) {
                        isUserInList = true
                        break
                    }
                }

                if (!isUserInList) {
                    val chatInformationModel = ChatInformationModel(
                        "open",
                        "archived"
                    )

                    database.child("users_list/${auth.currentUser?.uid}/chat_list/$friendUid/chat_information").setValue(chatInformationModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }
        }

        database.child("users_list/${auth.currentUser?.uid}/chat_list").addValueEventListener(senderObjectListener)

        val recipientObjectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isUserInList = false

                for (data in snapshot.children) {
                    if (auth.currentUser?.uid == data.key) {
                        isUserInList = true
                        break
                    }
                }

                if (!isUserInList) {
                    val chatInformationModel = ChatInformationModel(
                        "open",
                        "archived"
                    )

                    database.child("users_list/$friendUid/chat_list/${auth.currentUser?.uid}/chat_information").setValue(chatInformationModel)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //
            }

        }

        database.child("users_list/$friendUid/chat_list").addValueEventListener(recipientObjectListener)
    }
}