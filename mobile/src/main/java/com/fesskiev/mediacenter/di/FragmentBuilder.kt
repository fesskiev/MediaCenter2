package com.fesskiev.mediacenter.di

import com.fesskiev.mediacenter.ui.media.audio.AudioFragment
import com.fesskiev.mediacenter.ui.media.audio.AudioFragmentModule
import com.fesskiev.mediacenter.ui.media.files.FilesFragment
import com.fesskiev.mediacenter.ui.media.files.FilesFragmentModule
import com.fesskiev.mediacenter.ui.media.folders.FoldersFragment
import com.fesskiev.mediacenter.ui.media.folders.FoldersFragmentModule
import com.fesskiev.mediacenter.ui.media.video.VideoFragment
import com.fesskiev.mediacenter.ui.media.video.VideoFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = [AudioFragmentModule::class])
    internal abstract fun provideAudioFragmentFactory(): AudioFragment

    @ContributesAndroidInjector(modules = [VideoFragmentModule::class])
    internal abstract fun provideVideoFragmentFactory(): VideoFragment

    @ContributesAndroidInjector(modules = [FilesFragmentModule::class])
    internal abstract fun provideFilesFragmentFactory(): FilesFragment

    @ContributesAndroidInjector(modules = [FoldersFragmentModule::class])
    internal abstract fun provideFoldersFragmentFactory(): FoldersFragment
}