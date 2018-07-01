package com.fesskiev.mediacenter.domain.entity.media

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "SelectedVideoFolder")
data class SelectedVideoFolder(var videoFolderId: String,
                               @PrimaryKey
                               var isSelected: Boolean = true)