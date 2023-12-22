package com.example.apstelegrammessenger.telegram.handler

import org.drinkless.td.libcore.telegram.TdApi.*

interface TdUpdateHandler {
    fun authorizationStateUpdated(authorizationState: AuthorizationState) {}
    fun userUpdated(user: User) {}
    fun userStatusUpdated(updateUserStatus: UpdateUserStatus) {}
    fun basicGroupUpdated(updateBasicGroup: UpdateBasicGroup) {}
    fun supergroupUpdated(updateSupergroup: UpdateSupergroup) {}
    fun secretChatUpdated(updateSecretChat: UpdateSecretChat) {}
    fun newChatUpdated(updateNewChat: UpdateNewChat) {}
    fun chatTitleUpdated(updateChatTitle: UpdateChatTitle) {}
    fun chatPhotoUpdated(updateChatPhoto: UpdateChatPhoto) {}
    fun chatLastMessageUpdated(updateChatLastMessage: UpdateChatLastMessage) {}
    fun chatPositionUpdated(updateChatPosition: UpdateChatPosition) {}
    fun chatReadInboxUpdated(updateChatReadInbox: UpdateChatReadInbox) {}
    fun chatReadOutboxUpdated(updateChatReadOutbox: UpdateChatReadOutbox) {}
    fun chatUnreadMentionCountUpdated(updateChatUnreadMentionCount: UpdateChatUnreadMentionCount) {}
    fun messageMentionReadUpdated(updateMessageMentionRead: UpdateMessageMentionRead) {}
    fun chatReplyMarkupUpdated(updateChatReplyMarkup: UpdateChatReplyMarkup) {}
    fun chatDraftMessageUpdated(updateChatDraftMessage: UpdateChatDraftMessage) {}
    fun chatPermissionsUpdated(updateChatPermissions: UpdateChatPermissions) {}
    fun chatNotificationSettingsUpdated(updateChatNotificationSettings: UpdateChatNotificationSettings) {}
    fun chatDefaultDisableNotificationUpdated(updateChatDefaultDisableNotification: UpdateChatDefaultDisableNotification) {}
    fun chatIsMarkedAsUnreadUpdated(updateChatIsMarkedAsUnread: UpdateChatIsMarkedAsUnread) {}
    fun chatIsBlockedUpdated(UpdateChatIsBlocked: UpdateChatIsBlocked) {}
    fun chatHasScheduledMessagesUpdated(updateChatHasScheduledMessages: UpdateChatHasScheduledMessages) {}
    fun userFullInfoUpdated(updateUserFullInfo: UpdateUserFullInfo) {}
    fun basicGroupFullInfoUpdated(updateBasicGroupFullInfo: UpdateBasicGroupFullInfo) {}
    fun supergroupFullInfoUpdated(updateSupergroupFullInfo: UpdateSupergroupFullInfo) {}
    fun onUnknownUpdate(apiObject: Object?) {}
}