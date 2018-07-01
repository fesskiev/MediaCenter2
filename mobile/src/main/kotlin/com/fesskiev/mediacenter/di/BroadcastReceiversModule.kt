package com.fesskiev.mediacenter.di

import android.content.Context
import com.fesskiev.mediacenter.receivers.HeadsetReceiver
import com.fesskiev.mediacenter.utils.player.MediaPlayer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class BroadcastReceiversModule {

    @Provides
    @Singleton
    fun provideHeadsetReceiver(context: Context, mediaPlayer: MediaPlayer): HeadsetReceiver {
        return HeadsetReceiver(context, mediaPlayer)
    }
}