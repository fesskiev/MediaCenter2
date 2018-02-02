package com.fesskiev.mediacenter.ui.media.audio

import com.fesskiev.mediacenter.domain.source.DataRepository
import io.reactivex.disposables.CompositeDisposable


class AudioPresenter(compositeDisposable: CompositeDisposable,
                     dataRepository: DataRepository,
                     view: AudioContact.View) : AudioContact.Presenter {


    override fun detach() {

    }
}