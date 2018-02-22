package com.fesskiev.mediacenter.domain.source.local

import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import io.reactivex.Flowable
import io.reactivex.Single

interface LocalSource {

    /**
     * Audio folders methods
     */
    fun getAudioFolders(): Flowable<List<AudioFolder>>

    fun getSelectedAudioFolder(): Single<AudioFolder>

    /**
     * Video folders methods
     */
    fun getVideoFolders(): Flowable<List<VideoFolder>>

    fun getSelectedVideoFolder(): Single<VideoFolder>

    fun getSelectedAudioFile(): Single<AudioFile>

    fun getSelectedVideoFile(): Single<VideoFile>

    fun getAudioFilePlaylist(): Single<List<AudioFile>>

    fun getVideoFilePlaylist(): Single<List<VideoFile>>


    fun getArtistsList(): Single<List<String>>

    fun getGenresList(): Single<List<String>>

    fun insertAudioFolder(audioFolder: AudioFolder)

    fun updateAudioFolder(audioFolder: AudioFolder): Single<Any>

    fun deleteAudioFolderWithFiles(audioFolder: AudioFolder): Single<Int>

    fun getAudioFolderByPath(path: String): Single<AudioFolder>

    fun getSelectedAudioFiles(audioFolder: AudioFolder): Single<List<AudioFile>>

    fun updateSelectedAudioFolder(audioFolder: AudioFolder): Single<Any>

    fun updateAudioFoldersIndex(audioFolders: List<AudioFolder>): Single<Int>

    fun insertVideoFolder(videoFolder: VideoFolder)

    fun updateVideoFolder(videoFolder: VideoFolder): Single<Any>

    fun deleteVideoFolderWithFiles(videoFolder: VideoFolder): Single<Int>

    fun getVideoFolderByPath(path: String): Single<VideoFolder>

    fun getSelectedVideoFiles(videoFolder: VideoFolder): Single<List<VideoFile>>

    fun updateSelectedVideoFolder(videoFolder: VideoFolder): Single<Any>

    fun updateVideoFoldersIndex(videoFolders: List<VideoFolder>): Single<Int>

    /**
     * Audio files methods
     */
    fun insertAudioFile(audioFile: AudioFile)

    fun updateSelectedAudioFile(audioFile: AudioFile): Single<Any>

    fun updateAudioFile(audioFile: AudioFile): Single<Any>

    fun getAudioFileByPath(path: String): Single<AudioFile>

    fun getSearchAudioFiles(query: String): Single<List<AudioFile>>

    fun getGenreTracks(contentValue: String): Single<List<AudioFile>>

    fun getArtistTracks(contentValue: String): Single<List<AudioFile>>

    fun getAudioTracks(id: String): Single<List<AudioFile>>

    fun deleteAudioFile(audioFile: AudioFile): Single<Int>

    fun getAudioFiles(limit: Int, offset: Int): Flowable<List<AudioFile>>

    /**
     * Video files methods
     */
    fun insertVideoFile(videoFile: VideoFile)

    fun updateSelectedVideoFile(videoFile: VideoFile): Single<Any>

    fun updateVideoFile(videoFile: VideoFile): Single<Any>

    fun getVideoFiles(id: String): Single<List<VideoFile>>

    fun getSearchVideoFiles(query: String): Single<List<VideoFile>>

    fun getVideoFilesFrame(id: String): Single<List<String>>

    fun deleteVideoFile(videoFile: VideoFile): Single<Int>

    fun clearPlaylist(): Single<Int>

    fun getVideoFiles(limit: Int, offset: Int): Flowable<List<VideoFile>>

    /**
     * drop database
     */
    fun resetVideoContentDatabase()

    fun resetAudioContentDatabase()


    fun containAudioTrack(path: String): Boolean

    fun containAudioFolder(path: String): Boolean

    fun containVideoFolder(path: String): Boolean

    fun getFolderFilePaths(name: String): Single<List<String>>

}