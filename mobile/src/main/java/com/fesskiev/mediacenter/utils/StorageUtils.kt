package com.fesskiev.mediacenter.utils

import android.content.Context
import android.os.Build
import android.os.Environment
import android.text.TextUtils
import java.io.File
import java.util.*
import java.util.regex.Pattern


class StorageUtils {

    companion object {

        private val DIR_SEPARATOR = Pattern.compile("/")

        /**
         * Returns all available SD-Cards in the system (include emulated)
         *
         *
         * Warning: Hack! Based on Android source code of version 4.3 (API 18)
         * Because there is no standard way to get it.
         * Edited by hendrawd
         *
         * @return paths to all available SD-Cards in the system (include emulated)
         */
        fun getStorageDirectories(context: Context): Array<String> {
            // Final set of paths
            val rv = HashSet<String>()
            // Primary physical SD-CARD (not emulated)
            val rawExternalStorage = System.getenv("EXTERNAL_STORAGE")
            // All Secondary SD-CARDs (all exclude primary) separated by ":"
            val rawSecondaryStoragesStr = System.getenv("SECONDARY_STORAGE")
            // Primary emulated SD-CARD
            val rawEmulatedStorageTarget = System.getenv("EMULATED_STORAGE_TARGET")
            if (TextUtils.isEmpty(rawEmulatedStorageTarget)) {
                //fix of empty raw emulated storage on marshmallow
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    val files = context.getExternalFilesDirs(null)
                    for (file in files) {
                        if (file == null) continue
                        val applicationSpecificAbsolutePath = file.absolutePath
                        val emulatedRootPath = applicationSpecificAbsolutePath.substring(0, applicationSpecificAbsolutePath.indexOf("Android/data"))
                        rv.add(emulatedRootPath)
                    }
                } else {
                    // Device has physical external storage; use plain paths.
                    if (TextUtils.isEmpty(rawExternalStorage)) {
                        // EXTERNAL_STORAGE undefined; falling back to default.
                        rv.addAll(Arrays.asList(*getPhysicalPaths()))
                    } else {
                        rv.add(rawExternalStorage)
                    }
                }
            } else {
                // Device has emulated storage; external storage paths should have
                // userId burned into them.
                val rawUserId: String

                val path = Environment.getExternalStorageDirectory().absolutePath
                val folders = DIR_SEPARATOR.split(path)
                val lastFolder = folders[folders.size - 1]
                var isDigit = false
                try {
                    Integer.valueOf(lastFolder)
                    isDigit = true
                } catch (ignored: NumberFormatException) {

                }

                rawUserId = if (isDigit) lastFolder else ""
                // /storage/emulated/0[1,2,...]
                if (TextUtils.isEmpty(rawUserId)) {
                    rv.add(rawEmulatedStorageTarget)
                } else {
                    rv.add(rawEmulatedStorageTarget + File.separator + rawUserId)
                }
            }
            // Add all secondary storages
            if (!TextUtils.isEmpty(rawSecondaryStoragesStr)) {
                // All Secondary SD-CARDs splited into array
                val rawSecondaryStorages = rawSecondaryStoragesStr.split(File.pathSeparator.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
                Collections.addAll(rv, *rawSecondaryStorages)
            }
            return rv.toTypedArray()
        }

        /**
         * @return physicalPaths based on phone model
         */
        private fun getPhysicalPaths(): Array<String> {
            return arrayOf("/storage/sdcard0", "/storage/sdcard1", //Motorola Xoom
                    "/storage/extsdcard", //Samsung SGS3
                    "/storage/sdcard0/external_sdcard", //User request
                    "/mnt/extsdcard", "/mnt/sdcard/external_sd", //Samsung galaxy family
                    "/mnt/external_sd", "/mnt/media_rw/sdcard1", //4.4.2 on CyanogenMod S3
                    "/removable/microsd", //Asus transformer prime
                    "/mnt/emmc", "/storage/external_SD", //LG
                    "/storage/ext_sd", //HTC One Max
                    "/storage/removable/sdcard1", //Sony Xperia Z1
                    "/data/sdext", "/data/sdext2", "/data/sdext3", "/data/sdext4", "/sdcard1", //Sony Xperia Z
                    "/sdcard2", //HTC One M8s
                    "/storage/microsd"                  //ASUS ZenFone 2
            )
        }

    }
}