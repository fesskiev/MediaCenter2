package com.fesskiev.mediacenter.di

import android.arch.persistence.room.Room
import android.content.Context
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.domain.source.local.LocalDataSource
import com.fesskiev.mediacenter.domain.source.local.room.MediaDB
import com.fesskiev.mediacenter.domain.source.remote.RemoteDataSource
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
class DataSourceModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(context: Context, retrofit: Retrofit): RemoteDataSource = RemoteDataSource(context, retrofit)


    @Provides
    @Singleton
    fun provideRoomDatabase(context: Context): MediaDB = Room.databaseBuilder(context, MediaDB::class.java, "mediaCenterDb").build()


    @Provides
    @Singleton
    fun provideLocalDataSource(mediaDB: MediaDB): LocalDataSource = LocalDataSource(mediaDB)


    @Provides
    @Singleton
    fun provideRepository(remoteDataSource: RemoteDataSource, localDataSource: LocalDataSource): DataRepository = DataRepository(remoteDataSource, localDataSource)

}