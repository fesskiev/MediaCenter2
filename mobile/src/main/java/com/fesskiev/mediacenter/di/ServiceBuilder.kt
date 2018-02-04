package com.fesskiev.mediacenter.di

import com.fesskiev.mediacenter.services.ScanSystemService
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ServiceBuilder {

    @ContributesAndroidInjector
    abstract fun bindScanSystemService(): ScanSystemService
}