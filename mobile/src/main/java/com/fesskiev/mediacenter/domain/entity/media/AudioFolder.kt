package com.fesskiev.mediacenter.domain.entity.media

import android.annotation.SuppressLint
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.io.File


@Parcelize
@SuppressLint("ParcelCreator")
@Entity(tableName = "AudioFolders")
data class AudioFolder(@NotNull
                  @PrimaryKey
                  var audioFolderId: String = "",
                  var audioFolderPath: File? = null,
                  var audioFolderImage: File? = null,
                  var audioFolderName: String = "",
                  var audioFolderIndex: Int = 0,
                  var audioFolderTimestamp: Long = 0,
                  var audioFolderSelected: Boolean = false,
                  var audioFolderHidden: Boolean = false) : MediaFolder, Parcelable, Comparable<AudioFolder> {

    override fun getId(): String {
        return audioFolderId
    }

    override fun getPath(): String {
        return audioFolderPath?.absolutePath ?: ""
    }

    override fun getFolderName(): String {
        return audioFolderName
    }

    override fun getTimestamp(): Long {
        return audioFolderTimestamp
    }

    override fun exists(): Boolean {
        return audioFolderPath?.exists() ?: false
    }

    override fun isHidden(): Boolean {
        return audioFolderHidden
    }

    override fun isSelected(): Boolean {
        return audioFolderSelected
    }

    override fun compareTo(other: AudioFolder): Int {
        if (this.audioFolderIndex < other.audioFolderIndex) {
            return -1
        }
        if (this.audioFolderIndex == other.audioFolderIndex) {
            return 0
        }
        return 1
    }
}