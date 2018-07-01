package com.fesskiev.mediacenter.domain.source.local.room

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import com.fesskiev.mediacenter.domain.entity.media.*


@Database(entities = [AudioFolder::class, AudioFile::class, VideoFolder::class, VideoFile::class,
    SelectedAudioFile::class, SelectedAudioFolder::class, SelectedVideoFolder::class,
    SelectedVideoFile::class], version = 1)
@TypeConverters(PathConverter::class)
abstract class MediaDB : RoomDatabase() {

    abstract fun mediaDAO(): MediaDAO

}