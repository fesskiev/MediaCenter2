package com.fesskiev.mediacenter.services

import android.app.Service
import android.content.Intent
import android.os.IBinder
import dagger.android.AndroidInjection

class PlaybackService : Service() {

    override fun onCreate() {
        super.onCreate()
        AndroidInjection.inject(this)
    }

    override fun onBind(intent: Intent?): IBinder {
        throw NotImplementedError()
    }
}