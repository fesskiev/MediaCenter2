package com.fesskiev.mediacenter.di

import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.engines.ExoPlayerEngine
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class EnginesModule {

    @Provides
    @Singleton
    fun provideFFmpegEngine() : FFmpegEngine {
        return FFmpegEngine()
    }

    @Provides
    @Singleton
    fun provideSuperpoweredSDKEngine() : SuperpoweredEngine {
        return SuperpoweredEngine()
    }

    @Provides
    @Singleton
    fun provideExoPlayerEngine() : ExoPlayerEngine {
        return ExoPlayerEngine()
    }
}