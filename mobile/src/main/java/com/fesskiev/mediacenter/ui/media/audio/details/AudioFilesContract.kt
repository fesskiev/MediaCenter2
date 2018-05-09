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

        fun fileNotExists()

        fun showBackdropBitmap(bitmap: Bitmap)

        fun showPaletteColors(paletteColors: BitmapUtils.PaletteColors)

        fun showFileDeleted()

        fun removeFileAdapter(position: Int)

        fun showFileNotDeleted()

        fun showFileAddedPlaylist()

        fun showEditFileView()

    }

    interface Presenter : BasePresenter {

        fun fetchAudioFiles(audioFolder: AudioFolder)

        fun fetchBackdropBitmap(audioFolder: AudioFolder)

        fun deleteFile(audioFile: AudioFile, position: Int)

        fun editFile(audioFile: AudioFile)

        fun toPlaylistFile(audioFile: AudioFile)

        fun playFile(audioFolder: AudioFolder, audioFile: AudioFile)
    }
}