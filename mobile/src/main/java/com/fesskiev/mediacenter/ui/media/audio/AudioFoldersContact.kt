package com.fesskiev.mediacenter.ui.media.audio

import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView


interface AudioFoldersContact {

    interface View : BaseView {

        fun showAudioFolders(audioFolders : List<AudioFolder>)

        fun showAudioFolderNotExist()
    }

    interface Presenter : BasePresenter {

        fun fetchAudioFolders()

        fun checkAudioFolderExist(audioFolder: AudioFolder) :Boolean
    }
}