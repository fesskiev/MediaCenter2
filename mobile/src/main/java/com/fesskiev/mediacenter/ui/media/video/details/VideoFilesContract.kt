package com.fesskiev.mediacenter.ui.media.video.details

import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.BasePresenter
import com.fesskiev.mediacenter.ui.BaseView


interface VideoFilesContract {

    interface View : BaseView {

        fun showVideoFiles(videoFiles: List<VideoFile>)

        fun fileNotExists()

        fun showFileDeleted()

        fun removeFileAdapter(position: Int)

        fun showFileNotDeleted()

        fun showFileAddedPlaylist()

        fun showEditFileView()
    }

    interface Presenter : BasePresenter {

        fun fetchVideoFiles(videoFolder: VideoFolder)

        fun deleteFile(videoFile: VideoFile, position: Int)

        fun editFile(videoFile: VideoFile)

        fun toPlaylistFile(videoFile: VideoFile)

        fun playFile(videoFile: VideoFile)
    }
}