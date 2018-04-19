package com.fesskiev.mediacenter.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import android.widget.RemoteViews
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.domain.entity.media.MediaFolder
import com.fesskiev.mediacenter.ui.main.MainActivity
import com.fesskiev.mediacenter.utils.player.MediaPlayer


@TargetApi(Build.VERSION_CODES.O)
class NotificationUtils(private val context: Context, private val mediaPlayer: MediaPlayer) {

    companion object {

        private const val CONTROL_CHANNEL = "notification_channel_control"
        private const val SCAN_CHANNEL = "notification_channel_scan"
        const val ACTION_STOP_SCAN = "action.STOP_SCAN"

        const val ACTION_MEDIA_CONTROL_PLAY = "action.MEDIA_CONTROL_PLAY"
        const val ACTION_MEDIA_CONTROL_PAUSE = "action.MEDIA_CONTROL_PAUSE"
        const val ACTION_MEDIA_CONTROL_NEXT = "action.MEDIA_CONTROL_NEXT"
        const val ACTION_MEDIA_CONTROL_PREVIOUS = "action.MEDIA_CONTROL_PREVIOUS"
        const val ACTION_CLOSE_APP = "action.CLOSE_APP"

        const val NOTIFICATION_SCAN_ID = 411
        const val NOTIFICATION_ID = 412
    }

    private var notificationManager: NotificationManager? = null
    private var notificationBuilder: NotificationCompat.Builder? = null

    init {
        notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelControl = NotificationChannel(CONTROL_CHANNEL,
                    context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
            channelControl.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            channelControl.enableVibration(false)
            channelControl.enableLights(false)
            channelControl.setShowBadge(false)
            channelControl.setSound(null, null)
            notificationManager?.createNotificationChannel(channelControl)

            val channelScan = NotificationChannel(SCAN_CHANNEL,
                    context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT)
            channelScan.lockscreenVisibility = NotificationCompat.VISIBILITY_PUBLIC
            channelScan.enableVibration(false)
            channelScan.enableLights(false)
            channelScan.setShowBadge(false)
            channelScan.setSound(Uri.parse("android.resource://" + context.packageName + "/" + R.raw.notification),
                    null)
            notificationManager?.createNotificationChannel(channelScan)
        }
    }

    fun updatePlaybackNotification(mediaFile: MediaFile?, bitmap: Bitmap?) : Notification? {
        val notification = createPlaybackNotification(mediaFile, bitmap)
        notificationManager?.notify(NOTIFICATION_ID, notification)
        return notification
    }

    fun stopPlaybackNotification() {
        notificationManager?.cancel(NOTIFICATION_ID)
    }

    private fun createPlaybackNotification(mediaFile: MediaFile?, bitmap: Bitmap?): Notification? {
        val artist: String
        val title: String
        if (mediaFile != null) {
            artist = mediaFile.getTitle()
            title = mediaFile.getTitle()
        } else {
            artist = context.getString(R.string.playback_control_track_not_selected)
            title = context.getString(R.string.playback_control_track_not_selected)
        }
        val notificationBigView = RemoteViews(context.packageName, R.layout.notification_playback_big_layout)
        val notificationView = RemoteViews(context.packageName, R.layout.notification_playback_layout)

        notificationBigView.setTextViewText(R.id.notificationArtist, artist)
        notificationBigView.setTextViewText(R.id.notificationTitle, title)
        notificationBigView.setImageViewBitmap(R.id.notificationCover, bitmap)

        notificationView.setTextViewText(R.id.notificationArtist, artist)
        notificationView.setTextViewText(R.id.notificationTitle, title)
        notificationView.setImageViewBitmap(R.id.notificationCover, bitmap)

        val notificationBuilder = NotificationCompat.Builder(context, CONTROL_CHANNEL)

        notificationBuilder
                .setCustomBigContentView(notificationBigView)
                .setCustomContentView(notificationView)
                .setSmallIcon(R.drawable.ic_scan)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setContentIntent(createContentIntent())

        notificationBigView.setOnClickPendingIntent(R.id.notificationNext, getPendingIntentAction(ACTION_MEDIA_CONTROL_NEXT))
        notificationBigView.setOnClickPendingIntent(R.id.notificationPrevious, getPendingIntentAction(ACTION_MEDIA_CONTROL_PREVIOUS))
        if (mediaPlayer.isPlaying()) {
            notificationBigView.setImageViewResource(R.id.notificationPlayPause, R.drawable.ic_play)
            notificationBigView.setOnClickPendingIntent(R.id.notificationPlayPause, getPendingIntentAction(ACTION_MEDIA_CONTROL_PLAY))
        } else {
            notificationBigView.setImageViewResource(R.id.notificationPlayPause, R.drawable.ic_pause)
            notificationBigView.setOnClickPendingIntent(R.id.notificationPlayPause, getPendingIntentAction(ACTION_MEDIA_CONTROL_PAUSE))
        }
        notificationBigView.setOnClickPendingIntent(R.id.notificationClose, getPendingIntentAction(ACTION_CLOSE_APP))

        notificationView.setOnClickPendingIntent(R.id.notificationNext, getPendingIntentAction(ACTION_MEDIA_CONTROL_NEXT))
        notificationView.setOnClickPendingIntent(R.id.notificationPrevious, getPendingIntentAction(ACTION_MEDIA_CONTROL_PREVIOUS))
        if (!mediaPlayer.isPlaying()) {
            notificationView.setImageViewResource(R.id.notificationPlayPause, R.drawable.ic_play)
            notificationView.setOnClickPendingIntent(R.id.notificationPlayPause, getPendingIntentAction(ACTION_MEDIA_CONTROL_PLAY))
        } else {
            notificationView.setImageViewResource(R.id.notificationPlayPause, R.drawable.ic_pause)
            notificationView.setOnClickPendingIntent(R.id.notificationPlayPause, getPendingIntentAction(ACTION_MEDIA_CONTROL_PAUSE))
        }
        return notificationBuilder.build()
    }

