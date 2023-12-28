package com.example.apstelegrammessenger

import android.app.Application
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.example.apstelegrammessenger.telegram.TelegramService
import com.example.apstelegrammessenger.telegram.command.CommandHandler
import com.example.apstelegrammessenger.telegram.command.ResponseHandler

class ApsTelegramMessengerApplication: Application() {
    private lateinit var commandHandler:CommandHandler
    lateinit var telegramService: TelegramService
    private lateinit var responseHandler: ResponseHandler
    private lateinit var apsDataReceiver: ApsDataReceiver

    override fun onCreate() {
        super.onCreate()

        commandHandler = TelegramCommandHandler(applicationContext)
        telegramService = TelegramService(commandHandler)
        responseHandler = ApsResponseHandler(telegramService)
        apsDataReceiver = ApsDataReceiver(responseHandler)

        //telegramService.loadSettings()
        //telegramService.start()
        ContextCompat.registerReceiver(applicationContext, apsDataReceiver,
            IntentFilter(Intents.RESPONSE_FROM_APS), ContextCompat.RECEIVER_EXPORTED)
    }

    override fun onTerminate() {
        telegramService.stop()
        unregisterReceiver(apsDataReceiver)

        super.onTerminate()
    }
}