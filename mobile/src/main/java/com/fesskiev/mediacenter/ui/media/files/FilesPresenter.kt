package com.fesskiev.mediacenter.ui.media.files

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class FilesPresenter(var compositeDisposable: CompositeDisposable,
                     var dataRepository: DataRepository,
                     var baseSchedulerProvider: BaseSchedulerProvider,
                     var view: FilesContract.View) : FilesContract.Presenter {

    override fun fetchMediaFiles() {

    }

    override fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}