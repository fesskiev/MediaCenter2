package com.fesskiev.mediacenter.domain.entity.media

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "SelectedAudioFolder")
data class SelectedAudioFolder(var audioFolderId: String,
                               @PrimaryKey
                               var isSelected: Boolean = true)