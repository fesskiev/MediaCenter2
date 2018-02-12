package com.fesskiev.mediacenter.ui.media.video.details

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class VideoFilesPresenter(private var compositeDisposable: CompositeDisposable,
                          private var dataRepository: DataRepository,
                          private var schedulerProvider: BaseSchedulerProvider,
                          private var view: VideoFilesContract.View) : VideoFilesContract.Presenter {

    override fun detach() {

    }
}