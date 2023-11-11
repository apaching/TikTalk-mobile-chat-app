package com.example.tiktalk.state

import com.example.tiktalk.model.UserInfoModel

sealed class   AuthenticationStates {

    data class Default (val user : UserInfoModel?) : AuthenticationStates()
    data class AlreadySignedIn (val alreadySignedIn : Boolean) : AuthenticationStates()
    data object SignedIn : AuthenticationStates()
    data object SignedUp : AuthenticationStates()
    data object SignedOut : AuthenticationStates()
    data object Error : AuthenticationStates()

}
