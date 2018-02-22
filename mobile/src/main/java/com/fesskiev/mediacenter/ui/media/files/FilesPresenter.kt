package com.fesskiev.mediacenter.ui.media.files

import android.graphics.Bitmap
import android.util.Log
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction


class FilesPresenter(private var compositeDisposable: CompositeDisposable,
                     private var dataRepository: DataRepository,
                     private var schedulerProvider: BaseSchedulerProvider,
                     private var bitmapUtils: BitmapUtils,
                     private var view: FilesContract.View?) : FilesContract.Presenter {

    override fun fetchMediaFiles(limit : Int, offset : Int) {
        Log.wtf("test", "offser: $offset")
        view?.showProgressBar()
        compositeDisposable.add(getZipMediaFiles(limit, offset)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ mediaFiles -> handleMediaFiles(mediaFiles) }, { throwable -> handleError(throwable) }))
    }

    private fun getZipMediaFiles(limit: Int, offset : Int): Flowable<List<MediaFile>> {
        val localDataSource = dataRepository.localDataSource
        return Flowable.zip(localDataSource.getAudioFiles(limit, offset), localDataSource.getVideoFiles(limit, offset),
                BiFunction { audioFiles, videoFiles -> zipMediaFiles(audioFiles, videoFiles) })
    }

    private fun zipMediaFiles(audioFiles: List<AudioFile>, videoFiles: List<VideoFile>): List<MediaFile> {
        Log.wtf("test", "**size**: audio: ${audioFiles.size} video: ${videoFiles.size}")
        val mediaFiles: MutableList<MediaFile> = ArrayList()
        mediaFiles.addAll(audioFiles)
        mediaFiles.addAll(videoFiles)
        return mediaFiles
    }

    private fun handleMediaFiles(mediaFiles: List<MediaFile>) {
        view?.hideProgressBar()
        view?.showMediaFiles(mediaFiles)
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

    fun getAudioFolderArtwork(mediaFile: MediaFile): Single<Bitmap> {
        return bitmapUtils.getMediaFileArtwork(mediaFile)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
    }
}