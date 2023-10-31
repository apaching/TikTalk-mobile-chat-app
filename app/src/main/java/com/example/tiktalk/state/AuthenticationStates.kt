package com.example.tiktalk.state

sealed class AuthenticationStates {

    data object Default : AuthenticationStates()
    data class AlreadySignedIn (val alreadySignedIn : Boolean) : AuthenticationStates()
    data object SignedIn : AuthenticationStates()
    data object SignedUp : AuthenticationStates()
    data object SignedOut : AuthenticationStates()
    data object Error : AuthenticationStates()

}
