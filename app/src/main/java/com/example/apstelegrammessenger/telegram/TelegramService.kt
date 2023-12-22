package com.example.apstelegrammessenger.telegram

import com.example.apstelegrammessenger.telegram.handler.TdAuthorizationHandlerAdapter
import com.example.apstelegrammessenger.telegram.handler.TdUpdateHandler
import com.example.apstelegrammessenger.telegram.handler.TdUpdateHandlerAdapter
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.subjects.PublishSubject
import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class TelegramService {
    private val chats: MutableList<Chat> = mutableListOf()
    private val allowedChats: MutableList<Long> = mutableListOf()

    private val authorizationStateSubject: PublishSubject<TdApi.AuthorizationState> = PublishSubject.create()

    var appId: Int = 0
        private set
    var appHash: String = ""
        private set
    var filesDir: String = ""
        private set
    var phoneNumber = ""
        private set

    private val exceptionHandler: Client.ExceptionHandler = ExceptionHandler()
    val client: Client = Client.create(TdUpdateHandlerAdapter(UpdateHandler(this)), exceptionHandler, exceptionHandler)

    fun getChats():List<Chat> {
        return chats.toList()
    }

    fun updateChats() {
        client.send(TdApi.LoadChats(TdApi.ChatListMain(), 1000)) {}
    }

    fun sendMessage(chatId: Long, message: String) {
        val content: TdApi.InputMessageContent =
            TdApi.InputMessageText(TdApi.FormattedText(message, null), false, true)
        client.send(TdApi.SendMessage(chatId, 0, 0, null, null, content)) {}
    }

    fun subscribeToUpdates(handler: (TdApi.AuthorizationState) -> Unit): Disposable {
        return authorizationStateSubject.subscribe(handler)
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

    companion object {
        private val INSTANCE:TelegramService = TelegramService()
        fun getInstance():TelegramService {
            return INSTANCE
        }
    }
}