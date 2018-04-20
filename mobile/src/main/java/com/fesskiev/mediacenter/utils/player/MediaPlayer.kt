package com.fesskiev.mediacenter.utils.player

import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.engines.ExoPlayerEngine

class MediaPlayer(private var ffmpegEngine: FFmpegEngine,
                  private var superpoweredEngine: SuperpoweredEngine,
                  private var exoPlayerEngine: ExoPlayerEngine) : Playable {

    private var isPlaying: Boolean = false


    override fun open(mediaFile: MediaFile) {

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
}