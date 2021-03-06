package com.fesskiev.mediacenter.domain.entity.media

import android.annotation.SuppressLint
import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import android.os.Parcelable
import com.fesskiev.mediacenter.utils.enums.MediaType
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.io.File

@Parcelize
@SuppressLint("ParcelCreator")
@Entity(tableName = "AudioFiles", foreignKeys = [ForeignKey(entity = AudioFolder::class,
        parentColumns = ["audioFolderId"],
        childColumns = ["audioFolderParentId"],
        onDelete = CASCADE)],
        indices = [Index("audioFolderParentId")])
data class AudioFile(@NotNull
                @PrimaryKey
                var audioFileId: String = "",
                var audioFolderParentId: String = "",
                var audioFilePath: File = File(""),
                var audioFileArtist: String = "",
                var audioFileTitle: String = "",
                var audioFileAlbum: String = "",
                var audioFileGenre: String = "",
                var audioFileBitrate: String = "",
                var audioFileSampleRate: String = "",
                var audioFileArtworkPath: String = "",
                var folderArtworkPath: String = "",
                var audioFileTrackNumber: Int = 0,
                var audioFileDuration: Long = 0,
                var audioFileSize: Long = 0,
                var audioFileTimestamp: Long = 0,
                var audioFileInPlayList: Boolean = false,
                var audioFileIsSelected: Boolean = false,
                var audioFileIsHidden: Boolean = false) : MediaFile, Parcelable, Comparable<AudioFile> {

    override fun getId(): String {
        return audioFileId
    }

    override fun getMediaType(): MediaType {
        return MediaType.AUDIO
    }

    override fun getTitle(): String {
        return audioFileTitle
    }

    override fun getFileName(): String {
        return audioFilePath.name
    }

    override fun getFilePath(): String {
        return audioFilePath.absolutePath
    }

    override fun getArtworkPath(): String {
        return if (audioFileArtworkPath.isNotEmpty()) {
            audioFileArtworkPath
        } else folderArtworkPath
    }

    override fun getDuration(): Long {
        return audioFileDuration
    }

    override fun getSize(): Long {
        return audioFileSize
    }

    override fun getTimestamp(): Long {
        return audioFileTimestamp
    }

    override fun exists(): Boolean {
        return audioFilePath.exists()
    }

    override fun inPlayList(): Boolean {
        return audioFileInPlayList
    }

    override fun isSelected(): Boolean {
        return audioFileIsSelected
    }

    override fun isHidden(): Boolean {
        return audioFileIsHidden
    }

    override fun setToPlayList(inPlaylist: Boolean) {
        this.audioFileInPlayList = inPlaylist
    }

    override fun compareTo(other: AudioFile): Int {
        if(this.audioFileTrackNumber > other.audioFileTrackNumber){
            return 1
        } else if (this.audioFileTrackNumber < other.audioFileTrackNumber){
            return -1
        }
        return 0
    }
}