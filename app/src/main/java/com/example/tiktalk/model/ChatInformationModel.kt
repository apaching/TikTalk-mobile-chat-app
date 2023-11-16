package com.example.tiktalk.model

data class ChatInformationModel(

    // If chat is open/expired
    // Open = Users can converse
    // Expired = Users can't converse anymore / Timer expired
    val timeStatus : String? = null,
    // Archived = Won't be displayed in the user's home screen
    // Unarchived = Chat will be displayed in the user's home screen
    val archiveStatus : String? = null,
    // Used for checking which chat should be at top
    val timeStamp : Long? = null

)
