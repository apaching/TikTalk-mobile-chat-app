package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityLoginBinding
import com.example.tiktalk.databinding.ActivityProfileBinding
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var viewModel : AuthenticationViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val customTitle = TextView(this@UserProfileActivity)
        customTitle.text = getString(R.string.sample_name) //user name
        customTitle.setTextAppearance(R.style.ActionBarTitleText)

        supportActionBar?.customView = customTitle
        supportActionBar?.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM


        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_24)

        viewModel = AuthenticationViewModel()
        viewModel.getState().observe(this@UserProfileActivity) {
            handleState(it)
        }

        viewModel.getCurrentUserInfo()

        viewModel = AuthenticationViewModel()
        viewModel.getState().observe(this@UserProfileActivity) {
            handleState(it)
        }

        viewModel.getCurrentUserInfo()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                HomeActivity.launch(this@UserProfileActivity)
                finish()
            }
        }
        return true
    }

    private fun handleState(it : AuthenticationStates) {
        when(it) {
            is AuthenticationStates.Default -> {
                binding.tvUsername.text = it.user?.name
            }

            else -> {}
        }

    }

    companion object {
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, UserProfileActivity::class.java))
        }
    }

}