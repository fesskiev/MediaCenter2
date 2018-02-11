package com.fesskiev.mediacenter.utils

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.domain.entity.media.MediaFolder




@TargetApi(Build.VERSION_CODES.O)
class NotificationUtils(private var context: Context) {

    companion object {

        private const val CONTROL_CHANNEL = "notification_channel_control"
        private const val SCAN_CHANNEL = "notification_channel_scan"

        private const val NOTIFICATION_SCAN_ID = 411
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

    fun createScanNotification() {
        notificationBuilder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(context, SCAN_CHANNEL)
        } else {
            NotificationCompat.Builder(context)
        }
        notificationBuilder?.color = ContextCompat.getColor(context, R.color.primary_light)
        notificationBuilder?.setSmallIcon(R.drawable.ic_scan)
        notificationBuilder?.setVisibility(Notification.VISIBILITY_PUBLIC)
        notificationBuilder?.setContentTitle(context.getString(R.string.scan_notification_title))
        notificationBuilder?.setContentText(context.getString(R.string.scan_notification_message))
        notificationBuilder?.setOnlyAlertOnce(true)
        notificationBuilder?.setAutoCancel(false)
        notificationBuilder?.setWhen(System.currentTimeMillis())
        notificationBuilder?.setShowWhen(true)
        notificationManager?.notify(NOTIFICATION_SCAN_ID, notificationBuilder?.build())
    }

    fun updateScanNotification(mediaFolder: MediaFolder?, mediaFile: MediaFile?, progress: Int) {
        notificationBuilder?.setContentTitle(mediaFolder?.getFolderName())
        notificationBuilder?.setContentText(mediaFile?.getTitle())
        notificationBuilder?.setProgress(100, progress, false)
        notificationManager?.notify(NOTIFICATION_SCAN_ID, notificationBuilder?.build())
    }

    fun removeScanNotification() {
        notificationManager?.cancel(NOTIFICATION_SCAN_ID)
    }
}