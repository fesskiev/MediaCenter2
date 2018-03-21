package com.fesskiev.mediacenter.ui.media.video

import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView


interface VideoFoldersContract {

    interface View : BaseView {

        fun showVideoFolders(videoFolders : List<VideoFolder>)

        fun showVideoFolderNotExist()
    }

    interface Presenter : BasePresenter {

        fun fetchVideoFolders()

        fun checkVideoFolderExist(videoFolder: VideoFolder) :Boolean
    }
}