package com.fesskiev.mediacenter.utils


class StringUtils {

    companion object {
        fun replaceSymbols(fileName: String): String {
            return fileName.replace("[|\\?*%<\":>+'/#]".toRegex(), "")
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
                "0" + number
            } else number.toString()

        }
    }
}