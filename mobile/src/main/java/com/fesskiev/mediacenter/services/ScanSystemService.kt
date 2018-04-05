package com.fesskiev.mediacenter.services

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.engines.TagsEngine
import com.fesskiev.mediacenter.utils.FileSystemUtils
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTERNAL_STORAGE
import com.fesskiev.mediacenter.utils.NotificationUtils
import com.fesskiev.mediacenter.utils.NotificationUtils.Companion.ACTION_STOP_SCAN
import com.fesskiev.mediacenter.utils.NotificationUtils.Companion.NOTIFICATION_SCAN_ID
import com.fesskiev.mediacenter.utils.StorageUtils
import com.fesskiev.mediacenter.utils.enums.ScanState
import com.fesskiev.mediacenter.utils.enums.ScanType
import dagger.android.DaggerService
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.IOFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import java.io.File
import java.io.FilenameFilter
import java.io.IOException
import java.util.*
import javax.inject.Inject

class ScanSystemService : DaggerService() {

    companion object {

        private const val ACTION_START_FETCH_MEDIA = "com.fesskiev.player.action.FETCH_MEDIA"
        private const val ACTION_START_FETCH_AUDIO = "com.fesskiev.player.action.START_FETCH_AUDIO"
        private const val ACTION_START_FETCH_VIDEO = "com.fesskiev.player.action.START_FETCH_VIDEO"

        private const val HANDLE_MEDIA = 0
        private const val HANDLE_VIDEO = 1
        private const val HANDLE_AUDIO = 2

        fun stopFileSystemService(context: Context) {
            val intent = Intent(context, ScanSystemService::class.java)
            context.stopService(intent)
        }

        fun startFetchMedia(context: Context) {
            val intent = Intent(context, ScanSystemService::class.java)
            intent.action = ACTION_START_FETCH_MEDIA
            context.startService(intent)
        }

        fun startFetchAudio(context: Context) {
            val intent = Intent(context, ScanSystemService::class.java)
            intent.action = ACTION_START_FETCH_AUDIO
            context.startService(intent)
        }

        fun startFetchVideo(context: Context) {
            val intent = Intent(context, ScanSystemService::class.java)
            intent.action = ACTION_START_FETCH_VIDEO
            context.startService(intent)
        }
    }

    @Inject
    @JvmField
    var repository: DataRepository? = null
    @Inject
    @JvmField
    var tagsEngine: TagsEngine? = null
    @Inject
    @JvmField
    var notificationUtils: NotificationUtils? = null
    @Inject
    @JvmField
    var fileSystemUtils: FileSystemUtils? = null

    private var scanType: ScanType? = null
    private var scanState: ScanState? = null

    private var handler: Handler? = null
    private var fetchContentThread: FetchContentThread? = null

    override fun onCreate() {
        super.onCreate()
        fetchContentThread = FetchContentThread()
        fetchContentThread?.start()
        registerNotificationReceiver()
    }

