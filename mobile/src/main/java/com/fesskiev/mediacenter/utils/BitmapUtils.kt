package com.fesskiev.mediacenter.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Path
import android.util.LruCache
import android.support.v7.graphics.Palette
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import io.reactivex.Single
import okhttp3.OkHttpClient
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import android.graphics.drawable.BitmapDrawable
import android.support.v4.content.ContextCompat
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder

class BitmapUtils(private var context: Context, private var okHttpClient: OkHttpClient) {

    companion object {

        private const val KEY_NO_COVER_FOLDER_ICON = "KEY_FOLDER"
        private const val KEY_NO_COVER_TRACK_ICON = "KEY_TRACK"
        private const val KEY_NO_COVER_PLAYER_ICON = "KEY_PLAYER"

        private const val WIDTH = 140 * 3
        private const val HEIGHT = 140 * 3
    }

    private var bitmapLruCache: LruCache<String, Bitmap>

    init {
        val maxMemory = (Runtime.getRuntime().maxMemory() / 1024).toInt()
        val cacheSize = maxMemory / 4
        bitmapLruCache = object : LruCache<String, Bitmap>(cacheSize) {
            override fun sizeOf(key: String, bitmap: Bitmap): Int {
                return bitmap.byteCount / 1024
            }
        }
    }

    private fun getNoCoverTrackBitmap(): Bitmap {
        val bitmap = getBitmapFromMemCache(KEY_NO_COVER_TRACK_ICON)
        if (bitmap == null) {
            val decodeBitmap = decodeBitmapFromResource(R.drawable.ic_no_track)
            addBitmapToMemoryCache(KEY_NO_COVER_TRACK_ICON, decodeBitmap)
            return decodeBitmap
        }
        return bitmap
    }

    private fun getNoCoverFolderBitmap(): Bitmap {
        val bitmap = getBitmapFromMemCache(KEY_NO_COVER_FOLDER_ICON)
        if (bitmap == null) {
            val decodeBitmap = decodeBitmapFromResource(R.drawable.ic_no_folder)
            addBitmapToMemoryCache(KEY_NO_COVER_FOLDER_ICON, decodeBitmap)
            return decodeBitmap
        }
        return bitmap
    }

    private fun getNoCoverAudioPlayer(): Bitmap {
        val bitmap = getBitmapFromMemCache(KEY_NO_COVER_PLAYER_ICON)
        if (bitmap == null) {
            val decodeBitmap = decodeBitmapFromResource(R.drawable.ic_no_folder)
            addBitmapToMemoryCache(KEY_NO_COVER_PLAYER_ICON, decodeBitmap)
            return decodeBitmap
        }
        return bitmap
    }

    private fun getBitmapFromPath(path: String?): Bitmap {
        val bitmap = getBitmapFromMemCache(path)
        if (bitmap == null) {
            val decodeBitmap = decodeBitmapFromFile(path)
            addBitmapToMemoryCache(path, decodeBitmap)
            return decodeBitmap
        }
        return bitmap
    }

    fun addBitmapToMemoryCache(key: String?, bitmap: Bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            bitmapLruCache.put(key, bitmap)
        }
    }

    fun getBitmapFromMemCache(key: String?): Bitmap? {
        return bitmapLruCache.get(key)
    }

