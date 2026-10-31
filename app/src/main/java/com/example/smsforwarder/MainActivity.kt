package com.example.smsforwarder

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.Gravity
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    private lateinit var etToken: EditText
    private lateinit var etChat: EditText
    private lateinit var cbEnabled: CheckBox
    private lateinit var tvStatus: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val pad = (16 * resources.displayMetrics.density).toInt()
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(pad, pad, pad, pad)
        }

        root.addView(TextView(this).apply {
            text = "SMS → Telegram Forwarder"
            textSize = 20f
            gravity = Gravity.START
        })

        etToken = EditText(this).apply {
            hint = "Bot Token (dari @BotFather)"
            inputType = InputType.TYPE_CLASS_TEXT
            setText(Prefs.token(this@MainActivity))
        }
        root.addView(etToken)

        etChat = EditText(this).apply {
            hint = "Chat ID (mis. 123456789)"
            inputType = InputType.TYPE_CLASS_TEXT
            setText(Prefs.chatId(this@MainActivity))
        }
        root.addView(etChat)

        cbEnabled = CheckBox(this).apply {
            text = "Aktifkan penerusan otomatis"
            isChecked = Prefs.enabled(this@MainActivity)
        }
        root.addView(cbEnabled)

        val btnSave = Button(this).apply { text = "Simpan" }
        val btnPerm = Button(this).apply { text = "Beri izin SMS" }
        val btnTest = Button(this).apply { text = "Kirim pesan tes" }
        root.addView(btnSave)
        root.addView(btnPerm)
        root.addView(btnTest)

        tvStatus = TextView(this).apply { setPadding(0, pad, 0, 0) }
        root.addView(tvStatus)

        setContentView(ScrollView(this).apply { addView(root) })

        btnSave.setOnClickListener {
            save()
            Toast.makeText(this, "Tersimpan", Toast.LENGTH_SHORT).show()
            refreshStatus()
        }
        btnPerm.setOnClickListener { requestPerms() }
        btnTest.setOnClickListener {
            save()
            val token = Prefs.token(this)
            val chat = Prefs.chatId(this)
            if (token.isEmpty() || chat.isEmpty()) {
                Toast.makeText(this, "Isi token dan chat ID dulu", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            tvStatus.text = "Mengirim tes..."
            Thread {
                val (ok, info) = Telegram.send(token, chat, "✅ Tes dari SMS Forwarder")
                runOnUiThread {
                    tvStatus.text = if (ok) "Tes berhasil terkirim." else "Gagal: $info"
                }
            }.start()
        }

        requestPerms()
        refreshStatus()
    }

    private fun save() {
        Prefs.save(this, etToken.text.toString(), etChat.text.toString(), cbEnabled.isChecked)
    }

    private fun requestPerms() {
        if (Build.VERSION.SDK_INT >= 23) {
            val need = arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS)
                .filter { checkSelfPermission(it) != PackageManager.PERMISSION_GRANTED }
            if (need.isNotEmpty()) requestPermissions(need.toTypedArray(), 1)
        }
    }

    override fun onRequestPermissionsResult(code: Int, perms: Array<out String>, res: IntArray) {
        super.onRequestPermissionsResult(code, perms, res)
        refreshStatus()
    }

    private fun refreshStatus() {
        val granted = Build.VERSION.SDK_INT < 23 ||
            checkSelfPermission(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
        tvStatus.text = "Izin SMS: " + (if (granted) "diberikan ✅" else "belum diberikan ❌")
    }
}
