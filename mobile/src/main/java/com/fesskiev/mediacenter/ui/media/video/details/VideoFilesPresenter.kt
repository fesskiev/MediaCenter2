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
}