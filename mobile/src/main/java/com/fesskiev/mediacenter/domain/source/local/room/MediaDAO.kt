package com.fesskiev.mediacenter.domain.source.local.room

import android.arch.persistence.room.*
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import com.fesskiev.mediacenter.domain.entity.media.*

@Dao
interface MediaDAO {

    @Query("SELECT * FROM AudioFolders")
    fun getAudioFolders(): List<AudioFolder>

    @Query("SELECT AudioFolders.*, SelectedAudioFolder.isSelected FROM AudioFolders INNER JOIN SelectedAudioFolder ON AudioFolders.id = SelectedAudioFolder.audioFolderId")
    fun getSelectedAudioFolder(): AudioFolder

    @Query("SELECT * FROM VideoFolders")
    fun getVideoFolders(): List<VideoFolder>

    @Query("SELECT VideoFolders.*, SelectedVideoFolder.isSelected FROM VideoFolders INNER JOIN SelectedVideoFolder ON VideoFolders.id = SelectedVideoFolder.videoFolderId")
    fun getSelectedVideoFolder(): VideoFolder

    @Query("SELECT AudioFiles.*, SelectedAudioFile.isSelected FROM AudioFiles INNER JOIN SelectedAudioFile ON AudioFiles.fileId = SelectedAudioFile.audioFileId")
    fun getSelectedAudioFile(): AudioFile

    @Query("SELECT VideoFiles.*, SelectedVideoFile.isSelected FROM VideoFiles INNER JOIN SelectedVideoFile ON VideoFiles.fileId = SelectedVideoFile.videoFileId")
    fun getSelectedVideoFile(): VideoFile

    @Query("SELECT * FROM AudioFiles WHERE inPlayList LIKE 1")
    fun getAudioFilePlaylist(): List<AudioFile>

    @Query("SELECT * FROM VideoFiles WHERE inPlayList LIKE 1")
    fun getVideoFilePlaylist(): List<VideoFile>

    @Query("SELECT DISTINCT artist FROM AudioFiles")
    fun getArtistsList(): List<String>

    @Query("SELECT DISTINCT genre FROM AudioFiles")
    fun getGenresList(): List<String>

    @Insert(onConflict = REPLACE)
    fun insertAudioFolder(audioFolder: AudioFolder)

    @Update(onConflict = REPLACE)
    fun updateAudioFolder(audioFolder: AudioFolder)

    @Delete
    fun deleteAudioFolderWithFiles(audioFolder: AudioFolder): Int

    @Query("SELECT * FROM AudioFolders WHERE folderPath LIKE :path")
    fun getAudioFolderByPath(path: String): AudioFolder

    @Query("SELECT * FROM AudioFiles WHERE folderId LIKE :id")
    fun getSelectedAudioFiles(id: String): List<AudioFile>

    @Insert(onConflict = REPLACE)
    fun updateSelectedAudioFolder(audioFolder: SelectedAudioFolder)

    @Update(onConflict = REPLACE)
    fun updateAudioFoldersIndex(audioFolders: List<AudioFolder>)

    @Insert(onConflict = REPLACE)
    fun insertVideoFolder(videoFolder: VideoFolder)

    @Update(onConflict = REPLACE)
    fun updateVideoFolder(videoFolder: VideoFolder)

    @Delete
    fun deleteVideoFolderWithFiles(videoFolder: VideoFolder): Int

    @Query("SELECT * FROM VideoFolders WHERE folderPath LIKE :path")
    fun getVideoFolderByPath(path: String): VideoFolder

    @Update(onConflict = REPLACE)
    fun updateVideoFoldersIndex(videoFolders: List<VideoFolder>)

    @Insert(onConflict = REPLACE)
    fun updateSelectedVideoFolder(videFolder: SelectedVideoFolder)

    @Insert(onConflict = REPLACE)
    fun insertAudioFile(audioFile: AudioFile)

    @Insert(onConflict = REPLACE)
    fun updateSelectedAudioFile(selectedAudioFile: SelectedAudioFile)

    @Update(onConflict = REPLACE)
    fun updateAudioFile(audioFile: AudioFile)

    @Query("SELECT * FROM AudioFiles WHERE filePath LIKE :path")
    fun getAudioFileByPath(path: String): AudioFile

    @Query("SELECT * FROM AudioFiles WHERE title LIKE '%' || :query || '%'")
    fun getSearchAudioFiles(query: String): List<AudioFile>

    @Query("SELECT * FROM AudioFiles WHERE genre LIKE :contentValue ORDER BY trackNumber ASC")
    fun getGenreTracks(contentValue: String): List<AudioFile>

    @Query("SELECT * FROM AudioFiles WHERE artist LIKE :contentValue ORDER BY trackNumber ASC")
    fun getArtistTracks(contentValue: String): List<AudioFile>

    @Query("SELECT * FROM AudioFiles WHERE folderId LIKE :id ORDER BY trackNumber ASC")
    fun getAudioTracks(id: String): List<AudioFile>

    @Delete
    fun deleteAudioFile(audioFile: AudioFile): Int

    @Insert(onConflict = REPLACE)
    fun insertVideoFile(videoFile: VideoFile)

    @Update(onConflict = REPLACE)
    fun updateVideoFile(videoFile: VideoFile)

    @Insert(onConflict = REPLACE)
    fun updateSelectedVideoFile(selectedVideoFile: SelectedVideoFile)

    @Query("SELECT * FROM VideoFiles WHERE folderId LIKE :id")
    fun getSelectedVideoFiles(id: String): List<VideoFile>

    @Query("SELECT * FROM VideoFiles WHERE folderId LIKE :id")
    fun getVideoFiles(id: String): List<VideoFile>

    @Query("SELECT framePath FROM VideoFiles WHERE folderId LIKE :id")
    fun getVideoFilesFrame(id: String): List<String>

    @Delete
    fun deleteVideoFile(videoFile: VideoFile): Int

    @Query("UPDATE VideoFiles SET inPlayList = 0")
    fun clearVideoFilesPlaylist(): Int

    @Query("UPDATE AudioFiles SET inPlayList = 0")
    fun clearAudioFilesPlaylist(): Int

    @Query("SELECT folderPath FROM AudioFolders WHERE folderName LIKE :name")
    fun getFolderFilePaths(name: String): List<String>

    @Query("SELECT * FROM AudioFiles WHERE filePath LIKE :path")
    fun getAudioFile(path: String): AudioFile

    @Query("SELECT * FROM AudioFolders WHERE folderPath LIKE :path")
    fun getAudioFolder(path: String): AudioFolder

    @Query("SELECT * FROM VideoFolders WHERE folderPath LIKE :path")
    fun getVideoFolder(path: String): VideoFolder

    @Query("DELETE FROM VideoFolders")
    fun dropVideoFolders(): Int

    @Query("DELETE FROM VideoFiles")
    fun dropVideoFiles(): Int

    @Query("DELETE FROM AudioFolders")
    fun dropAudioFolders(): Int

    @Query("DELETE FROM AudioFiles")
    fun dropAudioFiles(): Int
}