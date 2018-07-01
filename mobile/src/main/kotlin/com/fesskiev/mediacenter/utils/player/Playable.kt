package com.fesskiev.mediacenter.utils.player

import com.fesskiev.mediacenter.domain.entity.media.MediaFile


interface Playable {

    fun open(mediaFile: MediaFile)

    fun next(): MediaFile?

    fun previous(): MediaFile?

    fun play()

    fun pause()

    fun seek(seek: Int)

    fun position(position: Int)

    fun pitchShift(pitchShift: Int)

    fun tempo(tempo: Double)

    fun volume(volume: Float)

    fun shuffle()

    fun isPlaying(): Boolean

    fun background()

    fun foreground()

    fun shutdown()
}