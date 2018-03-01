package com.fesskiev.mediacenter.services

import android.content.Intent
import android.os.IBinder
import dagger.android.DaggerService

class PlaybackService : DaggerService() {

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder {
        throw NotImplementedError()
    }
}