    override fun onDestroy() {
        super.onDestroy()
        fetchContentThread?.quitSafely()
        unregisterNotificationReceiver()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action != null && scanState != ScanState.SCANNING) {
            when (action) {
                ACTION_START_FETCH_MEDIA -> handler?.sendEmptyMessage(HANDLE_MEDIA)
                ACTION_START_FETCH_VIDEO -> handler?.sendEmptyMessage(HANDLE_VIDEO)
                ACTION_START_FETCH_AUDIO -> handler?.sendEmptyMessage(HANDLE_AUDIO)
            }
        }
        return Service.START_NOT_STICKY
    }

    override fun onBind(intent: Intent?): IBinder {
        throw NotImplementedError()
    }

    private fun registerNotificationReceiver() {
        val filter = IntentFilter()
        filter.addAction(ACTION_STOP_SCAN)
        registerReceiver(notificationReceiver, filter)
    }

    private fun unregisterNotificationReceiver() {
        unregisterReceiver(notificationReceiver)
    }

    private val notificationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    ACTION_STOP_SCAN -> stopScan()
                }
            }
        }
    }

    private inner class FetchContentThread : HandlerThread(FetchContentThread::class.java.simpleName,
            Process.THREAD_PRIORITY_BACKGROUND) {

        override fun onLooperPrepared() {
            super.onLooperPrepared()
            handler = object : Handler(looper) {
                override fun handleMessage(msg: Message) {
                    when (msg.what) {
                        HANDLE_MEDIA -> fetchMediaContent()
                        HANDLE_VIDEO -> fetchVideoContent()
                        HANDLE_AUDIO -> fetchAudioContent()
                    }
                }
            }
        }
    }

    private fun startScan(scanType: ScanType?) {
        scanState = ScanState.PREPARE
        startForeground(NOTIFICATION_SCAN_ID, notificationUtils?.createScanNotification())
        val storagePaths = StorageUtils.getStorageDirectories(applicationContext)
        if (storagePaths.isNotEmpty()) {
            scanState = ScanState.SCANNING
            for (path in storagePaths) {
                fileWalk(path, scanType)
            }
        }
        stopScan()
    }

    private fun stopScan() {
        scanState = ScanState.FINISHED
        stopForeground(true)
    }

    private fun fileWalk(startPath: String, scanType: ScanType?) {
        val listOfFiles = FileUtils.listFilesAndDirs(File(startPath),
                TrueFileFilter.INSTANCE,
                object : IOFileFilter {
                    override fun accept(file: File): Boolean {
                        return try {
                            isPlainDir(file)
                        } catch (ex: IOException) {
                            false
                        }
                    }

                    override fun accept(dir: File, name: String): Boolean {
                        return try {
                            isPlainDir(dir)
                        } catch (ex: IOException) {
                            false
                        }
                    }
                })

        try {
            val size = listOfFiles.size
            var count = 0f
            var progress: Float

            for (n in listOfFiles) {
                if (scanState == ScanState.FINISHED) {
                    break
                }
                if (n.absolutePath == EXTERNAL_STORAGE) {
                    continue
                }
                progress = +(count / size.toFloat()) * 100
                if (isPlainDir(n)) {
                    checkDir(n, scanType, progress)
                } else {
                    checkFile(n, scanType)
                }
                count++
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun checkFile(file: File, scanType: ScanType?) {
        when (scanType) {
            ScanType.BOTH -> {
                filterAudioFile(file)
                filterVideoFile(file)
            }
            ScanType.AUDIO -> filterAudioFile(file)
            ScanType.VIDEO -> filterVideoFile(file)
        }
    }

    private fun checkDir(dir: File, scanType: ScanType?, progress: Float) {
        when (scanType) {
            ScanType.BOTH -> {
                filterAudioFolders(dir, progress)
                filterVideoFolders(dir, progress)
            }
            ScanType.AUDIO -> filterAudioFolders(dir, progress)
            ScanType.VIDEO -> filterVideoFolders(dir, progress)
        }
    }

    private fun filterVideoFolders(directoryFile: File, progress: Float) {
        val videoPaths = directoryFile.listFiles(videoFilter())
        if (videoPaths.isNotEmpty()) {
            val videoFolder = VideoFolder()

            val filterImages = directoryFile.listFiles(folderImageFilter())
            if (filterImages.isNotEmpty()) {
                videoFolder.videoFolderImage = filterImages[0]
            }

            videoFolder.videoFolderPath = directoryFile
            videoFolder.videoFolderName = directoryFile.name
            videoFolder.videoFolderId = UUID.randomUUID().toString()
            videoFolder.videoFolderTimestamp = System.currentTimeMillis()

            notificationUtils?.updateScanNotification(videoFolder, null, progress.toInt())
            repository?.localDataSource?.insertVideoFolder(videoFolder)
            for (path in videoPaths) {
                val videoFile = tagsEngine?.retrieveVideoFile(videoFolder, path)
                if (videoFile != null) {
                    notificationUtils?.updateScanNotification(videoFolder, videoFile, progress.toInt())
                    repository?.localDataSource?.insertVideoFile(videoFile)
                }
            }
        }
    }

    private fun filterAudioFolders(directoryFile: File, progress: Float) {
        val audioPaths = directoryFile.listFiles(audioFilter())
        if (audioPaths.isNotEmpty()) {
            val audioFolder = AudioFolder()

            val filterImages = directoryFile.listFiles(folderImageFilter())
            if (filterImages.isNotEmpty()) {
                audioFolder.audioFolderImage = filterImages[0]
            }

            audioFolder.audioFolderPath = directoryFile
            audioFolder.audioFolderName = directoryFile.name
            audioFolder.audioFolderId = UUID.randomUUID().toString()
            audioFolder.audioFolderTimestamp = System.currentTimeMillis()

            notificationUtils?.updateScanNotification(audioFolder, null, progress.toInt())
            repository?.localDataSource?.insertAudioFolder(audioFolder)
            for (path in audioPaths) {
                val audioFile = tagsEngine?.retrieveAudioFile(audioFolder, path)
                if (audioFile != null) {
                    notificationUtils?.updateScanNotification(audioFolder, audioFile, progress.toInt())
                    repository?.localDataSource?.insertAudioFile(audioFile)
                }
            }
        }
    }

    private fun filterVideoFile(file: File) {

    }

    private fun filterAudioFile(file: File) {

    }

    private fun fetchAudioContent() {
        dropAudioContent()
        scanType = ScanType.AUDIO
        startScan(scanType)
    }

    private fun fetchVideoContent() {
        dropVideoContent()
        scanType = ScanType.VIDEO
        startScan(scanType)
    }

    private fun fetchMediaContent() {
        dropAudioContent()
        dropVideoContent()
        scanType = ScanType.BOTH
        startScan(scanType)
    }

    private fun dropAudioContent() {
        repository?.localDataSource?.resetAudioContentDatabase()
        fileSystemUtils?.clearAudioImagesCache()
    }

    private fun dropVideoContent() {
        repository?.localDataSource?.resetVideoContentDatabase()
        fileSystemUtils?.clearVideoImagesCache()
    }

    private fun isSymbolicLink(file: File?): Boolean {
        if (file == null) {
            throw NullPointerException("File must not be null")
        }
        val canon: File
        if (file.parent == null) {
            canon = file
        } else {
            val canonDir = file.parentFile.canonicalFile
            canon = File(canonDir, file.name)
        }
        return canon.canonicalFile != canon.absoluteFile
    }

    private fun isPlainDir(file: File): Boolean {
        return file.isDirectory && !isSymbolicLink(file)
    }

    private fun audioFilter(): FilenameFilter {
        return FilenameFilter { dir, name ->
            val lowercaseName = name.toLowerCase()
            (lowercaseName.endsWith(".mp3")
                    || lowercaseName.endsWith(".flac")
                    || lowercaseName.endsWith(".wav")
                    || lowercaseName.endsWith(".m4a")
                    || lowercaseName.endsWith(".aac")
                    || lowercaseName.endsWith(".aiff"))
        }
    }

    private fun folderImageFilter(): FilenameFilter {
        return FilenameFilter { dir, name ->
            val lowercaseName = name.toLowerCase()
            lowercaseName.endsWith(".png") || lowercaseName.endsWith(".jpg")
        }
    }

    private fun videoFilter(): FilenameFilter {
        return FilenameFilter { dir, name ->
            val lowercaseName = name.toLowerCase()
            lowercaseName.endsWith(".mp4")
                    || lowercaseName.endsWith(".ts")
                    || lowercaseName.endsWith(".mkv")
        }
    }
}