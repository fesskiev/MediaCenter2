package com.fesskiev.mediacenter.services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.*
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.domain.source.DataRepository
import com.fesskiev.mediacenter.utils.Constants
import com.fesskiev.mediacenter.utils.StorageUtils
import com.fesskiev.mediacenter.utils.enums.ScanState
import com.fesskiev.mediacenter.utils.enums.ScanType
import org.apache.commons.io.FileUtils
import org.apache.commons.io.filefilter.IOFileFilter
import org.apache.commons.io.filefilter.TrueFileFilter
import java.io.File
import java.io.FilenameFilter
import java.io.IOException
import java.util.*

class ScanSystemService : Service() {

    companion object {

        private val ACTION_START_FETCH_MEDIA = "com.fesskiev.player.action.FETCH_MEDIA"
        private val ACTION_START_FETCH_AUDIO = "com.fesskiev.player.action.START_FETCH_AUDIO"
        private val ACTION_START_FETCH_VIDEO = "com.fesskiev.player.action.START_FETCH_VIDEO"

        private val HANDLE_MEDIA = 0
        private val HANDLE_VIDEO = 1
        private val HANDLE_AUDIO = 2

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

    private var repository: DataRepository? = null

    private var scanType: ScanType? = null
    private var scanState: ScanState? = null

    private var handler: Handler? = null
    private var fetchContentThread: FetchContentThread? = null

    override fun onCreate() {
        super.onCreate()
        fetchContentThread = FetchContentThread()
        fetchContentThread?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        fetchContentThread?.quitSafely()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val action = intent?.action
        if (action != null) {
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
        prepareScan()
        val storagePaths = StorageUtils.getStorageDirectories(applicationContext)
        if (storagePaths.isNotEmpty()) {
            scanning()
            for (path in storagePaths) {
                fileWalk(path, scanType)
            }
        }
        finishScan()
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
            for (n in listOfFiles) {
                if (n.absolutePath == Constants.EXTERNAL_STORAGE) {
                    continue
                }
                if (isPlainDir(n)) {
                    checkDir(n, scanType)
                } else {
                    checkFile(n, scanType)
                }
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

    private fun checkDir(dir: File, scanType: ScanType?) {
        when (scanType) {
            ScanType.BOTH -> {
                filterAudioFolders(dir)
                filterVideoFolders(dir)
            }
            ScanType.AUDIO -> filterAudioFolders(dir)
            ScanType.VIDEO -> filterVideoFolders(dir)
        }
    }

    private fun filterVideoFolders(directoryFile: File) {
        val videoPaths = directoryFile.listFiles(videoFilter())
        if (videoPaths != null && videoPaths.isNotEmpty()) {
            val videoFolder = VideoFolder()
            videoFolder.videoFolderPath = directoryFile
            videoFolder.videoFolderName = directoryFile.name
            videoFolder.videoFolderId = UUID.randomUUID().toString()
            videoFolder.videoFolderTimestamp = System.currentTimeMillis()
            repository?.localDataSource?.insertVideoFolder(videoFolder)
            for (path in videoPaths) {
                val videoFile = VideoFile(videoFolder.videoFolderId, path)
                repository?.localDataSource?.insertVideoFile(videoFile)
            }
        }
    }

    private fun filterAudioFolders(directoryFile: File) {
        val audioPaths = directoryFile.listFiles(audioFilter())
        if (audioPaths != null && audioPaths.isNotEmpty()) {

            val audioFolder = AudioFolder()

            val filterImages = directoryFile.listFiles(folderImageFilter())
            if (filterImages != null && filterImages.isNotEmpty()) {
                audioFolder.audioFolderImage = filterImages[0]
            }

            audioFolder.audioFolderPath = directoryFile
            audioFolder.audioFolderName = directoryFile.name
            audioFolder.audioFolderId = UUID.randomUUID().toString()
            audioFolder.audioFolderTimestamp = System.currentTimeMillis()

            repository?.localDataSource?.insertAudioFolder(audioFolder)
            for (path in audioPaths) {
                val audioFile = AudioFile(audioFolder.audioFolderId, path)
                val folderImage = audioFolder.audioFolderImage
                if (folderImage != null) {
                    audioFile.folderArtworkPath = folderImage.absolutePath
                }
                repository?.localDataSource?.insertAudioFile(audioFile)
            }
        }
    }

    private fun filterVideoFile(file: File) {

    }

    private fun filterAudioFile(file: File) {

    }

    private fun isPlainDir(file: File): Boolean {
        return file.isDirectory && !isSymbolicLink(file)
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

    private fun fetchAudioContent() {
        scanType = ScanType.AUDIO
        startScan(scanType)
    }

    private fun fetchVideoContent() {
        scanType = ScanType.VIDEO
        startScan(scanType)
    }

    private fun fetchMediaContent() {
        scanType = ScanType.BOTH
        startScan(scanType)
    }

    private fun prepareScan() {
        scanState = ScanState.PREPARE
    }

    private fun scanning() {
        scanState = ScanState.SCANNING
    }

    private fun finishScan() {
        scanState = ScanState.FINISHED
    }

    private fun audioFilter(): FilenameFilter {
        return FilenameFilter { dir, name ->
            val lowercaseName = name.toLowerCase()
            (lowercaseName.endsWith(".mp3") || lowercaseName.endsWith(".flac") || lowercaseName.endsWith(".wav")
                    || lowercaseName.endsWith(".m4a") || lowercaseName.endsWith(".aac") || lowercaseName.endsWith(".aiff"))
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
            lowercaseName.endsWith(".mp4") || lowercaseName.endsWith(".ts") || lowercaseName.endsWith(".mkv")
        }
    }
}