package com.fesskiev.kotlinsamples.di

import android.app.Application
import com.fesskiev.App
import com.fesskiev.mediacenter.di.EnginesModule
import com.fesskiev.mediacenter.di.NetworkModule
import com.fesskiev.mediacenter.di.ServiceBuilder
import com.fesskiev.mediacenter.di.UtilsModule

import javax.inject.Singleton

import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dagger.android.support.AndroidSupportInjectionModule

@Singleton
@Component(modules = [AndroidSupportInjectionModule::class, AppModule::class, NetworkModule::class,
    DataSourceModule::class, EnginesModule::class, UtilsModule::class, ActivityBuilder::class,
    ServiceBuilder:: class])
interface AppComponent : AndroidInjector<DaggerApplication> {

    fun inject(app: App)

    override fun inject(instance: DaggerApplication)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }
}
