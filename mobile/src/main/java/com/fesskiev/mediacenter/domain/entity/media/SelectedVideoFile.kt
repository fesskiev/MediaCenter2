package com.fesskiev.mediacenter.domain.entity.media

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "SelectedVideoFile")
data class SelectedVideoFile(var videoFileId: String,
                             @PrimaryKey
                             var isSelected: Boolean = true)