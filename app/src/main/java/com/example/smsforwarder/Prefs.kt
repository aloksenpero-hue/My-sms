package com.example.smsforwarder

import android.content.Context

object Prefs {
    private const val NAME = "sms_forwarder"

    fun save(ctx: Context, token: String, chatId: String, enabled: Boolean) {
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).edit()
            .putString("token", token.trim())
            .putString("chat_id", chatId.trim())
            .putBoolean("enabled", enabled)
            .apply()
    }

    fun token(ctx: Context): String =
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString("token", "") ?: ""

    fun chatId(ctx: Context): String =
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).getString("chat_id", "") ?: ""

    fun enabled(ctx: Context): Boolean =
        ctx.getSharedPreferences(NAME, Context.MODE_PRIVATE).getBoolean("enabled", true)
}
