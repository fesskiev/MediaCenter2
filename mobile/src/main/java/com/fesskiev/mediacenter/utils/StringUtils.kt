package com.fesskiev.mediacenter.utils

import java.util.*


class StringUtils {

    companion object {
        fun replaceSymbols(fileName: String): String {
            return fileName.replace("[|\\?*%<\":>+'/#]".toRegex(), "")
        }

        fun humanReadableByteCount(bytes: Long, si: Boolean): String {
            val unit = if (si) 1000 else 1024
            if (bytes < unit) return bytes.toString() + " B"
            val exp = (Math.log(bytes.toDouble()) / Math.log(unit.toDouble())).toInt()
            val pre = (if (si) "kMGTPE" else "KMGTPE")[exp - 1]
            return String.format(Locale.ENGLISH, "%.1f %sB", bytes / Math.pow(unit.toDouble(), exp.toDouble()), pre)
        }

        fun getDurationString(seconds: Long): String {
            val hours = seconds / 3600
            val minutes = seconds % 3600 / 60
            val sec = seconds % 60
            return twoDigitString(hours) + ":" + twoDigitString(minutes) + ":" + twoDigitString(sec)
        }

        private fun twoDigitString(number: Long): String {

            if (number == 0L) {
                return "00"
            }

            return if (number / 10 == 0L) {
                "0$number"
            } else number.toString()

        }
    }
}