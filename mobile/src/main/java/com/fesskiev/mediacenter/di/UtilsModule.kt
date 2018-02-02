package com.fesskiev.mediacenter.di

import android.content.Context
import com.fesskiev.mediacenter.utils.NotificationUtils
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class UtilsModule {

    @Provides
    @Singleton
    fun provideNotificationUtils(context: Context) : NotificationUtils {
        return NotificationUtils(context)
    }
}