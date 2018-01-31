package com.fesskiev.mediacenter.domain.source.local.room

import android.arch.persistence.room.TypeConverter
import java.io.File


class PathConverter {

    @TypeConverter
    fun stringPathToFile(path: String?): File? {
        if (path == null) {
            return null
        } else {
            return File(path)
        }
    }

    @TypeConverter
    fun filePathToString(path: File?): String? {
        return path?.absolutePath
    }
}