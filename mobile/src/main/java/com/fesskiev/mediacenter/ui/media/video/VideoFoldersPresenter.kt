package com.fesskiev.mediacenter.ui.media.video

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class VideoFoldersPresenter(private var compositeDisposable: CompositeDisposable,
                            private var dataRepository: DataRepository,
                            private var schedulerProvider: BaseSchedulerProvider,
                            private var bitmapUtils: BitmapUtils,
                            private var view: VideoFoldersContract.View?) : VideoFoldersContract.Presenter {

    override fun fetchVideoFolders() {
        view?.showProgressBar()
        val localDataSource = dataRepository.localDataSource
        compositeDisposable.add(localDataSource.getVideoFolders()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .map { videoFolders -> handleVideoFolders(videoFolders) }
                .flatMap { localDataSource.getSelectedVideoFolder() }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnNext { videoFolder -> videoFolder.videoFolderSelected = true }
                .subscribe({ videoFolder -> handleSelectedVideoFolder(videoFolder) }, { throwable -> handleError(throwable) }))
    }

    private fun handleSelectedVideoFolder(videoFolder: VideoFolder) {
        view?.updateSelectedVideoFolder(videoFolder)
    }

    override fun checkVideoFolderExist(videoFolder: VideoFolder): Boolean {
        if (videoFolder.exists()) {
            return true
        }
        view?.showVideoFolderNotExist()
        return false
    }

    private fun handleVideoFolders(videoFolders: List<VideoFolder>) {
        view?.hideProgressBar()
        view?.showVideoFolders(videoFolders)
    }

    private fun handleError(throwable: Throwable) {
        view?.hideProgressBar()
    }

    override fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        if (view != null) {
            view = null
        }
    }

    override fun getVideoFolderArtwork(videoFolder: VideoFolder): Single<Bitmap> {
        return bitmapUtils.getVideoFolderArtwork(videoFolder)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
    }
}