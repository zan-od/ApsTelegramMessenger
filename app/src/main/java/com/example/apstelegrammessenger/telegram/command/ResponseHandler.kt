package com.example.apstelegrammessenger.telegram.command

interface ResponseHandler {
    fun handle(chatId: Long, message: String, broadcast: Boolean)
}