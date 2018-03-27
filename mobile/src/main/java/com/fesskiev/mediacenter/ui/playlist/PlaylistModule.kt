package com.fesskiev.mediacenter.ui.playlist

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class PlaylistModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun providePlaylistPresenter(compositeDisposable: CompositeDisposable, dataRepository: DataRepository,
                                     schedulerProvider: BaseSchedulerProvider,
                                     view: PlaylistContract.View): PlaylistPresenter = PlaylistPresenter(compositeDisposable, dataRepository, schedulerProvider, view)

        @JvmStatic
        @Provides
        fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    }

    @Binds
    abstract fun providePlaylistView(playlistActivity: PlaylistActivity): PlaylistContract.View
}