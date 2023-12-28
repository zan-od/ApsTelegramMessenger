package com.example.apstelegrammessenger

import android.content.Context
import android.content.Intent
import com.example.apstelegrammessenger.Intents.Companion.COMMAND_TO_APS
import com.example.apstelegrammessenger.telegram.command.CommandHandler

class TelegramCommandHandler(
    private val context: Context
): CommandHandler {

    override fun handle(chatId: Long, message: String) {
        val sendIntent: Intent = Intent().apply {
            action = COMMAND_TO_APS
            putExtra("user", chatId.toString())
            putExtra("text", message)
        }
        sendIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        sendIntent.setPackage("info.nightscout.androidaps")
        context.sendBroadcast(sendIntent)
    }
}