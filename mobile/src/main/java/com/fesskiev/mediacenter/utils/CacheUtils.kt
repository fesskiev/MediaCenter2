package com.fesskiev.mediacenter.utils

import com.fesskiev.mediacenter.utils.Constants.Companion.IMAGES_AUDIO_CACHE_PATH
import com.fesskiev.mediacenter.utils.Constants.Companion.IMAGES_VIDEO_CACHE_PATH
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException


class CacheUtils {

    companion object {

        fun clearAudioImagesCache() {
            val folder = File(IMAGES_AUDIO_CACHE_PATH)
            if (!folder.exists()) {
                return
            }
            try {
                FileUtils.cleanDirectory(folder)
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        fun clearVideoImagesCache() {
            val folder = File(IMAGES_VIDEO_CACHE_PATH)
            if (!folder.exists()) {
                return
            }
            try {
                FileUtils.cleanDirectory(folder)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}