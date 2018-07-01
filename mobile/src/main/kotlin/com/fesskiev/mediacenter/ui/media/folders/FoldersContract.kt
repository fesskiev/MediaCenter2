package com.fesskiev.mediacenter.ui.media.folders

import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView
import java.io.File

interface FoldersContract {

    interface View : BaseView {

        fun showAddAudioFolder()

        fun showAddVideoFolder()
    }

    interface Presenter : BasePresenter {

        fun getMediaFileByPath(file: File)

        fun checkDirIsMedia(dir: File)
    }
}