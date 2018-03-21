package com.fesskiev.mediacenter.services

import android.content.Intent
import android.os.IBinder
import com.fesskiev.mediacenter.receivers.HeadsetReceiver
import dagger.android.DaggerService
import javax.inject.Inject

class PlaybackService : DaggerService() {

    @Inject
    @JvmField
    var receiver: HeadsetReceiver? = null

    override fun onCreate() {
        super.onCreate()
        receiver?.register()
    }

    override fun onDestroy() {
        super.onDestroy()
        receiver?.unregister()
    }

    override fun onBind(intent: Intent?): IBinder {
        throw NotImplementedError()
    }
}