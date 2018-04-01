package com.fesskiev.mediacenter.ui.media.audio.details

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.BitmapUtils
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
        fun provideAudioFilesPresenter(compositeDisposable: CompositeDisposable, dataRepository: DataRepository,
                                       schedulerProvider: BaseSchedulerProvider, bitmapUtils: BitmapUtils,
                                       view: AudioFilesContract.View): AudioFilesPresenter = AudioFilesPresenter(compositeDisposable, dataRepository, schedulerProvider, bitmapUtils, view)


        @JvmStatic
        @Provides
        fun provideCompositeDisposable(): CompositeDisposable = CompositeDisposable()
    }

    @Binds
    abstract fun provideAudioFilesView(audioFilesActivity: AudioFilesActivity): AudioFilesContract.View
}