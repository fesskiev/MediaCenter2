package com.fesskiev.mediacenter.utils.player

import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.engines.ExoPlayerEngine

class MediaPlayer(var ffmpegEngine: FFmpegEngine,
                  var superpoweredEngine: SuperpoweredEngine,
                  var exoPlayerEngine: ExoPlayerEngine) : Playable {


    override fun open(mediaFile: MediaFile) {

    }

    override fun next() {

    }

    override fun previous() {

    }

    override fun play() {

    }

    override fun pause() {

    }

    override fun seek(seek: Int) {

    }

    override fun position(position: Int) {

    }

    override fun volume(volume: Float) {

    }

    override fun shuffle() {

    }
}