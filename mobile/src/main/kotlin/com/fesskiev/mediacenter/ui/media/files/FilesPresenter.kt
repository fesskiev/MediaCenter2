package com.fesskiev.mediacenter.ui.media.files

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


class FilesPresenter(private var compositeDisposable: CompositeDisposable,
                     private var dataRepository: DataRepository,
                     private var schedulerProvider: BaseSchedulerProvider,
                     private var bitmapUtils: BitmapUtils,
                     private var view: FilesContract.View?) : FilesContract.Presenter {


    override fun queryFiles(query: String) {
        if (query.isNotEmpty()) {
            view?.showProgressBar()
            compositeDisposable.add(getZipMediaFiles(query)
                    .toObservable()
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ audioFolders -> handleMediaFiles(audioFolders) },
                            { throwable -> handleError(throwable) }))
        } else {
            view?.showEmptyQuery()
        }
    }

    private fun getZipMediaFiles(query: String): Single<List<MediaFile>> {
        val localDataSource = dataRepository.localDataSource
        return Single.zip(localDataSource.getSearchAudioFiles(query),
                localDataSource.getSearchVideoFiles(query),
                BiFunction { audioFiles, videoFiles -> zipMediaFiles(audioFiles, videoFiles) })
    }

    private fun zipMediaFiles(audioFiles: List<AudioFile>, videoFiles: List<VideoFile>): List<MediaFile> {
        val mediaFiles: MutableList<MediaFile> = ArrayList()
        mediaFiles.addAll(audioFiles)
        mediaFiles.addAll(videoFiles)
        return mediaFiles
    }

    private fun handleMediaFiles(mediaFiles: List<MediaFile>) {
        view?.hideProgressBar()
        view?.showQueryFiles(mediaFiles)
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