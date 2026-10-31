# SMS → Telegram Forwarder

Aplikasi Android sederhana (Kotlin native, tanpa library tambahan) yang meneruskan
semua SMS masuk ke bot Telegram secara otomatis.

## Cara mendapatkan APK

1. Buat repository GitHub baru, upload seluruh isi folder ini.
2. Buka tab **Actions** → jalankan workflow **Build APK** (atau otomatis saat push ke `main`).
3. Setelah selesai, unduh artifact `sms-forwarder-apk` → ekstrak → pasang `app-release.apk`.

Atau build lokal dengan Android Studio / `gradle assembleRelease` (JDK 17 + Android SDK).

## Menyiapkan Telegram

1. Chat **@BotFather** → `/newbot` → salin **Bot Token**.
2. Kirim pesan apa saja ke bot Anda, lalu buka
   `https://api.telegram.org/bot<TOKEN>/getUpdates` dan ambil angka `chat.id`.

## Memakai aplikasi

1. Buka aplikasi, isi Bot Token dan Chat ID, tekan **Simpan**.
2. Izinkan permission **SMS** (RECEIVE_SMS, READ_SMS) saat diminta.
3. Tekan **Kirim pesan tes** untuk memastikan koneksi berhasil.
4. Selesai — setiap SMS masuk otomatis diteruskan.

## Catatan

- Di beberapa HP (Xiaomi, Oppo, Vivo, Samsung) aktifkan **Autostart** dan matikan
  **penghemat baterai** untuk aplikasi ini agar penerusan tetap jalan.
- Di Android 13+ dari sideload, jika izin SMS diblokir: Setelan → Aplikasi → SMS Forwarder →
  menu ⋮ → **Izinkan setelan terbatas**, lalu beri izin SMS.
- Token bot disimpan lokal di perangkat. Gunakan hanya untuk SMS milik Anda sendiri.
