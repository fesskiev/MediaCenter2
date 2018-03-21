package com.fesskiev.mediacenter.ui.media.audio

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class AudioFoldersFragmentModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideAudioPresenter(compositeDisposable: CompositeDisposable,
                                  dataRepository: DataRepository,
                                  schedulerProvider: BaseSchedulerProvider,
                                  bitmapUtils: BitmapUtils,
                                  view: AudioFoldersContact.View): AudioFoldersPresenter {
            return AudioFoldersPresenter(compositeDisposable, dataRepository,
                    schedulerProvider, bitmapUtils, view)
        }
    }

    @Binds
    abstract fun provideAudioView(audioFoldersFragment: AudioFoldersFragment): AudioFoldersContact.View

}