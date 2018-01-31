package com.fesskiev.mediacenter.domain.entity.media


interface MediaFolder {

    fun getId(): String

    fun getPath(): String

    fun getFolderName(): String

    fun getTimestamp(): Long

    fun exists(): Boolean

    fun isHidden(): Boolean

    fun isSelected(): Boolean
}