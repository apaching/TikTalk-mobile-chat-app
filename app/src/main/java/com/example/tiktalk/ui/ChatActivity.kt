package com.example.tiktalk.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Menu
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tiktalk.R
import com.example.tiktalk.adapter.ChatAdapter
import com.example.tiktalk.adapter.ChatStates
import com.example.tiktalk.databinding.ActivityChatBinding
import com.example.tiktalk.databinding.ToolbarTitleBinding
import com.example.tiktalk.model.MessageModel
import com.example.tiktalk.viewmodel.ChatViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ChatActivity : AppCompatActivity() {

    private lateinit var binding :  ActivityChatBinding
    private lateinit var viewModel : ChatViewModel
    private lateinit var adapter : ChatAdapter

    private var auth = Firebase.auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ChatViewModel()
        viewModel.getState().observe(this@ChatActivity) {
            handleState(it)
        }

        binding.rvChat.layoutManager = LinearLayoutManager(this)

        val customTitle = TextView(this@ChatActivity)
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

            viewModel.sendMessage(messageModel)

            binding.etMessage.text.clear()
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.etMessage.windowToken, 0)
        }

    }

    private fun handleState(it : ChatStates) {
        when(it) {
            is ChatStates.ChatAdded -> {
                val adapter = ChatAdapter(this@ChatActivity, it.data)
                binding.rvChat.adapter = adapter
            }

            else -> {}
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                HomeActivity.launch(this@ChatActivity)
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