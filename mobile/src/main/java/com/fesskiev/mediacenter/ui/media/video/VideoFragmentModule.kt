package com.fesskiev.mediacenter.ui.media.video

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class VideoFragmentModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideVideoPresenter(compositeDisposable: CompositeDisposable,
                                  dataRepository: DataRepository,
                                  schedulerProvider: BaseSchedulerProvider,
                                  view: VideoContract.View): VideoPresenter {
            return VideoPresenter(compositeDisposable, dataRepository, schedulerProvider,  view)
        }

        @JvmStatic
        @Provides
        fun provideCompositeDisposable(): CompositeDisposable {
            return CompositeDisposable()
        }
    }

    @Binds
    abstract fun provideVideoView(filesFragment: VideoFragment): VideoContract.View
}