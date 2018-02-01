package com.fesskiev.mediacenter.domain.entity.media

import android.annotation.SuppressLint
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.io.File

@Parcelize
@SuppressLint("ParcelCreator")
class VideoFolder(@NotNull
                  @PrimaryKey
                  var videoFolderId: String = "",
                  var videoFolderPath: File? = null,
                  var videoFolderImage: File? = null,
                  var videoFolderName: String = "",
                  var videoFolderIndex: Int = 0,
                  var videoFolderTimestamp: Long = 0,
                  var videoFolderSelected: Boolean = false,
                  var videoFolderHidden: Boolean = false) : MediaFolder, Parcelable, Comparable<VideoFolder> {


    override fun getId(): String {
        return videoFolderId
    }

    override fun getPath(): String {
        return videoFolderPath!!.absolutePath
    }

    override fun getFolderName(): String {
        return videoFolderName
    }

    override fun getTimestamp(): Long {
        return videoFolderTimestamp;
    }

    override fun exists(): Boolean {
        return videoFolderPath!!.exists()
    }

    override fun isHidden(): Boolean {
        return videoFolderHidden
    }

    override fun isSelected(): Boolean {
        return videoFolderSelected
    }

    override fun compareTo(other: VideoFolder): Int {
        if (this.videoFolderIndex < other.videoFolderIndex) {
            return -1
        }
        if (this.videoFolderIndex == other.videoFolderIndex) {
            return 0
        }
        return 1
    }
}