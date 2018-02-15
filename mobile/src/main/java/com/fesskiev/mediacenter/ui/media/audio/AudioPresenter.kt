package com.fesskiev.mediacenter.ui.media.audio

import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class AudioPresenter(private var compositeDisposable: CompositeDisposable,
                     private var dataRepository: DataRepository,
                     private var schedulerProvider: BaseSchedulerProvider,
                     private var view: AudioContact.View) : AudioContact.Presenter {

    override fun fetchAudioFolders() {
        view.showProgressBar()
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
        view.showAudioFolderNotExist()
        return false
    }

    private fun handleAudioFolders(audioFolders: List<AudioFolder>) {
        view.hideProgressBar()
        view.showAudioFolders(audioFolders)
    }

    private fun handleError(throwable: Throwable) {
        view.hideProgressBar()
    }

    override fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }

}