package com.fesskiev.mediacenter.ui.media.files

import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView


interface FilesContract {

    interface View : BaseView {

        fun showMediaFiles(mediaFiles: List<MediaFile>)

        fun showQueryFiles(mediaFiles: List<MediaFile>)

        fun fetchMediaFiles()
    }

    interface Presenter : BasePresenter {

        fun fetchMediaFiles(limit: Int, offset: Int)
    }
}