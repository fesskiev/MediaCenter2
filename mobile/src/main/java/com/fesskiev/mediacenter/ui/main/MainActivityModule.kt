package com.fesskiev.mediacenter.ui.main

import com.fesskiev.mediacenter.utils.schedulers.BaseSchedulerProvider
import com.fesskiev.mediacenter.utils.schedulers.SchedulerProvider
import dagger.Module
import dagger.Provides

@Module
class MainActivityModule {

    @Provides
    fun provideSchedulerProvider(): BaseSchedulerProvider {
        return SchedulerProvider()
    }
}