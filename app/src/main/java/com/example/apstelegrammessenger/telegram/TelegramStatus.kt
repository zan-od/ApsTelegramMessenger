package com.example.apstelegrammessenger.telegram

enum class TelegramStatus(val description: String) {
    CONNECTED("Connected"),
    STARTING("Starting..."),
    STOPPED("Stopped"),
    WAITING_AUTH("Need authentication"),
    ERROR("Error")
}