package com.example.apstelegrammessenger.telegram.handler

import org.drinkless.td.libcore.telegram.TdApi
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateClosed
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateClosing
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateLoggingOut
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateReady
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitCode
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitEncryptionKey
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitOtherDeviceConfirmation
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitPassword
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitPhoneNumber
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitRegistration
import org.drinkless.td.libcore.telegram.TdApi.AuthorizationStateWaitTdlibParameters

class TdAuthorizationHandlerAdapter(private val handler: TdAuthorizationHandler) {
    private lateinit var lastAuthorizationState: TdApi.AuthorizationState

    fun onUpdate(authorizationState: TdApi.AuthorizationState) {
        lastAuthorizationState = authorizationState

        when (authorizationState.constructor) {
            AuthorizationStateWaitTdlibParameters.CONSTRUCTOR -> {
                handler.onWaitTdlibParameters()
            }

            AuthorizationStateWaitEncryptionKey.CONSTRUCTOR -> {
                handler.onWaitEncryptionKey()
            }

            AuthorizationStateWaitPhoneNumber.CONSTRUCTOR -> {
                handler.onWaitPhoneNumber()
            }

            AuthorizationStateWaitCode.CONSTRUCTOR -> {
                handler.onWaitCode()
            }

            AuthorizationStateWaitOtherDeviceConfirmation.CONSTRUCTOR -> {
                handler.onWaitOtherDeviceConfirmation()
            }

            AuthorizationStateWaitRegistration.CONSTRUCTOR -> {
                handler.onWaitRegistration()
            }

            AuthorizationStateWaitPassword.CONSTRUCTOR -> {
                handler.onWaitPassword()
            }

            AuthorizationStateReady.CONSTRUCTOR -> {
                handler.onStateReady()
            }

            AuthorizationStateLoggingOut.CONSTRUCTOR -> {
                handler.onLoggingOut()
            }

            AuthorizationStateClosing.CONSTRUCTOR -> {
                handler.onClosing()
            }

            AuthorizationStateClosed.CONSTRUCTOR -> {
                handler.onClosed()
            }
        }
    }

    fun onError(apiObject: TdApi.Object) {
        // repeat latest action
        if (lastAuthorizationState != null) {
            onUpdate(lastAuthorizationState)
        }
    }
}