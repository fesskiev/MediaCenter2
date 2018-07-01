package com.fesskiev.mediacenter.ui.media.audio

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView
import io.reactivex.Single


interface AudioFoldersContact {

    interface View : BaseView {

        fun showAudioFolders(audioFolders : List<AudioFolder>)

        fun showAudioFolderNotExist()

        fun updateSelectedAudioFolder(audioFolder: AudioFolder)

        fun showHintScanView()

        fun hideHintScanView()
    }

    interface Presenter : BasePresenter {

        fun fetchAudioFolders()

        fun getAudioFolderArtwork(audioFolder: AudioFolder): Single<Bitmap>

        fun checkAudioFolderExist(audioFolder: AudioFolder) :Boolean
    }
}