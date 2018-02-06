package com.fesskiev.mediacenter.utils

import android.os.Environment


class Constants {

    companion object {
        val EXTERNAL_STORAGE = Environment.getExternalStorageDirectory().toString()
        val IMAGES_AUDIO_CACHE_PATH = EXTERNAL_STORAGE + "/MediaCenter/Images/Audio/"
        val IMAGES_VIDEO_CACHE_PATH = EXTERNAL_STORAGE + "/MediaCenter/Images/Video/"
    }
}