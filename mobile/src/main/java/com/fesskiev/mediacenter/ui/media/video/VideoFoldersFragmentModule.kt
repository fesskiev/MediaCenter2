package com.fesskiev.mediacenter.ui.media.video

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class VideoFoldersFragmentModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideVideoPresenter(compositeDisposable: CompositeDisposable,
                                  dataRepository: DataRepository,
                                  schedulerProvider: BaseSchedulerProvider,
                                  bitmapUtils: BitmapUtils,
                                  view: VideoFoldersContract.View): VideoFoldersPresenter {
            return VideoFoldersPresenter(compositeDisposable, dataRepository,
                    schedulerProvider, bitmapUtils,  view)
        }
    }

    @Binds
    abstract fun provideVideoView(filesFoldersFragment: VideoFoldersFragment): VideoFoldersContract.View
}