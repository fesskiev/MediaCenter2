package com.fesskiev.mediacenter.services

import android.app.Service
import android.content.Intent
import android.os.IBinder


class ScanSystemService : Service() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder {
        throw NotImplementedError()
    }
}