package com.example.apstelegrammessenger.telegram

import com.example.apstelegrammessenger.telegram.handler.TdAuthorizationHandlerAdapter
import com.example.apstelegrammessenger.telegram.handler.TdUpdateHandler
import com.example.apstelegrammessenger.telegram.handler.TdUpdateHandlerAdapter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class TelegramService {
    private val chats: MutableList<Chat> = mutableListOf()
    private lateinit var allowedChats: Set<Long>

    private val authorizationStateSubject: PublishSubject<TdApi.AuthorizationState> = PublishSubject.create()
    private val logSubject: PublishSubject<String> = PublishSubject.create()

    var appId: Int = 0
        private set
    var appHash: String = ""
        private set
    var filesDir: String = ""
        private set
    var phoneNumber = ""
        private set

    private val resultHandler: Client.ResultHandler = ResultHandler()
    private val exceptionHandler: Client.ExceptionHandler = ExceptionHandler()
    var client: Client? = null

    private fun uiLog(message: String) {
        logSubject.onNext(message);
    }

    fun subscribeToLogUpdates(handler: (String) -> Unit): Disposable {
        return logSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(handler)
    }

    fun start() {
        client = Client.create(TdUpdateHandlerAdapter(UpdateHandler(this)), exceptionHandler, exceptionHandler)
        uiLog("Service started")
    }

    fun stop() {
        client?.close()
        client = null
        uiLog("Service stopped")
    }

    fun isStarted(): Boolean = client != null

    fun assertSettingsLoaded(): Boolean {
        if (appId == 0) {
            uiLog("Error! App Id is not set")
            return false
        } else if (appHash.isBlank()) {
            uiLog("Error! App Hash is not set")
            return false
        } else if (filesDir.isBlank()) {
            uiLog("Error! Files Dir is not set")
            return false
        } else if (phoneNumber.isBlank()) {
            uiLog("Error! Phone Number is not set")
            return false
        } else if (allowedChats.isEmpty()) {
            uiLog("Warning! Allowed Chats list is empty")
        }

        return true
    }

    fun loadSettings(settings: TelegramSettings) {
        appId = settings.appId
        appHash = settings.appHash
        filesDir = settings.filesDir
        phoneNumber = settings.phoneNumber
        allowedChats = settings.allowedChats
    }

    fun getChats():List<Chat> {
        return chats.toList()
    }

    fun updateChats() {
        chats.clear()
        client!!.send(TdApi.LoadChats(TdApi.ChatListMain(), 1000), resultHandler)
    }

    fun sendMessage(chatId: Long, message: String) {
        val content: TdApi.InputMessageContent =
            TdApi.InputMessageText(TdApi.FormattedText(message, null), false, true)
        client!!.send(TdApi.SendMessage(chatId, 0, 0, null, null, content), resultHandler)
    }

    fun sendAuthenticationCode(code: String) {
        client!!.send(TdApi.CheckAuthenticationCode(code), resultHandler)
    }

    fun subscribeToAuthorizationUpdates(handler: (TdApi.AuthorizationState) -> Unit): Disposable {
        return authorizationStateSubject
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(handler)
    }

    class UpdateHandler(val service: TelegramService): TdUpdateHandler {
        private val authorizationAdapter: TdAuthorizationHandlerAdapter = TdAuthorizationHandlerAdapter(
            AuthorizationHandler(service)
        )

        override fun authorizationStateUpdated(authorizationState: TdApi.AuthorizationState) {
            service.authorizationStateSubject.onNext(authorizationState)
            authorizationAdapter.onUpdate(authorizationState)
        }

        override fun newChatUpdated(updateNewChat: TdApi.UpdateNewChat) {
            val chat = updateNewChat.chat
            service.chats.add(Chat(chat.id, chat.title))
        }
    }

    class ExceptionHandler : Client.ExceptionHandler {
        override fun onException(e: Throwable?) {
            println("Default handler:${e.toString()}")
            e?.printStackTrace()
        }
    }

    inner class ResultHandler : Client.ResultHandler {
        override fun onResult(apiObject: TdApi.Object?) {
            when (apiObject?.constructor) {
                TdApi.Error.CONSTRUCTOR -> {
                    uiLog("$apiObject")
                }

                TdApi.Ok.CONSTRUCTOR -> {
                    uiLog("$apiObject")
                }

                else -> System.err.println("Receive wrong response from TDLib:\n" +
                        "$apiObject")
            }
        }
    }

    companion object {
        private val INSTANCE:TelegramService = TelegramService()
        fun getInstance():TelegramService {
            return INSTANCE
        }
    }
}