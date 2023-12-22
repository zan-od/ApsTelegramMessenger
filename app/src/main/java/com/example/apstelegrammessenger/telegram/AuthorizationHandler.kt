package com.example.apstelegrammessenger.telegram

import com.example.apstelegrammessenger.telegram.handler.TdAuthorizationHandler
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi


class AuthorizationHandler(val service: TelegramService): TdAuthorizationHandler {
    private val resultHandler = AuthorizationResultHandler()

    override fun onWaitTdlibParameters() {
        val parameters = TdApi.TdlibParameters(
            false,
            service.filesDir + "/tg_db",
            service.filesDir + "/tg_files",
            false,
            false,
            false,
            false,
            service.appId,
            service.appHash,
            "en",
            "Android-Test",
            "test",
            "test",
            false,
            false
        )

        service.client.send(TdApi.SetTdlibParameters(parameters), resultHandler)
    }

    override fun onWaitEncryptionKey() {
        service.client.send(TdApi.CheckDatabaseEncryptionKey(ByteArray(0)), resultHandler)
    }

    override fun onWaitPhoneNumber() {
        service.client.send(TdApi.SetAuthenticationPhoneNumber(service.phoneNumber, null), resultHandler)
    }

    override fun onWaitCode() {
        val code = "" //TODO get code from UI
        //service.client.send(TdApi.CheckAuthenticationCode(code), resultHandler)
    }

    private class AuthorizationResultHandler : Client.ResultHandler {
        override fun onResult(apiObject: TdApi.Object) {
            when (apiObject.constructor) {
                TdApi.Error.CONSTRUCTOR -> {
                    System.err.println("Receive an error:\n$apiObject")
                    //onAuthorizationStateUpdated(null) // repeat last action
                }

                TdApi.Ok.CONSTRUCTOR -> {}
                else -> System.err.println("Receive wrong response from TDLib:\n" +
                        "$apiObject")
            }
        }
    }
}