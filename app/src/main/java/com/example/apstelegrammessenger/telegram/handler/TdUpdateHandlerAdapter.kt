package com.example.apstelegrammessenger.telegram.handler

import org.drinkless.td.libcore.telegram.Client
import org.drinkless.td.libcore.telegram.TdApi

class TdUpdateHandlerAdapter(private val handler: TdUpdateHandler) : Client.ResultHandler {
    override fun onResult(apiObject: TdApi.Object?) {
        when (apiObject?.constructor) {
            TdApi.UpdateAuthorizationState.CONSTRUCTOR -> {
                handler.authorizationStateUpdated((apiObject as TdApi.UpdateAuthorizationState).authorizationState)
            }

            TdApi.UpdateUser.CONSTRUCTOR -> {
                handler.userUpdated((apiObject as TdApi.UpdateUser).user)
            }

            TdApi.UpdateUserStatus.CONSTRUCTOR -> {
                handler.userStatusUpdated(apiObject as TdApi.UpdateUserStatus)
            }

            TdApi.UpdateBasicGroup.CONSTRUCTOR -> {
                handler.basicGroupUpdated(apiObject as TdApi.UpdateBasicGroup)
            }

            TdApi.UpdateSupergroup.CONSTRUCTOR -> {
                handler.supergroupUpdated(apiObject as TdApi.UpdateSupergroup)
            }

            TdApi.UpdateSecretChat.CONSTRUCTOR -> {
                handler.secretChatUpdated(apiObject as TdApi.UpdateSecretChat)
            }

            TdApi.UpdateNewChat.CONSTRUCTOR -> {
                handler.newChatUpdated(apiObject as TdApi.UpdateNewChat)
            }

            TdApi.UpdateChatTitle.CONSTRUCTOR -> {
                handler.chatTitleUpdated(apiObject as TdApi.UpdateChatTitle)
            }

            TdApi.UpdateChatPhoto.CONSTRUCTOR -> {
                handler.chatPhotoUpdated(apiObject as TdApi.UpdateChatPhoto)
            }

            TdApi.UpdateChatLastMessage.CONSTRUCTOR -> {
                handler.chatLastMessageUpdated(apiObject as TdApi.UpdateChatLastMessage)
            }

            TdApi.UpdateChatPosition.CONSTRUCTOR -> {
                handler.chatPositionUpdated(apiObject as TdApi.UpdateChatPosition)
            }

            TdApi.UpdateChatReadInbox.CONSTRUCTOR -> {
                handler.chatReadInboxUpdated(apiObject as TdApi.UpdateChatReadInbox)
            }

            TdApi.UpdateChatReadOutbox.CONSTRUCTOR -> {
                handler.chatReadOutboxUpdated(apiObject as TdApi.UpdateChatReadOutbox)
            }

            TdApi.UpdateChatUnreadMentionCount.CONSTRUCTOR -> {
                handler.chatUnreadMentionCountUpdated(apiObject as TdApi.UpdateChatUnreadMentionCount)
            }

            TdApi.UpdateMessageMentionRead.CONSTRUCTOR -> {
                handler.messageMentionReadUpdated(apiObject as TdApi.UpdateMessageMentionRead)
            }

            TdApi.UpdateChatReplyMarkup.CONSTRUCTOR -> {
                handler.chatReplyMarkupUpdated(apiObject as TdApi.UpdateChatReplyMarkup)
            }

            TdApi.UpdateChatDraftMessage.CONSTRUCTOR -> {
                handler.chatDraftMessageUpdated(apiObject as TdApi.UpdateChatDraftMessage)
            }

            TdApi.UpdateChatPermissions.CONSTRUCTOR -> {
                handler.chatPermissionsUpdated(apiObject as TdApi.UpdateChatPermissions)
            }

            TdApi.UpdateChatNotificationSettings.CONSTRUCTOR -> {
                handler.chatNotificationSettingsUpdated(apiObject as TdApi.UpdateChatNotificationSettings)
            }

            TdApi.UpdateChatDefaultDisableNotification.CONSTRUCTOR -> {
                handler.chatDefaultDisableNotificationUpdated(apiObject as TdApi.UpdateChatDefaultDisableNotification)
            }

            TdApi.UpdateChatIsMarkedAsUnread.CONSTRUCTOR -> {
                handler.chatIsMarkedAsUnreadUpdated(apiObject as TdApi.UpdateChatIsMarkedAsUnread)
            }

            TdApi.UpdateChatIsBlocked.CONSTRUCTOR -> {
                handler.chatIsBlockedUpdated(apiObject as TdApi.UpdateChatIsBlocked)
            }

            TdApi.UpdateChatHasScheduledMessages.CONSTRUCTOR -> {
                handler.chatHasScheduledMessagesUpdated(apiObject as TdApi.UpdateChatHasScheduledMessages)
            }

            TdApi.UpdateUserFullInfo.CONSTRUCTOR -> {
                handler.userFullInfoUpdated(apiObject as TdApi.UpdateUserFullInfo)
            }

            TdApi.UpdateBasicGroupFullInfo.CONSTRUCTOR -> {
                handler.basicGroupFullInfoUpdated(apiObject as TdApi.UpdateBasicGroupFullInfo)
            }

            TdApi.UpdateSupergroupFullInfo.CONSTRUCTOR -> {
                handler.supergroupFullInfoUpdated(apiObject as TdApi.UpdateSupergroupFullInfo)
            }

            else -> {
                handler.onUnknownUpdate(apiObject)
            }
        }
    }
}