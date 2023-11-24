package com.example.apstelegrammessenger.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.MultiSelectListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.apstelegrammessenger.R
import java.util.stream.Collectors
import kotlin.random.Random

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.settings_activity)
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        data class Chat(val id:Long, val name:String)
        private val random = Random(0)

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
            val chats: MutableList<Chat> = ArrayList()
            val num = random.nextInt(100)
            for (i in 0..num) {
                chats.add(Chat(i.toLong(), "Item $i"))
            }

            val entries = arrayOfNulls<CharSequence>(chats.size)
            val entryValues = arrayOfNulls<CharSequence>(chats.size)
            for (i in 0..<chats.size) {
                entries[i] = chats[i].name
                entryValues[i] = chats[i].id.toString()
            }
            allowedChatsPreference.entries = entries
            allowedChatsPreference.entryValues = entryValues
        }
    }
}