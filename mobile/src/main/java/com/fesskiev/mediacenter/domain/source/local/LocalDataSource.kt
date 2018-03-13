package com.fesskiev.mediacenter.domain.source.local

import com.fesskiev.mediacenter.domain.entity.media.*
import com.fesskiev.mediacenter.domain.source.local.room.MediaDAO
import com.fesskiev.mediacenter.domain.source.local.room.MediaDB
import io.reactivex.Flowable
import io.reactivex.Single


class LocalDataSource(db: MediaDB) : LocalSource {

    internal enum class Irrelevant {
        INSTANCE
    }

    private val mediaDao: MediaDAO = db.mediaDAO()

    override fun getAudioFolders(): Flowable<List<AudioFolder>> {
        return mediaDao.getAudioFolders()
    }

    override fun getSelectedAudioFolder(): Single<AudioFolder> {
        return mediaDao.getSelectedAudioFolder()
    }

    override fun getVideoFolders(): Flowable<List<VideoFolder>> {
        return mediaDao.getVideoFolders()
    }

    override fun getSelectedAudioFile(): Single<AudioFile> {
        return mediaDao.getSelectedAudioFile()
    }

    override fun getAudioFilePlaylist(): Single<List<AudioFile>> {
        return mediaDao.getAudioFilePlaylist()
    }

    override fun getVideoFilePlaylist(): Single<List<VideoFile>> {
        return mediaDao.getVideoFilePlaylist()
    }

    override fun getArtistsList(): Single<List<String>> {
        return mediaDao.getArtistsList()
    }

    override fun getGenresList(): Single<List<String>> {
        return mediaDao.getGenresList()
    }

    override fun getSelectedVideoFolder(): Single<VideoFolder> {
        return mediaDao.getSelectedVideoFolder()
    }

    override fun getSelectedVideoFile(): Single<VideoFile> {
        return mediaDao.getSelectedVideoFile()
    }

    override fun insertAudioFolder(audioFolder: AudioFolder) {
        mediaDao.insertAudioFolder(audioFolder)
    }

    override fun updateAudioFolder(audioFolder: AudioFolder): Single<Any> {
        return Single.fromCallable {
            mediaDao.updateAudioFolder(audioFolder)
            Irrelevant.INSTANCE
        }
    }

    override fun deleteAudioFolderWithFiles(audioFolder: AudioFolder): Single<Int> {
        return Single.fromCallable { mediaDao.deleteAudioFolderWithFiles(audioFolder) }
    }

    override fun getAudioFolderByPath(path: String): Single<AudioFolder> {
        return mediaDao.getAudioFolderByPath(path)
    }

    override fun getSelectedAudioFiles(audioFolder: AudioFolder): Single<List<AudioFile>> {
        return mediaDao.getSelectedAudioFiles(audioFolder.audioFolderId)
    }

    override fun updateSelectedAudioFolder(audioFolder: AudioFolder): Single<Any> {
        return Single.fromCallable {
            mediaDao.updateSelectedAudioFolder(SelectedAudioFolder(audioFolder.audioFolderId))
            Irrelevant.INSTANCE
        }
    }

    override fun updateAudioFoldersIndex(audioFolders: List<AudioFolder>): Single<Int> {
        return Single.fromCallable {
            for (i in audioFolders.indices) {
                val audioFolder = audioFolders[i]
                audioFolder.audioFolderIndex = i
            }
            mediaDao.updateAudioFoldersIndex(audioFolders)
            1
        }
    }

    override fun insertVideoFolder(videoFolder: VideoFolder) {
        mediaDao.insertVideoFolder(videoFolder)
    }

    override fun updateVideoFolder(videoFolder: VideoFolder): Single<Any> {
        return Single.fromCallable {
            mediaDao.updateVideoFolder(videoFolder)
            Irrelevant.INSTANCE
        }
    }

    override fun deleteVideoFolderWithFiles(videoFolder: VideoFolder): Single<Int> {
        return Single.fromCallable { mediaDao.deleteVideoFolderWithFiles(videoFolder) }
    }

    override fun getVideoFolderByPath(path: String): Single<VideoFolder> {
        return mediaDao.getVideoFolderByPath(path)
    }

    override fun updateVideoFoldersIndex(videoFolders: List<VideoFolder>): Single<Int> {
        return Single.fromCallable {
            for (i in videoFolders.indices) {
                val videoFolder = videoFolders[i]
                videoFolder.videoFolderIndex = i
            }
            mediaDao.updateVideoFoldersIndex(videoFolders)
            1
        }
    }

