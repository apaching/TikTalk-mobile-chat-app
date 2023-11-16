package com.example.tiktalk.state

import com.example.tiktalk.model.UserInfoModel

sealed class   AuthenticationStates {

    // Used for displaying things sa screen upon retrieving the user's information
    data class Default (val user : UserInfoModel?) : AuthenticationStates()
    data class AlreadySignedIn (val alreadySignedIn : Boolean) : AuthenticationStates()
    data object SignedIn : AuthenticationStates()
    data object SignedUp : AuthenticationStates()
    data object SignedOut : AuthenticationStates()
    data object ResetPasswordEmailSent  : AuthenticationStates()
    data object Error : AuthenticationStates()

}

