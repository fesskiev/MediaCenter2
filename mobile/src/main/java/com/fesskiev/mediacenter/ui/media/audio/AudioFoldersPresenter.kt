package com.fesskiev.mediacenter.ui.media.audio

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable

class AudioFoldersPresenter(private var compositeDisposable: CompositeDisposable,
                            private var dataRepository: DataRepository,
                            private var schedulerProvider: BaseSchedulerProvider,
                            private var bitmapUtils: BitmapUtils,
                            private var view: AudioFoldersContact.View?) : AudioFoldersContact.Presenter {

    override fun fetchAudioFolders() {
        view?.showProgressBar()
        compositeDisposable.add(dataRepository.localDataSource.getAudioFolders()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ audioFolders -> handleAudioFolders(audioFolders) },
                        { throwable -> handleError(throwable) }))
    }

    override fun checkAudioFolderExist(audioFolder: AudioFolder): Boolean {
        if (audioFolder.exists()) {
            return true
        }
        view?.showAudioFolderNotExist()
        return false
    }

    private fun handleAudioFolders(audioFolders: List<AudioFolder>) {
        view?.hideProgressBar()
        view?.showAudioFolders(audioFolders)
    }

    private fun handleError(throwable: Throwable) {
        throwable.printStackTrace()
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

    override fun getAudioFolderArtwork(audioFolder: AudioFolder): Single<Bitmap> {
        return bitmapUtils.getAudioFolderArtwork(audioFolder)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
    }
}