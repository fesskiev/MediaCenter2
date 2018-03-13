package com.fesskiev.mediacenter.ui.playlist

import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView


interface PlaylistContract {

    interface View : BaseView {

        fun showPlaylist(mediaFiles : List<MediaFile>)

        fun showEmptyPlaylist()
    }

    interface Presenter : BasePresenter {

        fun getPlaylist()
    }
}