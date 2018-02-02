package com.fesskiev.mediacenter.ui.media.audio

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class AudioPresenter(var compositeDisposable: CompositeDisposable,
                     var dataRepository: DataRepository,
                     var baseSchedulerProvider: BaseSchedulerProvider,
                     var view: AudioContact.View) : AudioContact.Presenter {


    override fun fetchAudioFolders() {

    }

    override fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
    }
}