    override fun insertAudioFile(audioFile: AudioFile) {
        mediaDao.insertAudioFile(audioFile)
    }

    override fun updateAudioFile(audioFile: AudioFile): Single<Any> {
        return Single.fromCallable {
            mediaDao.updateAudioFile(audioFile)
            Irrelevant.INSTANCE
        }
    }

    override fun updateSelectedAudioFile(audioFile: AudioFile): Single<Any> {
        return Single.fromCallable {
            mediaDao.updateSelectedAudioFile(SelectedAudioFile(audioFile.audioFileId))
            Irrelevant.INSTANCE
        }
    }

    override fun getAudioFileByPath(path: String): Single<AudioFile> {
        return mediaDao.getAudioFileByPath(path)
    }

    override fun getSearchAudioFiles(query: String): Single<List<AudioFile>> {
        return mediaDao.getSearchAudioFiles(query)
    }

    override fun getSearchVideoFiles(query: String): Single<List<VideoFile>> {
        return mediaDao.getSearchVideoFiles(query)
    }

    override fun getGenreTracks(contentValue: String): Single<List<AudioFile>> {
        return mediaDao.getGenreTracks(contentValue)
    }

    override fun getArtistTracks(contentValue: String): Single<List<AudioFile>> {
        return mediaDao.getArtistTracks(contentValue)
    }

    override fun getAudioTracks(id: String): Single<List<AudioFile>> {
        return mediaDao.getAudioTracks(id)
    }

    override fun deleteAudioFile(audioFile: AudioFile): Single<Int> {
        return Single.fromCallable { mediaDao.deleteAudioFile(audioFile) }
    }

    override fun insertVideoFile(videoFile: VideoFile) {
        mediaDao.insertVideoFile(videoFile)
    }

    override fun updateVideoFile(videoFile: VideoFile): Single<Any> {
        return Single.fromCallable {
            mediaDao.updateVideoFile(videoFile)
            Irrelevant.INSTANCE
        }
    }

    override fun getVideoFiles(id: String): Single<List<VideoFile>> {
        return mediaDao.getVideoFiles(id)
    }

    override fun getVideoFilesFrame(id: String): Single<List<String>> {
        return mediaDao.getVideoFilesFrame(id)
    }

    override fun deleteVideoFile(videoFile: VideoFile): Single<Int> {
        return Single.fromCallable { mediaDao.deleteVideoFile(videoFile) }
    }

    override fun clearPlaylist(): Single<Int> {
        return Single.fromCallable {
            mediaDao.clearAudioFilesPlaylist()
            mediaDao.clearVideoFilesPlaylist()
        }
    }

    override fun resetVideoContentDatabase() {
        mediaDao.dropVideoFolders()
    }

    override fun resetAudioContentDatabase() {
        mediaDao.dropAudioFolders()
    }

    override fun getFolderFilePaths(name: String): Single<List<String>> {
        return mediaDao.getFolderFilePaths(name)
    }

    override fun getSelectedVideoFiles(videoFolder: VideoFolder): Single<List<VideoFile>> {
        return mediaDao.getSelectedVideoFiles(videoFolder.videoFolderId)
    }

    override fun updateSelectedVideoFolder(videoFolder: VideoFolder): Single<Any> {
        return Single.fromCallable {
            mediaDao.updateSelectedVideoFolder(SelectedVideoFolder(videoFolder.videoFolderId))
            Irrelevant.INSTANCE
        }
    }

    override fun updateSelectedVideoFile(videoFile: VideoFile): Single<Any> {
        return Single.fromCallable {
            mediaDao.updateSelectedVideoFile(SelectedVideoFile(videoFile.videoFileId))
            Irrelevant.INSTANCE
        }
    }

    override fun getAudioFiles(limit: Int, offset: Int): Flowable<List<AudioFile>> {
        return mediaDao.getAudioFiles(limit, offset)
    }

    override fun getVideoFiles(limit: Int, offset: Int): Flowable<List<VideoFile>> {
        return mediaDao.getVideoFiles(limit, offset)
    }

    override fun containAudioTrack(path: String): Boolean {
        return mediaDao.getAudioFile(path) != null
    }

    override fun containAudioFolder(path: String): Boolean {
        return mediaDao.getAudioFolder(path) != null
    }

    override fun containVideoFolder(path: String): Boolean {
        return mediaDao.getVideoFolder(path) != null
    }
}
