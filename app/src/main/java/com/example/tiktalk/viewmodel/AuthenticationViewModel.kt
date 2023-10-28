package com.example.tiktalk.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktalk.state.AuthenticationStates

class AuthenticationViewModel : ViewModel() {

    private val authenticationStates = MutableLiveData<AuthenticationStates>()


}