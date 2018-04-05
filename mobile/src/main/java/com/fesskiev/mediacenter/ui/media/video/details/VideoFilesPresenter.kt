package com.fesskiev.mediacenter.ui.media.video.details

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable


class VideoFilesPresenter(private var compositeDisposable: CompositeDisposable,
                          private var dataRepository: DataRepository,
                          private var schedulerProvider: BaseSchedulerProvider,
                          private var bitmapUtils: BitmapUtils,
                          private var view: VideoFilesContract.View?) : VideoFilesContract.Presenter {

    override fun fetchVideoFiles(videoFolder: VideoFolder) {
        view?.showProgressBar()
        compositeDisposable.add(dataRepository.localDataSource.getVideoFiles(videoFolder.videoFolderId)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ videoFiles -> handleVideoFiles(videoFiles) },
                        { throwable -> handleError(throwable) }))
    }

    private fun handleError(throwable: Throwable) {
        throwable.printStackTrace()
        view?.hideProgressBar()
    }

    private fun handleVideoFiles(videoFiles: List<VideoFile>) {
        view?.hideProgressBar()
        view?.showVideoFiles(videoFiles)
    }

    override fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        if (view != null) {
            view = null
        }
    }

    fun getVideoFileArtwork(videoFile: VideoFile): Single<Bitmap> {
        return bitmapUtils.getVideoFileArtwork(videoFile.videoFileFramePath)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
    }

    override fun deleteFile(videoFile: VideoFile, position: Int) {
        if (!videoFile.exists()) {
            view?.fileNotExists()
            return
        }
        view?.showProgressBar()
        compositeDisposable.add(dataRepository.localDataSource.deleteVideoFile(videoFile)
                .subscribeOn(schedulerProvider.io())
                .flatMap { Single.just(videoFile.videoFilePath.delete()) }
                .observeOn(schedulerProvider.ui())
                .subscribe({ deleted -> handleDeletedFile(deleted, position) }, { throwable -> handleError(throwable) }))
    }

    override fun editFile(videoFile: VideoFile) {
        if (!videoFile.exists()) {
            view?.fileNotExists()
            return
        }
        view?.showEditFileView()
    }

    override fun toPlaylistFile(videoFile: VideoFile) {
        if (!videoFile.exists()) {
            view?.fileNotExists()
            return
        }
        view?.showProgressBar()
        videoFile.setToPlayList(true)
        compositeDisposable.add(dataRepository.localDataSource.updateVideoFile(videoFile)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ handleFileAddedToPlaylist() }, { throwable -> handleError(throwable) }))
    }

    override fun playFile(videoFile: VideoFile) {
        if (!videoFile.exists()) {
            view?.fileNotExists()
            return
        }
    }

    private fun handleFileAddedToPlaylist() {
        view?.hideProgressBar()
        view?.showFileAddedPlaylist()
    }

    private fun handleDeletedFile(deleted: Boolean, position: Int) {
        view?.hideProgressBar()
        if (deleted) {
            view?.removeFileAdapter(position)
            view?.showFileDeleted()
        } else {
            view?.showFileNotDeleted()
        }
    }
}