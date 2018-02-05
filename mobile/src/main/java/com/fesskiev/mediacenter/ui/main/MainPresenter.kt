package com.fesskiev.mediacenter.ui.main

import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.functions.BiFunction


class MainPresenter(private var compositeDisposable: CompositeDisposable,
                    private var dataRepository: DataRepository,
                    private var schedulerProvider: BaseSchedulerProvider,
                    private var view: MainContract.View) : MainContract.Presenter {

    override fun queryFiles(query: String) {
        if (query.isNotEmpty()) {
            compositeDisposable.add(getZipMediaFiles(query)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ audioFolders -> handleMediaFiles(audioFolders) }, { throwable -> handleError(throwable) }))
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
        view.showQueryFiles(mediaFiles)
    }

    private fun handleError(throwable: Throwable) {

    }

    override fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}