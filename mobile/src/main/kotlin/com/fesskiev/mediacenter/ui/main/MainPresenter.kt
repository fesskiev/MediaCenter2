package com.fesskiev.mediacenter.ui.main

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.player.MediaPlayer
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable

class MainPresenter(private var compositeDisposable: CompositeDisposable,
                    private var dataRepository: DataRepository,
                    private var schedulerProvider: BaseSchedulerProvider,
                    private var bitmapUtils: BitmapUtils,
                    private var mediaPlayer: MediaPlayer,
                    private var view: MainContract.View?) : MainContract.Presenter {


    override fun fetchMediaControl() {
        view?.showAudioControl()
    }

    override fun fetchSelectedMedia() {
        val localSource = dataRepository.localDataSource
        compositeDisposable.add(localSource.getSelectedAudioFile()
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

        compositeDisposable.add(localSource.getSelectedVideoFile()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ videoFile -> handleSelectedVideoFile(videoFile) },
                        { throwable -> handleError(throwable) }))
    }

    override fun changePlaying(play: Boolean) {
        if (play) {
            mediaPlayer.pause()
        } else {
            mediaPlayer.play()
        }
    }

    override fun changeVolume(volume: Int) {
        mediaPlayer.volume(volume.toFloat())
    }

    override fun changeSeek(seek: Int) {
        mediaPlayer.seek(seek)
    }

    override fun changePitchShift(value: Int) {
        mediaPlayer.pitchShift(value)
    }

    override fun changeTempo(value: Double) {
        mediaPlayer.tempo(value)
    }

    private fun handleMediaFileArtwork(artwork: Bitmap) {
        view?.updateMediaFileArtwork(artwork)
    }

    private fun handleSelectedVideoFile(videoFile: VideoFile) {
        view?.updateSelectedVideoFile(videoFile)
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