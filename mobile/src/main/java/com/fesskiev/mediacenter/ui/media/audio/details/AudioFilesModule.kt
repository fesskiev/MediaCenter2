package com.fesskiev.mediacenter.ui.media.audio.details

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class AudioFilesModule {

    @Module
    companion object {

        @JvmStatic
        @Provides
        fun provideAudioFilesPresenter(compositeDisposable: CompositeDisposable,
                                  dataRepository: DataRepository,
                                  schedulerProvider: BaseSchedulerProvider,
                                  view: AudioFilesContract.View): AudioFilesPresenter {
            return AudioFilesPresenter(compositeDisposable, dataRepository, schedulerProvider, view)
        }

        @JvmStatic
        @Provides
        fun provideCompositeDisposable(): CompositeDisposable {
            return CompositeDisposable()
        }
    }

    @Binds
    abstract fun provideAudioFilesView(audioFilesActivity: AudioFilesActivity): AudioFilesContract.View
}