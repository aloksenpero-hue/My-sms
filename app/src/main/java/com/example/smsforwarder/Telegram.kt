package com.example.smsforwarder

import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

object Telegram {
    /** Kirim pesan teks ke Telegram. Harus dipanggil dari thread background. */
    fun send(token: String, chatId: String, text: String): Pair<Boolean, String> {
        return try {
            val body = "chat_id=" + URLEncoder.encode(chatId, "UTF-8") +
                "&text=" + URLEncoder.encode(text, "UTF-8")
            val conn = URL("https://api.telegram.org/bot$token/sendMessage")
                .openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.connectTimeout = 15000
            conn.readTimeout = 15000
            conn.doOutput = true
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
            conn.outputStream.use { it.write(body.toByteArray()) }
            val code = conn.responseCode
            val stream = if (code in 200..299) conn.inputStream else conn.errorStream
            val resp = stream?.bufferedReader()?.readText() ?: ""
            conn.disconnect()
            Pair(code in 200..299, "HTTP $code $resp")
        } catch (e: Exception) {
            Pair(false, e.message ?: "error")
        }
    }
}
