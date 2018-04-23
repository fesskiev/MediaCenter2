package com.fesskiev.mediacenter.utils.player

import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.engines.ExoPlayerEngine

class MediaPlayer(private var ffmpegEngine: FFmpegEngine,
                  private var superpoweredEngine: SuperpoweredEngine,
                  private var exoPlayerEngine: ExoPlayerEngine) : Playable {

    private var isPlaying: Boolean = false
    private var currentMediaFile: MediaFile? = null
    private var currentMediaFilesList: List<MediaFile>? = null


    override fun open(mediaFile: MediaFile) {
        if (mediaFile is AudioFile) {
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
    }

    override fun pause() {
        isPlaying = false
    }

    override fun seek(seek: Int) {

    }

    override fun position(position: Int) {

    }

    override fun volume(volume: Float) {

    }

    override fun shuffle() {

    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun background() {

    }

    override fun foreground() {

    }

    override fun shutdown() {
        superpoweredEngine.unregisterCallback()
        superpoweredEngine.onDestroyAudioPlayer()
    }
}