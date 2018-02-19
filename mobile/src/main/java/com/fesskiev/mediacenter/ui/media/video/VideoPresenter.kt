package com.fesskiev.mediacenter.ui.media.video

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class VideoPresenter(private var compositeDisposable: CompositeDisposable,
                     private var dataRepository: DataRepository,
                     private var schedulerProvider: BaseSchedulerProvider,
                     private var bitmapUtils: BitmapUtils,
                     private var view: VideoContract.View?) : VideoContract.Presenter {

    override fun fetchVideoFolders() {
        view?.showProgressBar()
        compositeDisposable.add(dataRepository.localDataSource.getVideoFolders()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ videoFolders -> handleVideoFolders(videoFolders) }, { throwable -> handleError(throwable) }))
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

    fun getVideoFolderArtwork(videoFolder: VideoFolder): Single<Bitmap> {
        return bitmapUtils.loadVideoFolderArtwork(videoFolder)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
    }
}