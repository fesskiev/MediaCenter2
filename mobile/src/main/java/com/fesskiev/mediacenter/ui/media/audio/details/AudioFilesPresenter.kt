package com.fesskiev.mediacenter.ui.media.audio.details

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class AudioFilesPresenter(private var compositeDisposable: CompositeDisposable,
                          private var dataRepository: DataRepository,
                          private var schedulerProvider: BaseSchedulerProvider,
                          private var view: AudioFilesContract.View) : AudioFilesContract.Presenter {

    override fun detach() {

    }
}