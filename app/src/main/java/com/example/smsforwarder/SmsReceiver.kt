package com.example.smsforwarder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.provider.Telephony
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SmsReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Telephony.Sms.Intents.SMS_RECEIVED_ACTION) return
        if (!Prefs.enabled(context)) return

        val token = Prefs.token(context)
        val chatId = Prefs.chatId(context)
        if (token.isEmpty() || chatId.isEmpty()) return

        val messages = Telephony.Sms.Intents.getMessagesFromIntent(intent) ?: return
        if (messages.isEmpty()) return

        // Gabungkan SMS multi-bagian dari pengirim yang sama.
        val sender = messages[0].originatingAddress ?: "Unknown"
        val body = messages.joinToString("") { it.messageBody ?: "" }
        val time = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(Date())

        val text = "📩 SMS Baru\nDari: $sender\nWaktu: $time\n\n$body"

        val pending = goAsync()
        Thread {
            try {
                Telegram.send(token, chatId, text)
            } finally {
                pending.finish()
            }
        }.start()
    }
}
