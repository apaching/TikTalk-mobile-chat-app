package com.example.tiktalk.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserInfoModel(

    val uid : String? = null,
    val name : String? = null,
    val email : String? = null,
    val imageUrl : String? = null,
    val aboutUser : String? = null,

) : Parcelable
