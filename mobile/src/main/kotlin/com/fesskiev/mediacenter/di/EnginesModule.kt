package com.fesskiev.mediacenter.di

import android.content.Context
import android.media.AudioManager
import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.engines.ExoPlayerEngine
import com.fesskiev.mediacenter.engines.TagsEngine
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.FileSystemUtils
import com.fesskiev.mediacenter.utils.PermissionsUtils
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
    fun provideSuperpoweredSDKEngine(context: Context, fileSystemUtils: FileSystemUtils): SuperpoweredEngine {
        var sampleRateString: String? = null
        var bufferSizeString: String? = null
        val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager?
        if (audioManager != null) {
            sampleRateString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_SAMPLE_RATE)
            bufferSizeString = audioManager.getProperty(AudioManager.PROPERTY_OUTPUT_FRAMES_PER_BUFFER)
        }
        if (sampleRateString == null) {
            sampleRateString = "44100"
        }
        if (bufferSizeString == null) {
            bufferSizeString = "512"
        }
        return SuperpoweredEngine(fileSystemUtils.getRecordTempPath().absolutePath,
                sampleRateString.toInt(), bufferSizeString.toInt())
    }


    @Provides
    @Singleton
    fun provideExoPlayerEngine(): ExoPlayerEngine = ExoPlayerEngine()


    @Provides
    @Singleton
    fun provideTagsEngine(context: Context, bitmapUtils: BitmapUtils): TagsEngine = TagsEngine(context, bitmapUtils)
}