package com.Lokalan.application

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp  // ✅ INI YANG PENTING! Bukan @AndroidEntryPoint
class LokalanApp : Application() {
    // Optional: Bisa tambah kode lain kalo perlu
    override fun onCreate() {
        super.onCreate()
        // Setup lain kalo ada (Firebase, Analytics, dll)
    }
}