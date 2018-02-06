package com.fesskiev.mediacenter.engines

import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.text.TextUtils
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.utils.BitmapUtils
import com.fesskiev.mediacenter.utils.Constants.Companion.IMAGES_AUDIO_CACHE_PATH
import com.fesskiev.mediacenter.utils.Constants.Companion.IMAGES_VIDEO_CACHE_PATH
import com.fesskiev.mediacenter.utils.StringUtils
import org.jaudiotagger.audio.AudioFileIO
import org.jaudiotagger.audio.exceptions.CannotReadException
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException
import org.jaudiotagger.tag.FieldKey
import org.jaudiotagger.tag.Tag
import org.jaudiotagger.tag.TagException
import org.jaudiotagger.tag.TagOptionSingleton
import org.jaudiotagger.tag.id3.ID3v24Frames
import java.io.File
import java.io.IOException
import java.util.*
import java.util.concurrent.ThreadLocalRandom


class TagsEngine(private val context: Context) {

    fun retrieveVideoFile(videoFolder: VideoFolder, path: File): VideoFile {
        val videoFile = VideoFile()
        videoFile.videoFilePath = renameFileCorrect(path)
        videoFile.videoFolderParentId = videoFolder.videoFolderId
        videoFile.videoFileId = UUID.randomUUID().toString()

        videoFile.videoFileSize = videoFile.videoFilePath.length()
        videoFile.videoFileTimestamp = System.currentTimeMillis()

        try {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(videoFile.videoFilePath.absolutePath)

            val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
            if (duration != null) {
                videoFile.videoFileDuration = Integer.valueOf(duration).toLong()
            }

            val frame = retriever.getFrameAtTime((ThreadLocalRandom.current().nextInt(0, (videoFile.videoFileDuration * 1000000).toInt()).toLong()))
            saveFrame(frame, videoFile)

            val sb = StringBuilder()
            sb.append(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))
            sb.append("x")
            sb.append(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
            videoFile.videoFileResolution = sb.toString()

            videoFile.videoFileTitle = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
            retriever.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return videoFile
    }

    fun retrieveAudioFile(audioFolder: AudioFolder, path: File): AudioFile {
        val audioFile = AudioFile()
        audioFile.audioFilePath = renameFileCorrect(path)
        audioFile.audioFolderParentId = audioFolder.audioFolderId
        audioFile.audioFileId = UUID.randomUUID().toString()
        val folderImage = audioFolder.audioFolderImage
        if (folderImage != null) {
            audioFile.folderArtworkPath = folderImage.absolutePath
        }
        audioFile.audioFileSize = audioFile.audioFilePath.length()
        audioFile.audioFileTimestamp = System.currentTimeMillis()

        try {
            TagOptionSingleton.getInstance().isAndroid = true
            val file = AudioFileIO.read(audioFile.audioFilePath)
            val audioHeader = file.audioHeader

            audioFile.audioFileBitrate = audioHeader.bitRate + " kbps " + if (audioHeader.isVariableBitRate) "(VBR)" else "(CBR)"
            audioFile.audioFileSampleRate = audioHeader.sampleRateAsNumber.toString() + " Hz"
            audioFile.audioFileDuration = audioHeader.trackLength.toLong()

            if (audioHeader.isLossless) {
                parseLossless(file, audioFile)
            } else {
                parseMP3(file, audioFile)
            }
        } catch (e: CannotReadException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: TagException) {
            e.printStackTrace()
        } catch (e: ReadOnlyFileException) {
            e.printStackTrace()
        } catch (e: InvalidAudioFrameException) {
            e.printStackTrace()
        } finally {
            fillEmptyFields(audioFile)
        }
        return audioFile
    }

    private fun parseMP3(file: org.jaudiotagger.audio.AudioFile, audioFile: AudioFile) {
        val tag = file.tag
        if (tag != null && tag.hasCommonFields()) {
            if (tag.hasField(ID3v24Frames.FRAME_ID_TITLE)) {
                audioFile.audioFileTitle = tag.getFirst(ID3v24Frames.FRAME_ID_TITLE)
            }
            if (tag.hasField(ID3v24Frames.FRAME_ID_ARTIST)) {
                audioFile.audioFileArtist = tag.getFirst(ID3v24Frames.FRAME_ID_ARTIST)
            }
            if (tag.hasField(ID3v24Frames.FRAME_ID_ALBUM)) {
                audioFile.audioFileAlbum = tag.getFirst(ID3v24Frames.FRAME_ID_ALBUM)
            }
            if (tag.hasField(ID3v24Frames.FRAME_ID_GENRE)) {
                audioFile.audioFileGenre = tag.getFirst(ID3v24Frames.FRAME_ID_GENRE)
            }
            if (tag.hasField(ID3v24Frames.FRAME_ID_TRACK)) {
                val number = tag.getFirst(ID3v24Frames.FRAME_ID_TRACK)
                if (number != "null" && !TextUtils.isEmpty(number)) {
                    try {
                        audioFile.audioFileTrackNumber = Integer.valueOf(number)
                    } catch (e: NumberFormatException) {
                        audioFile.audioFileTrackNumber = 0
                    }
                }
            }
            saveArtwork(tag, audioFile)
        }
        fillEmptyFields(audioFile)
    }

    private fun parseLossless(file: org.jaudiotagger.audio.AudioFile, audioFile: AudioFile) {
        val flacTag = file.tag
        if (flacTag != null && flacTag.hasCommonFields()) {
            audioFile.audioFileTitle = flacTag.getFirst(FieldKey.TITLE)
            audioFile.audioFileArtist = flacTag.getFirst(FieldKey.ARTIST)
            audioFile.audioFileAlbum = flacTag.getFirst(FieldKey.ALBUM)
            audioFile.audioFileGenre = flacTag.getFirst(FieldKey.GENRE)
            val number = flacTag.getFirst(FieldKey.TRACK)
            if (!TextUtils.isEmpty(number)) {
                try {
                    audioFile.audioFileTrackNumber = Integer.valueOf(number)
                } catch (e: NumberFormatException) {
                    audioFile.audioFileTrackNumber = 0
                }
            }
            saveArtwork(flacTag, audioFile)
            fillEmptyFields(audioFile)
        }
    }

    private fun fillEmptyFields(audioFile: AudioFile) {
        if (TextUtils.isEmpty(audioFile.audioFileArtist)) {
            audioFile.audioFileArtist = context.getString(R.string.empty_audio_file_artist)
        }
        if (TextUtils.isEmpty(audioFile.audioFileTitle)) {
            audioFile.audioFileTitle = context.getString(R.string.empty_audio_file_title)
        }
        if (TextUtils.isEmpty(audioFile.audioFileAlbum)) {
            audioFile.audioFileAlbum = context.getString(R.string.empty_audio_file_album)
        }
        if (TextUtils.isEmpty(audioFile.audioFileGenre)) {
            audioFile.audioFileGenre = context.getString(R.string.empty_audio_file_genre)
        }
    }

    private fun renameFileCorrect(path: File): File {
        val newPath = File(path.parent, StringUtils.replaceSymbols(path.name))
        val rename = path.renameTo(newPath)
        if (rename) {
            return newPath
        }
        return path
    }

    private fun saveArtwork(tag: Tag, audioFile: AudioFile) {
        val artworks = tag.artworkList
        for (artwork in artworks) {
            val imageRawData = artwork?.binaryData
            if (imageRawData != null) {
                try {
                    val path = File.createTempFile(UUID.randomUUID().toString(), ".jpg", File(IMAGES_AUDIO_CACHE_PATH))

                    BitmapUtils.saveBitmap(artwork.binaryData, path)

                    audioFile.audioFileArtworkPath = path.absolutePath
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                break
            }
        }
    }

    private fun saveFrame(bitmap: Bitmap, videoFile: VideoFile) {
        try {

            val dir = File(IMAGES_VIDEO_CACHE_PATH)
            if (!dir.exists()) {
                dir.mkdirs()
            }
            val path = File.createTempFile(UUID.randomUUID().toString(), ".jpg", dir)

            BitmapUtils.saveBitmap(bitmap, path)

            videoFile.videoFileFramePath = path.absolutePath
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}