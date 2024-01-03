package com.example.apstelegrammessenger

import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.method.ScrollingMovementMethod
import android.text.util.Linkify
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.apstelegrammessenger.databinding.ActivityMainBinding
import com.example.apstelegrammessenger.settings.SettingsActivity
import com.example.apstelegrammessenger.telegram.TelegramService
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.reactivex.rxjava3.disposables.Disposable
import java.text.SimpleDateFormat
import java.util.Date


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var telegramService: TelegramService
    private lateinit var settingsLoader: TelegramSettingsLoader

    private val formatter = SimpleDateFormat("dd.MM.yyyy HH:mm:ss")
    private lateinit var textLog: TextView
    private lateinit var textStatus: TextView
    private lateinit var logDisposable: Disposable
    private lateinit var authorizationDisposable: Disposable
    private lateinit var apsReceiverDisposable: Disposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val app = application as ApsTelegramMessengerApplication
        telegramService = app.telegramService
        settingsLoader = app.settingsLoader

        textLog = findViewById(R.id.textLog)
        textLog.movementMethod = ScrollingMovementMethod()
        textStatus = findViewById(R.id.textStatus)
        logDisposable = telegramService.subscribeToLogUpdates {
            uiLog(it)
        }
        authorizationDisposable = telegramService.subscribeToAuthorizationUpdates {
            uiLog(it.toString())
        }
        apsReceiverDisposable = app.commandHandler.subscribeToLogUpdates {
            uiLog(it)
        }
        apsReceiverDisposable = app.apsDataReceiver.subscribeToLogUpdates {
            uiLog(it)
        }
    }

    override fun onDestroy() {
        logDisposable.dispose()
        authorizationDisposable.dispose()

        super.onDestroy()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                val myIntent = Intent(this, SettingsActivity::class.java)

                this@MainActivity.startActivity(myIntent)

                return true
            }
            R.id.action_about -> {
                val appNameVersion = getString(R.string.app_name) + " " + BuildConfig.VERSION_NAME
                var message =
                    "${getString(R.string.app_name)}\n" +
                    "Version: ${BuildConfig.VERSION_NAME}\n" +
                    "A bridge between Android APS and Telegram\n" +
                    "Copyright 2024 Anatolii Zlatov (anatoly.zlatov@gmail.com)\n" +
                    "Project URL: https://github.com/zan-od/ApsTelegramMessenger"
                val messageSpanned = SpannableString(message)
                Linkify.addLinks(messageSpanned, Linkify.WEB_URLS)
                MaterialAlertDialogBuilder(this)
                    .setTitle(appNameVersion)
                    .setMessage(messageSpanned)
                    .setPositiveButton("OK", null)
                    .create().apply {
                        show()
                    }
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun uiLog(message: String) {
        textLog.append("${formatter.format(Date())}: $message\n")
    }

    fun startTelegram(button: View) {
        if (telegramService.isStarted()) {
            uiLog("Service already started")
            return
        }

        telegramService.start()
    }

    fun stopTelegram(button: View) {
        if (!telegramService.isStarted()) {
            uiLog("Service already stopped")
            return
        }

        telegramService.stop()
    }

    fun loadSettings(button: View) {
        if (telegramService.isStarted()) {
            uiLog("Service is running. Stop it before loading settings")
            return
        }

        telegramService.updateSettings(settingsLoader.load())
    }

    fun sendAuthCode(button: View) {
        if (!telegramService.isStarted()) {
            uiLog("Service is stopped")
            return
        }

        enterAuthCode()
    }

    fun sendTestMessage(button: View) {
        if (!telegramService.isStarted()) {
            uiLog("Service is stopped")
            return
        }

        if (telegramService.allowedChats.isEmpty()) {
            uiLog("Allowed Chats list is empty")
            return
        }

        telegramService.sendMessage(telegramService.allowedChats.first(), "test")
    }

    fun updateChats(button: View) {
        if (!telegramService.isStarted()) {
            uiLog("Service is stopped")
            return
        }

        telegramService.updateChats()
    }

    private fun enterAuthCode() {
        val alert: AlertDialog.Builder = AlertDialog.Builder(this)

        alert.setTitle("Auth code")
        alert.setMessage("Enter Telegram authentication code")

        val input = EditText(this)
        alert.setView(input)

        alert.setPositiveButton("Ok") { dialog, whichButton ->
            val code: String = input.text.toString()
            telegramService.sendAuthenticationCode(code)
        }

        alert.setNegativeButton("Cancel",
            DialogInterface.OnClickListener { dialog, whichButton ->
                // Canceled.
            })

        alert.show()
    }
}