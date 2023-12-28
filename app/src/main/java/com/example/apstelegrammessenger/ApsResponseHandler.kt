package com.example.apstelegrammessenger

import com.example.apstelegrammessenger.telegram.TelegramService
import com.example.apstelegrammessenger.telegram.command.ResponseHandler

class ApsResponseHandler(
    private val telegramService: TelegramService
): ResponseHandler {
    override fun handle(chatId: Long, message: String, broadcast: Boolean) {
        val receivers = if (broadcast) telegramService.allowedChats else setOf(chatId)
        receivers.forEach { receiverId -> telegramService.sendMessage(receiverId, message) }
    }
}