package com.example.apstelegrammessenger.telegram.command

interface CommandHandler {
    fun handle(chatId: Long, message: String)
}