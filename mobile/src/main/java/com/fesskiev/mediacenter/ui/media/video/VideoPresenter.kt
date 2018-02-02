package com.fesskiev.mediacenter.ui.media.video

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class VideoPresenter(var compositeDisposable: CompositeDisposable,
                     var dataRepository: DataRepository,
                     var baseSchedulerProvider: BaseSchedulerProvider,
                     var view: VideoContract.View) : VideoContract.Presenter {


    override fun fetchVideoFolders() {

    }

    override fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}