package com.fesskiev.mediacenter.ui.media.video.details

import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView


interface VideoFilesContract {

    interface View : BaseView {

        fun showVideoFiles(videoFile: List<VideoFile>)

    }

    interface Presenter : BasePresenter {

        fun fetchVideoFiles(videoFolder: VideoFolder)
    }
}