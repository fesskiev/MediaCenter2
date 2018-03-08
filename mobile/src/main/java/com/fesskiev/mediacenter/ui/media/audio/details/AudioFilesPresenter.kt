package com.fesskiev.mediacenter.ui.media.audio.details

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class AudioFilesPresenter(private var compositeDisposable: CompositeDisposable,
                          private var dataRepository: DataRepository,
                          private var schedulerProvider: BaseSchedulerProvider,
                          private var bitmapUtils: BitmapUtils,
                          private var view: AudioFilesContract.View?) : AudioFilesContract.Presenter {

    override fun fetchAudioFiles(audioFolder: AudioFolder) {
        view?.showProgressBar()
        compositeDisposable.add(dataRepository.localDataSource.getAudioTracks(audioFolder.audioFolderId)
                .doOnSuccess { Collections.sort(it) }
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ audioFiles -> handleAudioFolders(audioFiles) }, { throwable -> handleError(throwable) }))
    }

    override fun fetchBackdropBitmap(audioFolder: AudioFolder) {
        compositeDisposable.add(bitmapUtils.getAudioFolderArtwork(audioFolder)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ bitmap -> handleBackdropBitmap(bitmap) }, { throwable -> handleError(throwable) }))
    }

    private fun handleBackdropBitmap(bitmap: Bitmap) {
        view?.showBackdropBitmap(bitmap)
    }

    private fun handleAudioFolders(audioFiles: List<AudioFile>) {
        view?.hideProgressBar()
        view?.showAudioFiles(audioFiles)
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

}