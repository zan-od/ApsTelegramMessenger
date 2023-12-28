package com.example.apstelegrammessenger.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.apstelegrammessenger.ApsTelegramMessengerApplication
import com.example.apstelegrammessenger.R
import com.example.apstelegrammessenger.telegram.Chat
import com.example.apstelegrammessenger.telegram.TelegramService
import java.util.stream.Collectors

class SettingsActivity : AppCompatActivity() {
    private lateinit var telegramService: TelegramService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        telegramService = (application as ApsTelegramMessengerApplication).telegramService

        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment(telegramService))
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment(private val telegramService: TelegramService) : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            val allowedChatsPreferenceKey = "allowed_chats"
            val allowedChatsPreference: MultiSelectListPreference = findPreference(
                allowedChatsPreferenceKey
            )!!
            initChatList(allowedChatsPreference)

            allowedChatsPreference.setOnPreferenceClickListener { preference ->
                if ((preference is MultiSelectListPreference) && (preference.key == allowedChatsPreferenceKey)) {
                    updateChatList(preference)
                    return@setOnPreferenceClickListener true
                }

                false
            }

            allowedChatsPreference.summaryProvider =
                Preference.SummaryProvider { preference: MultiSelectListPreference ->
                    preference.values.joinToString(", ")
                }

            allowedChatsPreference.onPreferenceChangeListener =
                Preference.OnPreferenceChangeListener { _, newValue ->
                    allowedChatsPreference.values = (newValue as Set<String>).stream()
                        .filter { allowedChatsPreference.entryValues.contains(it) }
                        .collect(Collectors.toSet())
                    false
                }
        }

        private fun initChatList(allowedChatsPreference: MultiSelectListPreference) {
            allowedChatsPreference.entries = emptyArray()
            allowedChatsPreference.entryValues = emptyArray()
        }

        private fun updateChatList(allowedChatsPreference: MultiSelectListPreference) {
            val chats: List<Chat> = telegramService.getChats()

            val entries = arrayOfNulls<CharSequence>(chats.size)
            val entryValues = arrayOfNulls<CharSequence>(chats.size)
            for (i in chats.indices) {
                entries[i] = chats[i].name
                entryValues[i] = chats[i].id.toString()
            }
            allowedChatsPreference.entries = entries
            allowedChatsPreference.entryValues = entryValues
        }
    }
}