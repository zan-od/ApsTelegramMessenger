package com.example.apstelegrammessenger

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.example.apstelegrammessenger.telegram.TelegramSettings
import java.util.stream.Collectors

class TelegramSettingsLoader(private val context: Context) {
    fun load(): TelegramSettings {
        val prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val allowedChats: Set<Long> = prefs.getStringSet("allowed_chats", emptySet())!!
            .stream().map { it.toLong() }
            .collect(Collectors.toSet())
            .toSet()

        return TelegramSettings(
            prefs.getString("app_id", "0")!!.toInt(),
            prefs.getString("app_hash", "")!!,
            context.filesDir.absolutePath,
            prefs.getString("phone_number", "")!!,
            allowedChats
        )
    }
}