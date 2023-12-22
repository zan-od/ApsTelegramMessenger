package com.example.apstelegrammessenger.telegram.handler

interface TdAuthorizationHandler {
    fun onWaitTdlibParameters() {}
    fun onWaitEncryptionKey() {}
    fun onWaitPhoneNumber() {}
    fun onWaitCode() {}
    fun onWaitOtherDeviceConfirmation() {}
    fun onWaitRegistration() {}
    fun onWaitPassword() {}
    fun onStateReady() {}
    fun onLoggingOut() {}
    fun onClosing() {}
    fun onClosed() {}
}