package com.example.apstelegrammessenger

import android.app.Application
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.example.apstelegrammessenger.telegram.TelegramService
import com.example.apstelegrammessenger.telegram.command.CommandHandler
import com.example.apstelegrammessenger.telegram.command.ResponseHandler

class ApsTelegramMessengerApplication: Application() {
    lateinit var telegramService: TelegramService
    lateinit var settingsLoader: TelegramSettingsLoader
    lateinit var apsDataReceiver: ApsDataReceiver
    lateinit var commandHandler: TelegramCommandHandler

    private lateinit var responseHandler: ResponseHandler

    override fun onCreate() {
        super.onCreate()

        commandHandler = TelegramCommandHandler(applicationContext)
        settingsLoader = TelegramSettingsLoader(applicationContext)
        telegramService = TelegramService(commandHandler)
        responseHandler = ApsResponseHandler(telegramService)
        apsDataReceiver = ApsDataReceiver(responseHandler)

        telegramService.updateSettings(settingsLoader.load())
        telegramService.start()
        ContextCompat.registerReceiver(applicationContext, apsDataReceiver,
            IntentFilter(Intents.RESPONSE_FROM_APS), ContextCompat.RECEIVER_EXPORTED)
    }

    override fun onTerminate() {
        telegramService.stop()
        unregisterReceiver(apsDataReceiver)

        super.onTerminate()
    }
}