package com.fesskiev.mediacenter.ui.media.files

import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction


class FilesPresenter(private var compositeDisposable: CompositeDisposable,
                     private var dataRepository: DataRepository,
                     private var schedulerProvider: BaseSchedulerProvider,
                     private var view: FilesContract.View) : FilesContract.Presenter {

    override fun fetchMediaFiles(limit : Int, offset : Int) {
        view.showProgressBar()
        compositeDisposable.add(getZipMediaFiles(limit, offset)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ mediaFiles -> handleMediaFiles(mediaFiles) }, { throwable -> handleError(throwable) }))
    }

    private fun getZipMediaFiles(limit: Int, offset: Int): Single<List<MediaFile>> {
        val localDataSource = dataRepository.localDataSource
        return Single.zip(localDataSource.getAudioFiles(limit, offset), localDataSource.getVideoFiles(limit, offset),
                BiFunction { audioFiles, videoFiles -> zipMediaFiles(audioFiles, videoFiles) })
    }

    private fun zipMediaFiles(audioFiles: List<AudioFile>, videoFiles: List<VideoFile>): List<MediaFile> {
        val mediaFiles: MutableList<MediaFile> = ArrayList()
        mediaFiles.addAll(audioFiles)
        mediaFiles.addAll(videoFiles)
        return mediaFiles
    }

    private fun handleMediaFiles(mediaFiles: List<MediaFile>) {
        view.hideProgressBar()
        view.showMediaFiles(mediaFiles)
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