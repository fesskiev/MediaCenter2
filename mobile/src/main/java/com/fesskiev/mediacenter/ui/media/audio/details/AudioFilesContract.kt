package com.fesskiev.mediacenter.ui.media.audio.details

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView
import com.fesskiev.mediacenter.utils.BitmapUtils

interface AudioFilesContract {

    interface View : BaseView {

        fun showAudioFiles(audioFiles: List<AudioFile>)

        fun showBackdropBitmap(bitmap: Bitmap)

        fun showPaletteColors(paletteColors: BitmapUtils.PaletteColors)

        fun audioFileNotExist()
    }

    interface Presenter : BasePresenter {

        fun fetchAudioFiles(audioFolder: AudioFolder)

        fun fetchBackdropBitmap(audioFolder: AudioFolder)
    }
}