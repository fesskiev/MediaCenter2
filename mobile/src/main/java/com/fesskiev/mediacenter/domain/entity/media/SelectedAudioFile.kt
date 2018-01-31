package com.fesskiev.mediacenter.domain.entity.media

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "SelectedAudioFile")
data class SelectedAudioFile(var audioFileId: String,
                             @PrimaryKey
                             var isSelected: Boolean = true)