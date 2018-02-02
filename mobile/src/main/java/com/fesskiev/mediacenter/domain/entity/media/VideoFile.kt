package com.fesskiev.mediacenter.domain.entity.media

import android.annotation.SuppressLint
import android.arch.persistence.room.Entity
import android.arch.persistence.room.ForeignKey
import android.arch.persistence.room.ForeignKey.CASCADE
import android.arch.persistence.room.Index
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import com.fesskiev.mediacenter.utils.StringUtils
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.io.File
import java.util.*


@Entity(tableName = "VideoFiles",
        foreignKeys = [ForeignKey(entity = VideoFolder::class,
                parentColumns = ["videoFolderId"],
                childColumns = ["videoFolderParentId"],
                onDelete = CASCADE)],
        indices = [Index("videoFolderParentId")])
@Parcelize
@SuppressLint("ParcelCreator")
data class VideoFile(@NotNull
                @PrimaryKey
                var videoFileId: String = "",
                var videoFolderParentId: String = "",
                var videoFilePath: File = File(""),
                var videoFileFramePath: String = "",
                var videoFileDescription: String = "",
                var videoFileResolution: String = "",
                var videoFileInPlayList: Boolean = false,
                var videoFileIsHidden: Boolean = false,
                var videoFileIsSelected: Boolean = false,
                var videoFileSize: Long = 0,
                var videoFileTimestamp: Long = 0,
                var videoFileDuration: Long = 0) : MediaFile, Parcelable {


    constructor(folderId: String, path: File) : this() {
        this.videoFolderParentId = folderId
        this.videoFileId = UUID.randomUUID().toString()

        val newPath = File(path.parent, StringUtils.replaceSymbols(path.name))
        val rename = path.renameTo(newPath)
        if (rename) {
            videoFilePath = newPath
        }
    }


    override fun getId(): String {
        return videoFileId
    }

    override fun getMediaType(): MediaType {
        return MediaType.VIDEO
    }

    override fun getTitle(): String {
        return videoFileDescription
    }

    override fun getFileName(): String {
        return videoFilePath.name
    }

    override fun getFilePath(): String {
        return videoFilePath.absolutePath
    }

    override fun getArtworkPath(): String {
        return videoFileFramePath
    }

    override fun getDuration(): Long {
        return videoFileDuration
    }

    override fun getSize(): Long {
        return videoFileSize
    }

    override fun getTimestamp(): Long {
        return videoFileTimestamp
    }

    override fun exists(): Boolean {
        return videoFilePath.exists()
    }

    override fun inPlayList(): Boolean {
        return videoFileInPlayList
    }

    override fun isSelected(): Boolean {
        return videoFileIsSelected
    }

    override fun isHidden(): Boolean {
        return videoFileIsHidden
    }

    override fun setToPlayList(inPlaylist: Boolean) {
        this.videoFileInPlayList = inPlaylist
    }
}