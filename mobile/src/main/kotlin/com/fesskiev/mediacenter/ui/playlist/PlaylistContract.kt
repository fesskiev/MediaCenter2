package com.fesskiev.mediacenter.ui.playlist

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView
import io.reactivex.Single


interface PlaylistContract {

    interface View : BaseView {

        fun showPlaylist(mediaFiles : List<MediaFile>)

        fun showEmptyPlaylist()

        fun showPlaylistFileDeleted()

        fun removeFileAdapter(position: Int)
    }

    interface Presenter : BasePresenter {

        fun getPlaylist()

        fun deletePlaylistFile(mediaFile: MediaFile, position: Int, lastItem: Boolean)

        fun getMediaFileArtwork(mediaFile: MediaFile): Single<Bitmap>
    }
}