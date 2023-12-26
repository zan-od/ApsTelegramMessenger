package com.example.apstelegrammessenger.telegram

data class TelegramSettings(
    val appId: Int,
    val appHash: String,
    val filesDir: String,
    val phoneNumber: String,
    val allowedChats: Set<Long>
)
