package com.fesskiev.mediacenter.ui.media.video.details

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
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
        fun provideVideoFilesPresenter(compositeDisposable: CompositeDisposable, dataRepository: DataRepository,
                                       schedulerProvider: BaseSchedulerProvider, bitmapUtils: BitmapUtils,
                                       view: VideoFilesContract.View):
                VideoFilesPresenter = VideoFilesPresenter(compositeDisposable, dataRepository, schedulerProvider, bitmapUtils,  view)

        @JvmStatic
        @Provides
        fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()

    }

    @Binds
    abstract fun provideVideoFilesView(videoFilesActivity: VideoFilesActivity): VideoFilesContract.View
}