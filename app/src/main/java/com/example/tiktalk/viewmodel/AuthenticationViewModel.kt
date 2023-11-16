package com.example.tiktalk.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tiktalk.model.UserInfoModel
import com.example.tiktalk.state.AuthenticationStates
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase



class AuthenticationViewModel : ViewModel() {

    private val authenticationStates = MutableLiveData<AuthenticationStates>()

    private var auth = Firebase.auth
    private var database = Firebase.database.reference

    fun getState() : LiveData<AuthenticationStates> = authenticationStates

    fun isUserSignedIn() {
        if (auth.currentUser != null) {
            authenticationStates.postValue(AuthenticationStates.AlreadySignedIn(true))
        } else {
            authenticationStates.postValue(AuthenticationStates.AlreadySignedIn(false))
        }
    }

    fun getCurrentUserInfo() {
        val objectListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue<UserInfoModel>()

                authenticationStates.value = AuthenticationStates.Default(user)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        }

        database.child("users_list/${auth.currentUser?.uid}/user_information").addListenerForSingleValueEvent(objectListener)
    }

    fun changeUserPassword(email : String){
        val emailAddress = email

        Firebase.auth.sendPasswordResetEmail(emailAddress)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    authenticationStates.value = AuthenticationStates.ResetPasswordEmailSent
                }else {
                    authenticationStates.value = AuthenticationStates.Error
                }
            }
    }

    fun signUp(email : String, password : String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    authenticationStates.value = AuthenticationStates.SignedUp
                } else {
                    authenticationStates.value = AuthenticationStates.Error
                }
            }
            .addOnFailureListener {
                authenticationStates.value = AuthenticationStates.Error
            }
    }

    fun signIn(email : String, password : String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                authenticationStates.value = AuthenticationStates.SignedIn
            } else {
                authenticationStates.value = AuthenticationStates.Error

            }
        }.addOnFailureListener {
            authenticationStates.value = AuthenticationStates.Error
        }
    }

    fun signOut() {
        auth.signOut()
        authenticationStates.value = AuthenticationStates.SignedOut
    }

    fun createUserRecord(uid : String, name : String, email : String) {
        val databaseRef = database.child("users_list/$uid/user_information")

        val userInfoModel = UserInfoModel(
            uid,
            name,
            email,
            null,
            null,
        )

        databaseRef.setValue(userInfoModel)
    }
}