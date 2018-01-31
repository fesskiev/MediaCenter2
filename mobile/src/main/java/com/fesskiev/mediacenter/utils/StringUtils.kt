package com.fesskiev.mediacenter.utils


class StringUtils {

    companion object {
        fun replaceSymbols(fileName: String): String {
            return fileName.replace("[|\\?*%<\":>+'/#]".toRegex(), "")
        }
    }
}