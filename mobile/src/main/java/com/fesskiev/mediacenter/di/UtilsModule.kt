package com.fesskiev.mediacenter.di

import android.content.Context
import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.engines.ExoPlayerEngine
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.FileSystemUtils

import com.fesskiev.mediacenter.utils.NotificationUtils
import com.fesskiev.mediacenter.utils.PermissionsUtils
import com.fesskiev.mediacenter.utils.player.MediaPlayer
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import com.fesskiev.mediacenter.utils.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import javax.inject.Singleton

@Module
class UtilsModule {

    @Provides
    @Singleton
    fun provideBitmapUtils(context: Context, httpClient: OkHttpClient): BitmapUtils = BitmapUtils(context, httpClient)

    @Provides
    @Singleton
    fun providePermissionsUtils(): PermissionsUtils = PermissionsUtils()

    @Provides
    @Singleton
    fun provideNotificationUtils(context: Context, mediaPlayer: MediaPlayer): NotificationUtils = NotificationUtils(context, mediaPlayer)

    @Provides
    @Singleton
    fun provideMediaPlayer(fFmpegEngine: FFmpegEngine, superpoweredEngine: SuperpoweredEngine,
                           exoPlayerEngine: ExoPlayerEngine): MediaPlayer = MediaPlayer(fFmpegEngine, superpoweredEngine, exoPlayerEngine)

    @Provides
    @Singleton
    fun provideSchedulerProvider(): BaseSchedulerProvider = SchedulerProvider()

    @Provides
    @Singleton
    fun provideFileSystemUtils(): FileSystemUtils = FileSystemUtils()
}