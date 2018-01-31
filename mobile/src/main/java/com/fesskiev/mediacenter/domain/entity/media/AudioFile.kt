package com.fesskiev.mediacenter.domain.entity.media

import android.annotation.SuppressLint
import android.arch.persistence.room.*
import android.arch.persistence.room.ForeignKey.CASCADE
import android.os.Parcelable
import com.fesskiev.mediacenter.utils.StringUtils
import kotlinx.android.parcel.Parcelize
import org.jetbrains.annotations.NotNull
import java.io.File
import java.util.*

@Parcelize
@SuppressLint("ParcelCreator")
@Entity(tableName = "AudioFiles", foreignKeys = [ForeignKey(entity = AudioFolder::class,
        parentColumns = ["id"],
        childColumns = ["folderId"],
        onDelete = CASCADE)],
        indices = [Index("folderId")])
class AudioFile() : MediaFile, Parcelable {

    @NotNull
    @PrimaryKey
    var audioFileId: String = ""
    var audioFolderId: String = ""
    var audioFilePath: File = File("")
    @Ignore
    var audioFileConvertedPath: File? = null
    var audioFileArtist: String = ""
    var audioFileTitle: String = ""
    var audioFileAlbum: String = ""
    var audioFileGenre: String = ""
    var audioFileBitrate: String = ""
    var audioFileSampleRate: String = ""
    var audioFileArtworkPath: String = ""
    var folderArtworkPath: String = ""
    var audioFileTrackNumber: Int = 0
    var audioFileDuration: Long = 0
    var audioFileSize: Long = 0
    var audioFileTimestamp: Long = 0
    var audioFileInPlayList: Boolean = false
    var audioFileIsSelected: Boolean = false
    var audioFileIsHidden: Boolean = false

    constructor(folderId: String, path: File) : this() {
        this.audioFolderId = folderId
        this.audioFileId = UUID.randomUUID().toString()

        val newPath = File(path.parent, StringUtils.replaceSymbols(path.name))
        val rename = path.renameTo(newPath)
        if (rename) {
            audioFilePath = newPath
        }
    }

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
        return audioFileArtworkPath
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
}