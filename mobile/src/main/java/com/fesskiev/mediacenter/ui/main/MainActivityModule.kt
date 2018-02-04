package com.fesskiev.mediacenter.ui.main

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import com.fesskiev.mediacenter.utils.schedulers.SchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class MainActivityModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideMainPresenter(compositeDisposable: CompositeDisposable,
                                  dataRepository: DataRepository,
                                  schedulerProvider: BaseSchedulerProvider,
                                  view: MainContract.View): MainPresenter {
            return MainPresenter(compositeDisposable, dataRepository, schedulerProvider, view)
        }

        @JvmStatic
        @Provides
        fun provideCompositeDisposable(): CompositeDisposable {
            return CompositeDisposable()
        }

        @JvmStatic
        @Provides
        fun provideSchedulerProvider(): BaseSchedulerProvider {
            return SchedulerProvider()
        }
    }

    @Binds
    abstract fun provideMainView(mainActivity: MainActivity): MainContract.View
}