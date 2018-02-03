package com.fesskiev.mediacenter.utils.player

import com.fesskiev.mediacenter.domain.entity.media.MediaFile


interface Playable {

    fun openFile(mediaFile: MediaFile)

    fun next()

    fun previous()

    fun play()

    fun pause()

    fun seek(seek: Int)

    fun position(position : Int)

    fun volume(volume : Float)
}