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
        val localDataSource = dataRepository.localDataSource
        compositeDisposable.add(localDataSource.getAudioFolders()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .map { audioFolders -> handleAudioFolders(audioFolders) }
                .flatMap { localDataSource.getSelectedAudioFolder() }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnNext { audioFolder: AudioFolder -> audioFolder.audioFolderSelected = true }
                .subscribe({ audioFolder -> handleSelectedAudioFolder(audioFolder) },
                        { throwable -> handleError(throwable) }))
    }

    private fun handleSelectedAudioFolder(audioFolder: AudioFolder) {
        view?.updateSelectedAudioFolder(audioFolder)
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
        if (audioFolders.isNotEmpty()) {
            view?.hideHintScanView()
        } else {
            view?.showHintScanView()
        }
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