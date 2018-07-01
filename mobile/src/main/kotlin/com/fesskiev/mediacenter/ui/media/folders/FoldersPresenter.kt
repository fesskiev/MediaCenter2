package com.fesskiev.mediacenter.ui.media.folders

import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.filterAudioFilesInFolder
import com.fesskiev.mediacenter.utils.filterVideoFilesInFolder
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.Single
import io.reactivex.SingleOnSubscribe
import io.reactivex.disposables.CompositeDisposable
import java.io.File

class FoldersPresenter(private var compositeDisposable: CompositeDisposable,
                       private var dataRepository: DataRepository,
                       private var schedulerProvider: BaseSchedulerProvider,
                       private var view: FoldersContract.View?) : FoldersContract.Presenter {

    override fun checkDirIsMedia(dir: File) {
        if (!filterAudioFilesInFolder(dir).isEmpty()) {
            compositeDisposable.add(containsAudioFolder(dir)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ result -> containAudioFolderResult(result) },
                            { throwable -> handleError(throwable) }))
        } else if (!filterVideoFilesInFolder(dir).isEmpty()) {
            compositeDisposable.add(containsVideoFolder(dir)
                    .subscribeOn(schedulerProvider.io())
                    .observeOn(schedulerProvider.ui())
                    .subscribe({ result -> containVideoFolderResult(result) },
                            { throwable -> handleError(throwable) }))
        }
    }

    override fun getMediaFileByPath(file: File) {
        view?.showProgressBar()
        compositeDisposable.add(dataRepository.localDataSource.getAudioFileByPath(file.absolutePath)
                .subscribeOn(schedulerProvider.io())
                .observeOn(schedulerProvider.ui())
                .subscribe({ audioFile -> handleAudioFile(audioFile) },
                        { throwable -> handleError(throwable) }))
    }

    private fun containVideoFolderResult(contain: Boolean) {
        if (!contain) {
            view?.showAddAudioFolder()
        }
    }

    private fun containAudioFolderResult(contain: Boolean) {
        if (!contain) {
            view?.showAddVideoFolder()
        }
    }

    private fun containsAudioFolder(dir: File): Single<Boolean> {
        return Single.create({
            it.onSuccess(dataRepository.localDataSource.containAudioFolder(dir.absolutePath))
        })
    }

    private fun containsVideoFolder(dir: File): Single<Boolean> {
        return Single.create({
            it.onSuccess(dataRepository.localDataSource.containVideoFolder(dir.absolutePath))
        })
    }

    private fun handleAudioFile(audioFile: AudioFile) {
        view?.hideProgressBar()
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
}