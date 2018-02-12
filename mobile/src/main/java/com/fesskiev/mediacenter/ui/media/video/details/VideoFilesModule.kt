package com.fesskiev.mediacenter.ui.media.video.details

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class VideoFilesModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideVideoFilesPresenter(compositeDisposable: CompositeDisposable,
                                       dataRepository: DataRepository,
                                       schedulerProvider: BaseSchedulerProvider,
                                       view: VideoFilesContract.View): VideoFilesPresenter {
            return VideoFilesPresenter(compositeDisposable, dataRepository, schedulerProvider, view)
        }

        @JvmStatic
        @Provides
        fun provideCompositeDisposable(): CompositeDisposable {
            return CompositeDisposable()
        }
    }

    @Binds
    abstract fun provideVideoFilesView(videoFilesActivity: VideoFilesActivity): VideoFilesContract.View
}