package com.fesskiev.mediacenter.ui.main

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainPresenter(private var compositeDisposable: CompositeDisposable,
                    private var dataRepository: DataRepository,
                    private var schedulerProvider: BaseSchedulerProvider,
                    private var bitmapUtils: BitmapUtils,
                    private var view: MainContract.View?) : MainContract.Presenter {


    override fun fetchMediaControl() {
        view?.showAudioControl()
    }

    override fun fetchSelectedMedia() {
        compositeDisposable.add(dataRepository.localDataSource.getSelectedAudioFolder()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ audioFolders -> handleSelectedAudioFolders(audioFolders) },
                        { throwable -> handleError(throwable) }))

        compositeDisposable.add(dataRepository.localDataSource.getSelectedAudioFile()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .doOnNext { audioFile -> handleSelectedAudioFile(audioFile) }
                .subscribeOn(schedulerProvider.io())
                .take(1)
                .singleOrError()
                .flatMap { audioFile -> bitmapUtils.getMediaFileArtworkPlayback(audioFile) }
                .observeOn(schedulerProvider.ui())
                .subscribe({ artwork -> handleMediaFileArtwork(artwork) },
                        { throwable -> handleError(throwable) }))

        compositeDisposable.add(dataRepository.localDataSource.getSelectedVideoFolder()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ videoFolder -> handleSelectedVideoFolder(videoFolder) },
                        { throwable -> handleError(throwable) }))

        compositeDisposable.add(dataRepository.localDataSource.getSelectedVideoFile()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ videoFile -> handleSelectedVideoFile(videoFile) },
                        { throwable -> handleError(throwable) }))
    }

    private fun handleMediaFileArtwork(artwork: Bitmap) {
        view?.updateMediaFileArtwork(artwork)
    }

    private fun handleSelectedVideoFolder(videoFolder: VideoFolder) {
        view?.updateSelectedVideoFolder(videoFolder)
    }

    private fun handleSelectedVideoFile(videoFile: VideoFile) {
        view?.updateSelectedVideoFile(videoFile)
    }

    private fun handleSelectedAudioFolders(audioFolders: AudioFolder) {
        view?.updateSelectedAudioFolder(audioFolders)
    }

    private fun handleSelectedAudioFile(audioFile: AudioFile) {
        view?.updateSelectedAudioFile(audioFile)
    }

    private fun handleError(t: Throwable) {
        t.printStackTrace()
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