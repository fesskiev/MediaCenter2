package com.fesskiev.mediacenter.ui.media.video

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView
import io.reactivex.Single


interface VideoFoldersContract {

    interface View : BaseView {

        fun showVideoFolders(videoFolders: List<VideoFolder>)

        fun showVideoFolderNotExist()

        fun updateSelectedVideoFolder(videoFolder: VideoFolder)
    }

    interface Presenter : BasePresenter {

        fun fetchVideoFolders()

        fun checkVideoFolderExist(videoFolder: VideoFolder): Boolean

        fun getVideoFolderArtwork(videoFolder: VideoFolder): Single<Bitmap>
    }
}