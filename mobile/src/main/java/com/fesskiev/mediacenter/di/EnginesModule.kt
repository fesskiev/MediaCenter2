package com.fesskiev.mediacenter.di

import android.content.Context
import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.engines.ExoPlayerEngine
import com.fesskiev.mediacenter.engines.TagsEngine
import com.fesskiev.mediacenter.utils.BitmapUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class EnginesModule {

    @Provides
    @Singleton
    fun provideFFmpegEngine(): FFmpegEngine = FFmpegEngine()


    @Provides
    @Singleton
    fun provideSuperpoweredSDKEngine(): SuperpoweredEngine = SuperpoweredEngine()


    @Provides
    @Singleton
    fun provideExoPlayerEngine(): ExoPlayerEngine = ExoPlayerEngine()


    @Provides
    @Singleton
    fun provideTagsEngine(context: Context, bitmapUtils: BitmapUtils): TagsEngine = TagsEngine(context, bitmapUtils)
}