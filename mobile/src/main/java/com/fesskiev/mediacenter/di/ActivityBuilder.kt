package com.fesskiev.mediacenter.di

import com.fesskiev.mediacenter.ui.main.MainActivity
import com.fesskiev.mediacenter.ui.main.MainActivityModule
import com.fesskiev.mediacenter.ui.media.audio.details.AudioFilesActivity
import com.fesskiev.mediacenter.ui.media.audio.details.AudioFilesModule
import com.fesskiev.mediacenter.ui.media.video.details.VideoFilesActivity
import com.fesskiev.mediacenter.ui.media.video.details.VideoFilesModule
import com.fesskiev.mediacenter.ui.playlist.PlaylistActivity
import com.fesskiev.mediacenter.ui.playlist.PlaylistModule

import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder {

    @ContributesAndroidInjector(modules = [MainActivityModule::class, FragmentBuilder::class])
    abstract fun bindMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [VideoFilesModule::class])
    abstract fun bindVideoFilesActivity(): VideoFilesActivity

    @ContributesAndroidInjector(modules = [AudioFilesModule::class])
    abstract fun bindAudioFilesActivity(): AudioFilesActivity

    @ContributesAndroidInjector(modules = [PlaylistModule::class])
    abstract fun bindPlaylistActivity(): PlaylistActivity
}
