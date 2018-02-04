package com.fesskiev.kotlinsamples.di

import com.fesskiev.mediacenter.di.FragmentBuilder
import com.fesskiev.mediacenter.ui.main.MainActivity
import com.fesskiev.mediacenter.ui.main.MainActivityModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity
}
