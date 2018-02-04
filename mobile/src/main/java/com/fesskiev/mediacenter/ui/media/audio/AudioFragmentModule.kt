package com.fesskiev.mediacenter.ui.media.audio

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class AudioFragmentModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideAudioPresenter(compositeDisposable: CompositeDisposable,
                                  dataRepository: DataRepository,
                                  schedulerProvider: BaseSchedulerProvider,
                                  view: AudioContact.View): AudioPresenter {
            return AudioPresenter(compositeDisposable, dataRepository, schedulerProvider, view)
        }
    }

    @Binds
    abstract fun provideAudioView(audioFragment: AudioFragment): AudioContact.View

}