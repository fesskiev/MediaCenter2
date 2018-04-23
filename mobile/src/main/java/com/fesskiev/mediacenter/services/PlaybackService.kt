package com.fesskiev.mediacenter.services

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import com.fesskiev.mediacenter.receivers.HeadsetReceiver
import com.fesskiev.mediacenter.ui.main.MainActivity
import com.fesskiev.mediacenter.utils.NotificationUtils
import com.fesskiev.mediacenter.utils.NotificationUtils.Companion.ACTION_CLOSE_APP
import com.fesskiev.mediacenter.utils.NotificationUtils.Companion.ACTION_MEDIA_CONTROL_NEXT
import com.fesskiev.mediacenter.utils.NotificationUtils.Companion.ACTION_MEDIA_CONTROL_PAUSE
import com.fesskiev.mediacenter.utils.NotificationUtils.Companion.ACTION_MEDIA_CONTROL_PLAY
import com.fesskiev.mediacenter.utils.NotificationUtils.Companion.ACTION_MEDIA_CONTROL_PREVIOUS
import com.fesskiev.mediacenter.utils.player.MediaPlayer
import dagger.android.DaggerService
import javax.inject.Inject

class PlaybackService : DaggerService() {

    companion object {

        private const val ACTION_START_PLAYBACK_SERVICE = "action.START_PLAYBACK_SERVICE"
        const val ACTION_FINISH_APP = "action.ACTION_FINISH_APP"

        fun startPlaybackService(context: Context) {
            val intent = Intent(context, PlaybackService::class.java)
            intent.action = ACTION_START_PLAYBACK_SERVICE
            context.startService(intent)
        }
    }

    @Inject
    @JvmField
    var mediaPlayer: MediaPlayer? = null
    @Inject
    @JvmField
    var receiver: HeadsetReceiver? = null
    @Inject
    @JvmField
    var notificationUtils: NotificationUtils? = null

    override fun onCreate() {
        super.onCreate()
        receiver?.register()
        val notification = notificationUtils?.updatePlaybackNotification(null, null)
        startForeground(NotificationUtils.NOTIFICATION_ID, notification)
        registerNotificationReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.shutdown()
        receiver?.unregister()
        notificationUtils?.stopPlaybackNotification()
        unregisterNotificationReceiver()
    }

    private fun registerNotificationReceiver() {
        val filter = IntentFilter()
        filter.addAction(ACTION_MEDIA_CONTROL_PLAY)
        filter.addAction(ACTION_MEDIA_CONTROL_PAUSE)
        filter.addAction(ACTION_MEDIA_CONTROL_NEXT)
        filter.addAction(ACTION_MEDIA_CONTROL_PREVIOUS)
        filter.addAction(ACTION_CLOSE_APP)
        registerReceiver(notificationReceiver, filter)
    }

    private fun unregisterNotificationReceiver() {
        unregisterReceiver(notificationReceiver)
    }

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    ACTION_MEDIA_CONTROL_PLAY -> mediaPlayer?.play()
                    ACTION_MEDIA_CONTROL_PAUSE -> mediaPlayer?.pause()
                    ACTION_MEDIA_CONTROL_PREVIOUS -> mediaPlayer?.previous()
                    ACTION_MEDIA_CONTROL_NEXT -> mediaPlayer?.next()
                    ACTION_CLOSE_APP -> closeApp()
                }
            }
        }
    }

    private fun closeApp() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        intent.action = ACTION_FINISH_APP
        startActivity(intent)
        stopSelf()
    }

    override fun onBind(intent: Intent?): IBinder {
        throw NotImplementedError()
    }
}