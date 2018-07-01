package com.fesskiev.mediacenter.receivers

import com.fesskiev.mediacenter.utils.player.MediaPlayer
import android.content.Intent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.IntentFilter

class HeadsetReceiver(private val context: Context, private val mediaPlayer: MediaPlayer) {

    private val receiver : HeadsetBroadcastReceiver = HeadsetBroadcastReceiver()

    fun register(){
        val filter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
        context.registerReceiver(receiver, filter)
    }

    fun unregister(){
        context.unregisterReceiver(receiver)
    }

    private inner class HeadsetBroadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action.equals(Intent.ACTION_HEADSET_PLUG)) {
                val state = intent?.getIntExtra("state", -1)
                when (state) {
                    0 -> unplugged()
                    1 -> plugged()
                }
            }
        }
    }

    private fun unplugged() {
        mediaPlayer.pause()
    }

    private fun plugged() {
        mediaPlayer.play()
    }
}