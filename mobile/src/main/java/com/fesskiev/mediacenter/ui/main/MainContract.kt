package com.fesskiev.mediacenter.ui.main

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView

interface MainContract {

    interface View : BaseView {

        fun showAudioControl()

        fun showVideoControl()

        fun updateSelectedAudioFile(audioFile: AudioFile)

        fun updateSelectedVideoFile(videoFile: VideoFile)

        fun updateMediaFileArtwork(bitmap: Bitmap)
    }

    interface Presenter : BasePresenter {

        fun fetchMediaControl()

        fun fetchSelectedMedia()

        fun changePlaying(play: Boolean)

        fun changeVolume(volume: Int)

        fun changeSeek(seek: Int)

        fun changePitchShift(value: Int)

        fun changeTempo(value: Int)
    }
}