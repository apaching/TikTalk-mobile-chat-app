package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiktalk.R
import com.example.tiktalk.adapter.ChatAdapter
import com.example.tiktalk.state.ChatStates
import com.example.tiktalk.databinding.ActivityChatBinding
import com.example.tiktalk.model.MessageModel
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.ChatViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var binding :  ActivityChatBinding
    private lateinit var chatViewModel : ChatViewModel
    private lateinit var adapter : ChatAdapter
    private lateinit var customTitle : TextView

    private var friendUid : String? = null

    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        friendUid = intent.getStringExtra("friend_uid")

        chatViewModel = ChatViewModel()
        chatViewModel.getState().observe(this@ChatActivity) {
            handleState(it)
        }

        chatViewModel.getUserInfo(friendUid.toString())

        chatViewModel.newMessage(friendUid.toString())
        chatViewModel.retrieveConversation(friendUid.toString())

        chatViewModel.scrollToBottomEvent.observe(this, Observer {
            // Scroll to the bottom after adding a new message
            binding.rvChat.smoothScrollToPosition(adapter.itemCount - 1)
        })


        binding.rvChat.layoutManager = LinearLayoutManager(this)

        customTitle = TextView(this@ChatActivity)
        customTitle.text = getString(R.string.sample_name_2) //another user name
        customTitle.setTextAppearance(R.style.ActionBarTitleText)


        val layoutParams = ActionBar.LayoutParams(
            ActionBar.LayoutParams.WRAP_CONTENT,
            ActionBar.LayoutParams.WRAP_CONTENT,
            Gravity.CENTER
        )

        supportActionBar?.customView = customTitle
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM

        supportActionBar?.apply {
            customView = customTitle
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(customTitle, layoutParams)
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        binding.btnSend.setOnClickListener {
            // Generate unix timestamp
            val unixTimestamp : Long = System.currentTimeMillis()

            val senderUid = auth.currentUser?.uid
            // Get message
            val message = binding.etMessage.text.toString()
            // Get the time when the message is being sent and store it in timeStamp
            val calendar = Calendar.getInstance()
            val dateFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
            val timeStamp = dateFormat.format(calendar.time).toString()

            // Store uid, message, and timeStamp into a model
            val messageModel = MessageModel(
                senderUid,
                message,
                timeStamp
            )

            chatViewModel.sendMessage(messageModel, friendUid.toString(), unixTimestamp)

            binding.etMessage.text.clear()
        }

    }

    private fun handleState(it : ChatStates) {
        when(it) {
            is ChatStates.ChatAdded -> {
                adapter = ChatAdapter(this@ChatActivity, it.data)
                binding.rvChat.adapter = adapter
            }

            is ChatStates.InformationRetrieved -> {
                customTitle.text = it.data?.name.toString()
            }

            else -> {}
        }
    }

    private fun handleState(it : AuthenticationStates) {
        when(it) {


            else -> {}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
            }
        }
        return true
    }


    companion object {
        fun launch(activity : Activity) {
            activity.startActivity(Intent(activity, ChatActivity::class.java))
        }
    }
}