package com.fesskiev.mediacenter.ui.media.files

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView
import io.reactivex.Single


interface FilesContract {

    interface View : BaseView {

        fun showQueryFiles(mediaFiles: List<MediaFile>)

        fun showEmptyQuery()
    }

    interface Presenter : BasePresenter {

        fun queryFiles(query: String)

        fun getMediaFileArtwork(mediaFile: MediaFile): Single<Bitmap>
    }
}