//    fun getCoverBitmapFromURL(url: String?): Single<Bitmap> {
//            if (url != null) {
//                val request = Request.Builder().url(url).build()
//                val response = okHttpClient.newCall(request).execute()
//                return Single.fromCallable(decodeBitmapFromInputStream(response.body()!!.byteStream()))
//            } else {
//                return getNoCoverTrackBitmap()
//            }
//    }

    fun loadBitmap(mediaFile: MediaFile): Single<Bitmap> {
        val path = findPath(mediaFile)
        return Single.create { e ->
            if (path != null) {
                val bitmap = getBitmapFromPath(path)
                e.onSuccess(bitmap)
            } else {
                val bitmap = getNoCoverTrackBitmap()
                e.onSuccess(bitmap)
            }
        }
    }

    fun loadAudioPlayerArtwork(audioFile: AudioFile): Single<Bitmap> {
        val mediaArtworkPath = findMediaFileArtworkPath(audioFile)
        return Single.create { e ->
            if (mediaArtworkPath != null) {
                val bitmap = getBitmapFromPath(mediaArtworkPath)
                e.onSuccess(bitmap)
            } else {
                val bitmap = getNoCoverAudioPlayer()
                e.onSuccess(bitmap)
            }
        }
    }

    fun getMediaFileArtwork(mediaFile: MediaFile): Single<Bitmap> {
        val mediaArtworkPath = findMediaFileArtworkPath(mediaFile)
        return Single.create { e ->
            if (mediaArtworkPath != null && mediaArtworkPath.isNotEmpty()) {
                val bitmap = getCircularBitmap(getBitmapFromPath(mediaArtworkPath))
                e.onSuccess(bitmap)
            } else {
                val bitmap = getCircularBitmap(getNoCoverTrackBitmap())
                e.onSuccess(bitmap)
            }
        }
    }

    fun getAudioFolderArtwork(audioFolder: AudioFolder): Single<Bitmap> {
        return Single.create { e ->
            val coverFile = findAudioFolderArtworkPath(audioFolder)
            if (coverFile != null && coverFile.isNotEmpty()) {
                val bitmap = getBitmapFromPath(coverFile)
                e.onSuccess(bitmap)
            } else {
                val bitmap = getNoCoverFolderBitmap()
                e.onSuccess(bitmap)
            }
        }
    }

    fun loadVideoFileFrame(path: String?): Single<Bitmap> {
        return Single.create { e ->
            if (path != null) {
                val bitmap = getBitmapFromPath(path)
                e.onSuccess(bitmap)
            } else {
                val bitmap = getNoCoverTrackBitmap()
                e.onSuccess(bitmap)
            }
        }
    }

    fun loadVideoFolderArtwork(videoFolder: VideoFolder): Single<Bitmap> {
        return Single.create { e ->
            val path = videoFolder.videoFolderImage?.absolutePath
            if (path != null) {
                val bitmap = getBitmapFromPath(path)
                e.onSuccess(bitmap)
            } else {
                val bitmap = getNoCoverFolderBitmap()
                e.onSuccess(bitmap)
            }
        }
    }

    private fun findMediaFileArtworkPath(mediaFile: MediaFile?): String? {
        if (mediaFile != null) {
            return mediaFile.getArtworkPath()
        }
        return null
    }

    private fun findAudioFolderArtworkPath(audioFolder: AudioFolder): String? {
        val artworkFile = audioFolder.audioFolderImage
        return artworkFile?.absolutePath
    }

    private fun findPath(mediaFile: MediaFile?): String? {
        if (mediaFile != null) {
            val mediaArtworkPath = findMediaFileArtworkPath(mediaFile)
            if (mediaArtworkPath != null) {
                return mediaArtworkPath
            }
        }
        return null
    }

    fun getAudioFilePalette(audioFile: AudioFile): Single<PaletteColor> {
        return Single.create { e ->
            val coverPath = findMediaFileArtworkPath(audioFile)
            if (coverPath != null) {
                e.onSuccess(PaletteColor(context, Palette
                        .from(decodeBitmapFromFile(coverPath))
                        .generate()))
            } else {
                val bitmap = getNoCoverAudioPlayer()
                e.onSuccess(PaletteColor(context, Palette
                        .from(bitmap)
                        .generate()))
            }
        }
    }

    fun getAudioFolderPalette(audioFolder: AudioFolder): Single<PaletteColor> {
        return Single.create { e ->
            val coverPath = findAudioFolderArtworkPath(audioFolder)
            if (coverPath != null) {
                e.onSuccess(PaletteColor(context, Palette
                        .from(decodeBitmapFromFile(coverPath))
                        .generate()))
            } else {
                val bitmap = getNoCoverFolderBitmap()
                e.onSuccess(PaletteColor(context, Palette
                        .from(bitmap)
                        .generate()))
            }
        }
    }

    fun decodeBitmapFromInputStream(inputStream: InputStream): Bitmap {
        val options = BitmapFactory.Options()

        options.inMutable = true
        options.inJustDecodeBounds = false

        return BitmapFactory.decodeStream(inputStream, null, options)
    }

    private fun decodeBitmapFromResource(res: Int): Bitmap {
        val drawable = ContextCompat.getDrawable(context, res)
        val bitmap: Bitmap
        if (drawable is BitmapDrawable) {
            if (drawable.bitmap != null) {
                return drawable.bitmap
            }
        }
        bitmap = if (drawable!!.intrinsicHeight <= 0 || drawable.intrinsicHeight <= 0) {
            Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
        } else {
            Bitmap.createBitmap(drawable.intrinsicWidth,
                    drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        }
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    fun decodeBitmapFromFile(path: String?): Bitmap {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeFile(path, options)
        options.inSampleSize = calculateInSampleSize(options, WIDTH, HEIGHT)
        options.inMutable = true
        options.inJustDecodeBounds = false
        return BitmapFactory.decodeFile(path, options)
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while (halfHeight / inSampleSize > reqHeight && halfWidth / inSampleSize > reqWidth) {
                inSampleSize *= 2
            }
            // This offers some additional logic in case the image has a strange
            // aspect ratio. For example, a panorama may have a much larger
            // width than height. In these cases the total pixels might still
            // end up being too large to fit comfortably in memory, so we should
            // be more aggressive with sample down the image (=larger inSampleSize).

            var totalPixels = (width * height / inSampleSize).toLong()

            // Anything more than 2x the requested pixels we'll sample down further
            val totalReqPixelsCap = (reqWidth * reqHeight * 2).toLong()

            while (totalPixels > totalReqPixelsCap) {
                inSampleSize *= 2
                totalPixels /= 2
            }
        }
        return inSampleSize
    }


    inner class PaletteColor(context: Context, palette: Palette) {

        val vibrant: Int
        val vibrantLight: Int
        val vibrantDark: Int
        val muted: Int
        val mutedLight: Int
        val mutedDark: Int

        init {
            val defaultValue = ContextCompat.getColor(context, R.color.secondary_text)
            vibrant = palette.getVibrantColor(defaultValue)
            vibrantLight = palette.getLightVibrantColor(defaultValue)
            vibrantDark = palette.getDarkVibrantColor(defaultValue)
            muted = palette.getMutedColor(defaultValue)
            mutedLight = palette.getLightMutedColor(defaultValue)
            mutedDark = palette.getDarkMutedColor(defaultValue)
        }

        override fun toString(): String {
            return "PaletteColor{" +
                    "vibrant=" + vibrant +
                    ", vibrantLight=" + vibrantLight +
                    ", vibrantDark=" + vibrantDark +
                    ", muted=" + muted +
                    ", mutedLight=" + mutedLight +
                    ", mutedDark=" + mutedDark +
                    '}'.toString()
        }
    }

    fun getCircularBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val outputBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)

        val path = Path()
        path.addCircle((width / 2).toFloat(), (height / 2).toFloat(), Math.min(width, height / 2).toFloat(), Path.Direction.CCW)

        val canvas = Canvas(outputBitmap)
        canvas.clipPath(path)
        canvas.drawBitmap(bitmap, 0f, 0f, null)
        return outputBitmap
    }

    fun clearCache() {
        bitmapLruCache.evictAll()
    }


    fun saveBitmap(bitmap: Bitmap, path: File) {
        var out: FileOutputStream? = null
        try {
            out = FileOutputStream(path)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            try {
                out?.close()
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