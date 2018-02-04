package com.fesskiev.mediacenter.ui.media.files

import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import dagger.Binds
import dagger.Module
import dagger.Provides
import io.reactivex.disposables.CompositeDisposable

@Module
abstract class FilesFragmentModule {

    @Module
    companion object {
        @JvmStatic
        @Provides
        fun provideFilesPresenter(compositeDisposable: CompositeDisposable,
                                  dataRepository: DataRepository,
                                  schedulerProvider: BaseSchedulerProvider,
                                  view: FilesContract.View): FilesPresenter {
            return FilesPresenter(compositeDisposable, dataRepository, schedulerProvider,  view)
        }
    }

    @Binds
    abstract fun provideFilesView(filesFragment: FilesFragment):FilesContract.View

}