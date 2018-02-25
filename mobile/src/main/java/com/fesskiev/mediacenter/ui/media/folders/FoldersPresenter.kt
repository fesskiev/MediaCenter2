package com.fesskiev.mediacenter.ui.media.folders

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import io.reactivex.disposables.CompositeDisposable


class FoldersPresenter(private var compositeDisposable: CompositeDisposable,
                       private var dataRepository: DataRepository,
                       private var schedulerProvider: BaseSchedulerProvider,
                       private var view: FoldersContract.View?) : FoldersContract.Presenter {



    override fun detach() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        if (view != null) {
            view = null
        }
    }
}