package com.fesskiev.mediacenter.ui.media.video

import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView


interface VideoContract {

    interface View : BaseView {

        fun showVideoFolders(videoFolders : List<VideoFolder>)
    }

    interface Presenter : BasePresenter {

        fun fetchVideoFolders()
    }
}