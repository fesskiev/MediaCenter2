package com.fesskiev.mediacenter.di

import android.content.Context
import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.engines.ExoPlayerEngine

import com.fesskiev.mediacenter.utils.NotificationUtils
import com.fesskiev.mediacenter.utils.PermissionsUtils
import com.fesskiev.mediacenter.utils.player.MediaPlayer
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule {

    @Provides
    @Singleton
    fun providePermissionsUtils() : PermissionsUtils {
        return PermissionsUtils()
    }

    @Provides
    @Singleton
    fun provideNotificationUtils(context: Context) : NotificationUtils {
        return NotificationUtils(context)
    }

    @Provides
    @Singleton
    fun provideMediaPlayer(fFmpegEngine: FFmpegEngine, superpoweredEngine: SuperpoweredEngine,
                           exoPlayerEngine: ExoPlayerEngine) : MediaPlayer {
        return MediaPlayer(fFmpegEngine, superpoweredEngine, exoPlayerEngine)
    }
}