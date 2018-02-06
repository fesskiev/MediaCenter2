package com.fesskiev.mediacenter.utils

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


class BitmapUtils {

    companion object {

        fun saveBitmap(bitmap: Bitmap, path: File) {
            var out: FileOutputStream? = null
            try {
                out = FileOutputStream(path)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                try {
                    if (out != null) {
                        out.close()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }

        fun saveBitmap(data: ByteArray, path: File) {
            val options = BitmapFactory.Options()
            options.inMutable = true
            val bitmap = BitmapFactory.decodeByteArray(data, 0, data.size, options)
            saveBitmap(bitmap, path)
        }
    }
}