package com.fesskiev.mediacenter.ui.media.folders

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class FoldersFragmentModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideFoldersPresenter(compositeDisposable: CompositeDisposable, dataRepository: DataRepository,
                                    schedulerProvider: BaseSchedulerProvider,
                                    view: FoldersContract.View): FoldersPresenter = FoldersPresenter(compositeDisposable, dataRepository, schedulerProvider, view)

    }

    @Binds
    abstract fun provideFoldersView(foldersFragment: FoldersFragment): FoldersContract.View
}