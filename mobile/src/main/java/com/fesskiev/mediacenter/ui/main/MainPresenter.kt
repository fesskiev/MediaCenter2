package com.fesskiev.mediacenter.ui.main

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class MainPresenter(private var compositeDisposable: CompositeDisposable,
                    private var dataRepository: DataRepository,
                    private var schedulerProvider: BaseSchedulerProvider,
                    private var view: MainContract.View?) : MainContract.Presenter {

    override fun detach() {
        if (view != null) {
            view = null
        }
    }
}