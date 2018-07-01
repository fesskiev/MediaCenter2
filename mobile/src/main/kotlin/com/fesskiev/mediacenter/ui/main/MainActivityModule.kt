package com.fesskiev.mediacenter.ui.main

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.player.MediaPlayer
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
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
                                 bitmapUtils: BitmapUtils,
                                 mediaPlayer: MediaPlayer,
                                 view: MainContract.View): MainPresenter = MainPresenter(compositeDisposable,
                dataRepository, schedulerProvider, bitmapUtils, mediaPlayer,  view)


        @JvmStatic
        @Provides
        fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
    }

    @Binds
    abstract fun provideMainView(mainActivity: MainActivity): MainContract.View
}