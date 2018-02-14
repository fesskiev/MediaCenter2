package com.fesskiev.mediacenter.ui.main

import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView


interface MainContract {

    interface View : BaseView {

        fun showQueryFiles(mediaFiles: List<MediaFile>)

        fun queryIsEmpty()
    }

    interface Presenter : BasePresenter {

        fun queryFiles(query : String)
    }
}