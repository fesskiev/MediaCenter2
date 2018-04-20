package com.fesskiev

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleObserver
import android.arch.lifecycle.OnLifecycleEvent
import android.arch.lifecycle.ProcessLifecycleOwner
import com.fesskiev.mediacenter.di.DaggerAppComponent
import com.fesskiev.mediacenter.utils.player.MediaPlayer
import com.squareup.leakcanary.LeakCanary
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import javax.inject.Inject


class App : DaggerApplication() {

    private val processLifecycleObserver = ProcessLifecycleObserver()

    @Inject
    @JvmField
    var mediaPlayer: MediaPlayer? = null

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        val appComponent = DaggerAppComponent.builder().application(this).build()
        appComponent.inject(this)
        return appComponent
    }

    override fun onCreate() {
        super.onCreate()
        setupLeakCanary()
        setupProcessLifecycle()
    }

    private fun setupProcessLifecycle() {
        ProcessLifecycleOwner.get().lifecycle.addObserver(processLifecycleObserver)
    }

    private fun setupLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return
        }
        LeakCanary.install(this)
    }

    private inner class ProcessLifecycleObserver : LifecycleObserver {

        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        fun resumed() {
            mediaPlayer?.play()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        fun paused() {
            mediaPlayer?.pause()
        }
    }
}