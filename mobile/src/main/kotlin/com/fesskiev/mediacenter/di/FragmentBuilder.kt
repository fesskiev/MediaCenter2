package com.fesskiev.mediacenter.di

import com.fesskiev.mediacenter.ui.media.audio.AudioFoldersFragment
import com.fesskiev.mediacenter.ui.media.audio.AudioFoldersFragmentModule
import com.fesskiev.mediacenter.ui.media.files.FilesFragment
import com.fesskiev.mediacenter.ui.media.files.FilesFragmentModule
import com.fesskiev.mediacenter.ui.media.folders.FoldersFragment
import com.fesskiev.mediacenter.ui.media.folders.FoldersFragmentModule
import com.fesskiev.mediacenter.ui.media.video.VideoFoldersFragment
import com.fesskiev.mediacenter.ui.media.video.VideoFoldersFragmentModule
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentBuilder {

    @ContributesAndroidInjector(modules = [AudioFoldersFragmentModule::class])
    internal abstract fun provideAudioFragmentFactory(): AudioFoldersFragment

    @ContributesAndroidInjector(modules = [VideoFoldersFragmentModule::class])
    internal abstract fun provideVideoFragmentFactory(): VideoFoldersFragment

    @ContributesAndroidInjector(modules = [FilesFragmentModule::class])
    internal abstract fun provideFilesFragmentFactory(): FilesFragment

    @ContributesAndroidInjector(modules = [FoldersFragmentModule::class])
    internal abstract fun provideFoldersFragmentFactory(): FoldersFragment
}