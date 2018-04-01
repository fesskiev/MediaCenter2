package com.fesskiev.mediacenter.ui.main

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

        fun updateSelectedAudioFolder(audioFolder: AudioFolder)

        fun updateSelectedVideoFolder(videoFolder: VideoFolder)
    }

    interface Presenter : BasePresenter {

        fun fetchMediaControl()

        fun fetchSelectedMedia()
    }
}