    fun createScanNotification(): Notification? {
        notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, SCAN_CHANNEL)
        } else {
            NotificationCompat.Builder(context)
        }

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP

        val notificationBigView = RemoteViews(context.packageName, R.layout.notification_scan_big_lyout)
        val notificationView = RemoteViews(context.packageName, R.layout.notification_scan_layout)

        notificationBigView.setTextViewText(R.id.notificationTitle, context.getString(R.string.scan_notification_title))
        notificationBigView.setTextViewText(R.id.notificationText, context.getString(R.string.scan_notification_message))
        notificationBigView.setOnClickPendingIntent(R.id.notificationClose, getPendingIntentAction(ACTION_STOP_SCAN))

        notificationView.setTextViewText(R.id.notificationTitle, context.getString(R.string.scan_notification_title))
        notificationView.setTextViewText(R.id.notificationText, context.getString(R.string.scan_notification_message))

        notificationBuilder?.color = ContextCompat.getColor(context, R.color.primary_light)
        notificationBuilder?.setSmallIcon(R.drawable.ic_scan)
        notificationBuilder?.setVisibility(Notification.VISIBILITY_PUBLIC)
        notificationBuilder?.setCustomBigContentView(notificationBigView)
        notificationBuilder?.setCustomContentView(notificationView)
        notificationBuilder?.setOnlyAlertOnce(true)
        notificationBuilder?.setAutoCancel(false)
        notificationBuilder?.setWhen(System.currentTimeMillis())
        notificationBuilder?.setShowWhen(true)
        notificationBuilder?.setContentIntent(PendingIntent.getActivity(context,
                System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT))
        val notification = notificationBuilder?.build()
        notificationManager?.notify(NOTIFICATION_SCAN_ID, notification)

        return notification
    }

    @SuppressLint("RestrictedApi")
    fun updateScanNotification(mediaFolder: MediaFolder?, mediaFile: MediaFile?, progress: Int) {

//        notificationBuilder?.bigContentView?.setTextViewText(R.id.notificationTitle, mediaFolder?.getFolderName())
//        notificationBuilder?.bigContentView?.setTextViewText(R.id.notificationText, mediaFile?.getTitle())
        notificationBuilder?.bigContentView?.setProgressBar(R.id.progressBar, 100, progress, false)
//
//        notificationBuilder?.contentView?.setTextViewText(R.id.notificationTitle, mediaFolder?.getFolderName())
//        notificationBuilder?.contentView?.setTextViewText(R.id.notificationText, mediaFile?.getTitle())

        notificationManager?.notify(NOTIFICATION_SCAN_ID, notificationBuilder?.build())
    }

    private fun getPendingIntentAction(action: String): PendingIntent {
        val intent = Intent(action)
        return PendingIntent.getBroadcast(context, System.currentTimeMillis().toInt(), intent, 0)
    }

    private fun createContentIntent(): PendingIntent {
        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
        return PendingIntent.getActivity(context, System.currentTimeMillis().toInt(), intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }
}