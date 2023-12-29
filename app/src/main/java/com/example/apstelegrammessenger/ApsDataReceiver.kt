package com.example.apstelegrammessenger

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.apstelegrammessenger.telegram.command.ResponseHandler
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject

class ApsDataReceiver(
    private val responseHandler: ResponseHandler
): BroadcastReceiver() {
    private val logSubject: PublishSubject<String> = PublishSubject.create()

    fun subscribeToLogUpdates(handler: (String) -> Unit): Disposable {
        return logSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(handler)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        when (intent?.action) {
            Intents.RESPONSE_FROM_APS ->  {
                var chatId = intent.getStringExtra("user")?.toLong()
                val message = intent.getStringExtra("text").orEmpty()
                val broadcast = intent.getStringExtra("broadcast") == "true"

                logSubject.onNext("APS >>> : ($chatId, $message, $broadcast)" )
                if (chatId == null) {
                    if (broadcast) {
                        chatId = 0
                    } else {
                        logSubject.onNext("Incorrect message received - empty chat id" )
                        return
                    }
                }
                if (message.isBlank()) {
                    logSubject.onNext("Incorrect message received - empty message" )
                    return
                }

                responseHandler.handle(chatId, message, broadcast)
            }
            else -> {
                logSubject.onNext("Unknown intent ${intent?.action}-${intent.toString()}")
            }
        }
    }
}