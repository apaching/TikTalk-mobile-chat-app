package com.example.tiktalk.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.ActionBar
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.tiktalk.R
import com.example.tiktalk.databinding.ActivityLoginBinding
import com.example.tiktalk.databinding.ActivityProfileBinding
import com.example.tiktalk.model.UserInfoModel
import com.example.tiktalk.state.AuthenticationStates
import com.example.tiktalk.viewmodel.AuthenticationViewModel
import java.io.ByteArrayOutputStream

class UserProfileActivity : AppCompatActivity() {

    private lateinit var binding : ActivityProfileBinding
    private lateinit var viewModel : AuthenticationViewModel

    private lateinit var customTitle : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)


        customTitle = TextView(this@UserProfileActivity)
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

        var isEdit = false

        binding.btnEditProfile.setOnClickListener {
            if (!isEdit) {
                binding.clProfile.visibility = View.GONE
                binding.btnConfirmEdit.visibility = View.VISIBLE
                binding.btnEditImage.visibility = View.VISIBLE
                binding.clEditProfile.visibility = View.VISIBLE

                binding.btnEditProfile.text = "CANCEL EDIT"

                isEdit = true
            } else {
                binding.clProfile.visibility = View.VISIBLE
                binding.btnConfirmEdit.visibility = View.GONE
                binding.btnEditImage.visibility = View.GONE
                binding.clEditProfile.visibility = View.GONE

                binding.btnEditProfile.text = "EDIT PROFILE"

                isEdit = false
            }
        }

        val galleryIntentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult(), ActivityResultCallback {
            if(it.resultCode == RESULT_OK){
                val imageUri = it.data?.data
                val imageStream = imageUri?.let { it1 -> contentResolver.openInputStream(it1) }
                val photo = BitmapFactory.decodeStream(imageStream)
                binding.ivProfile.setImageBitmap(photo)
            }
        })

        binding.btnEditImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            galleryIntentLauncher.launch(intent)
        }

        binding.btnConfirmEdit.setOnClickListener {
            if (binding.etUsername.text!!.isNotEmpty() &&
                binding.etAboutMe.text!!.isNotEmpty()) {

                val newName = binding.etUsername.text.toString()
                val newAboutMe = binding.etAboutMe.text.toString()

                val bitmap = (binding.ivProfile.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)

                viewModel.editUserRecord(baos.toByteArray(), newName, newAboutMe)
            }
        }
    }


    private fun handleState(it : AuthenticationStates) {
        when(it) {
            is AuthenticationStates.Default -> {
                binding.tvUsername.text = it.user?.name
                binding.tvAboutMe.text = it.user?.aboutUser

                customTitle.text = it.user?.name

                if (it.user?.imageUrl != null) {
                    Glide.with(this@UserProfileActivity)
                        .load(it.user.imageUrl)
                        .apply(RequestOptions().centerCrop().override(150, 150))
                        .into(binding.ivProfile)
                }

                binding.btnConfirmEdit.visibility = View.GONE
                binding.clEditProfile.visibility = View.GONE
                binding.btnEditImage.visibility = View.GONE
            }

            is AuthenticationStates.EditSuccess -> {
                finish()
            }

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
        fun launch (activity : Activity) {
            activity.startActivity(Intent(activity, UserProfileActivity::class.java))
        }
    }

}