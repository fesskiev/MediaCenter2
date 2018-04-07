package com.fesskiev.mediacenter.ui.playlist

import android.graphics.Bitmap
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction


class PlaylistPresenter(private var compositeDisposable: CompositeDisposable,
                        private var dataRepository: DataRepository,
                        private var schedulerProvider: BaseSchedulerProvider,
                        private var bitmapUtils: BitmapUtils,
                        private var view: PlaylistContract.View?) : PlaylistContract.Presenter {

    override fun getPlaylist() {
        view?.showProgressBar()
        compositeDisposable.add(getPlaylistFiles()
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ mediaFiles -> handlePlaylistFiles(mediaFiles) },
                        { throwable -> handleError(throwable) }))
    }

    override fun deletePlaylistFile(mediaFile: MediaFile, position: Int, lastItem: Boolean) {
        view?.showProgressBar()
        compositeDisposable.add(deletePlaylistFile(mediaFile)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ handleDeletedFiles(position, lastItem) },
                        { throwable -> handleError(throwable) }))
    }

    private fun handleDeletedFiles(position: Int, lastItem: Boolean) {
        view?.hideProgressBar()
        view?.removeFileAdapter(position)
        view?.showPlaylistFileDeleted()
        if (lastItem) {
            view?.showEmptyPlaylist()
        }
    }

    private fun deletePlaylistFile(mediaFile: MediaFile): Single<Any> {
        mediaFile.setToPlayList(false)
        return if (mediaFile is AudioFile) {
            dataRepository.localDataSource.updateAudioFile(mediaFile)
        } else {
            mediaFile as VideoFile
            dataRepository.localDataSource.updateVideoFile(mediaFile)
        }
    }

    private fun getPlaylistFiles(): Single<List<MediaFile>> {
        val localDataSource = dataRepository.localDataSource
        return Single.zip(localDataSource.getAudioFilePlaylist(),
                localDataSource.getVideoFilePlaylist(),
                BiFunction { audioFiles, videoFiles -> zipMediaFiles(audioFiles, videoFiles) })
    }

    private fun zipMediaFiles(audioFiles: List<AudioFile>, videoFiles: List<VideoFile>): List<MediaFile> {
        val mediaFiles: MutableList<MediaFile> = ArrayList()
        mediaFiles.addAll(audioFiles)
        mediaFiles.addAll(videoFiles)
        return mediaFiles
    }

    private fun handlePlaylistFiles(mediaFiles: List<MediaFile>) {
        view?.hideProgressBar()
        if (mediaFiles.isNotEmpty()) {
            view?.showPlaylist(mediaFiles)
        } else {
            view?.showEmptyPlaylist()
        }
    }

    private fun handleError(t: Throwable) {
        t.printStackTrace()
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

    override fun getMediaFileArtwork(mediaFile: MediaFile): Single<Bitmap> {
        return bitmapUtils.getMediaFileArtwork(mediaFile)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
    }
}