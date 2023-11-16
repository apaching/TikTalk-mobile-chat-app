package com.example.tiktalk.state

import com.example.tiktalk.model.MessageModel
import com.example.tiktalk.model.UserInfoModel

sealed class ChatStates {

    data class Default(val data : ArrayList<String>?) : ChatStates()
    data class ChatAdded(val data : ArrayList<MessageModel>?) : ChatStates()
    data class InformationRetrieved(val data: UserInfoModel?) : ChatStates()

}