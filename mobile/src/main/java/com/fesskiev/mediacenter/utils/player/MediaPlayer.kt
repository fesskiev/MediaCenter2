package com.fesskiev.mediacenter.utils.player

import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.engines.ExoPlayerEngine
import com.fesskiev.mediacenter.utils.enums.MediaType

class MediaPlayer(private val ffmpegEngine: FFmpegEngine,
                  private val superpoweredEngine: SuperpoweredEngine,
                  private val exoPlayerEngine: ExoPlayerEngine) : Playable {

    private var isPlaying: Boolean = false
    private var currentMediaFile: MediaFile? = null
    private var currentMediaFilesList: List<MediaFile>? = null


    override fun open(mediaFile: MediaFile) {
        if (mediaFile.getMediaType() == MediaType.AUDIO) {
            superpoweredEngine.openAudioFile(mediaFile.getFilePath())
            superpoweredEngine.togglePlayback()
        }
    }

    override fun next() {

    }

    override fun previous() {

    }

    override fun play() {
        isPlaying = true
        superpoweredEngine.togglePlayback()
    }

    override fun pause() {
        isPlaying = false
        superpoweredEngine.togglePlayback()
    }

    override fun seek(seek: Int) {
        superpoweredEngine.setSeek(seek)
    }

    override fun position(position: Int) {
        superpoweredEngine.setPosition(position)
    }

    override fun volume(volume: Float) {
        superpoweredEngine.setVolume(volume)
    }

    override fun shuffle() {

    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun background() {
        superpoweredEngine.onBackground()
    }

    override fun foreground() {
        superpoweredEngine.onForeground()
    }

    override fun shutdown() {
        superpoweredEngine.unregisterCallback()
        superpoweredEngine.onDestroyAudioPlayer()
    }
}