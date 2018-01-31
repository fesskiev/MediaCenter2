package com.fesskiev.mediacenter.domain.entity.media


interface MediaFile {

    fun getId(): String

    fun getMediaType(): MediaType

    fun getTitle(): String

    fun getFileName(): String

    fun getFilePath(): String

    fun getArtworkPath(): String

    fun getDuration(): Long

    fun getSize(): Long

    fun getTimestamp(): Long

    fun exists(): Boolean

    fun inPlayList(): Boolean

    fun isSelected(): Boolean

    fun isHidden(): Boolean

    fun setToPlayList(inPlaylist: Boolean)
}