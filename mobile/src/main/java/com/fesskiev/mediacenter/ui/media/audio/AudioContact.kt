package com.fesskiev.mediacenter.ui.media.audio

import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView


interface AudioContact {

    interface View : BaseView {

        fun showAudioFolders(audioFolders : List<AudioFolder>)
    }

    interface Presenter : BasePresenter {

        fun fetchAudioFolders()
    }
}