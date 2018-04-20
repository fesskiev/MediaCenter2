package com.fesskiev.mediacenter.ui.media.audio.details

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.player.MediaPlayer
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import java.util.*

class AudioFilesPresenter(private var compositeDisposable: CompositeDisposable,
                          private var dataRepository: DataRepository,
                          private var schedulerProvider: BaseSchedulerProvider,
                          private var bitmapUtils: BitmapUtils,
                          private var mediaPlayer: MediaPlayer,
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
                .doOnSuccess { bitmap: Bitmap -> fetchPaletteColors(bitmap) }
                .observeOn(schedulerProvider.ui())
                .subscribe({ bitmap -> handleBackdropBitmap(bitmap) }, { throwable -> handleError(throwable) }))
    }

    private fun fetchPaletteColors(bitmap: Bitmap) {
        bitmapUtils.getPaletteColorsFromBitmap(bitmap)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ paletteColors -> handlePaletteColors(paletteColors) }, { throwable -> handleError(throwable) })
    }

    override fun deleteFile(audioFile: AudioFile, position: Int) {
        if (!audioFile.exists()) {
            view?.fileNotExists()
            return
        }
        view?.showProgressBar()
        compositeDisposable.add(dataRepository.localDataSource.deleteAudioFile(audioFile)
                .subscribeOn(schedulerProvider.io())
                .flatMap { Single.just(audioFile.audioFilePath.delete()) }
                .observeOn(schedulerProvider.ui())
                .subscribe({ deleted -> handleDeletedFile(deleted, position) }, { throwable -> handleError(throwable) }))
    }

    override fun editFile(audioFile: AudioFile) {
        if (!audioFile.exists()) {
            view?.fileNotExists()
            return
        }
        view?.showEditFileView()
    }

    override fun toPlaylistFile(audioFile: AudioFile) {
        if (!audioFile.exists()) {
            view?.fileNotExists()
            return
        }
        view?.showProgressBar()
        audioFile.setToPlayList(true)
        compositeDisposable.add(dataRepository.localDataSource.updateAudioFile(audioFile)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ handleFileAddedToPlaylist() }, { throwable -> handleError(throwable) }))
    }

    override fun playFile(audioFile: AudioFile) {
        if (!audioFile.exists()) {
            view?.fileNotExists()
            return
        }
        compositeDisposable.add(dataRepository.localDataSource.updateSelectedAudioFile(audioFile)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ playAudioFile(audioFile) }, { throwable -> handleError(throwable) }))
    }

    private fun playAudioFile(audioFile: AudioFile) {
        mediaPlayer.open(audioFile)
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

    private fun handleFileAddedToPlaylist() {
        view?.hideProgressBar()
        view?.showFileAddedPlaylist()
    }

    private fun handlePaletteColors(paletteColors: BitmapUtils.PaletteColors) {
        view?.showPaletteColors(paletteColors)
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