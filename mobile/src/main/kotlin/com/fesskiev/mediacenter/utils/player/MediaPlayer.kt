package com.fesskiev.mediacenter.utils.player

import com.fesskiev.engine.FFmpegEngine
import com.fesskiev.engine.SuperpoweredEngine
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.engines.ExoPlayerEngine
import com.fesskiev.mediacenter.utils.enums.MediaType

class MediaPlayer(private val ffmpegEngine: FFmpegEngine,
                  private val superpoweredEngine: SuperpoweredEngine,
                  private val exoPlayerEngine: ExoPlayerEngine) : Playable {

    private val fileListIterator: MediaFileListIterator = MediaFileListIterator()
    private var isPlaying: Boolean = false
    private var currentMediaFile: MediaFile? = null
    private var currentMediaFilesList: List<MediaFile>? = null
    private var position: Int = -1


    override fun open(mediaFile: MediaFile) {
        if (mediaFile.getMediaType() == MediaType.AUDIO) {
            superpoweredEngine.openAudioFile(mediaFile.getFilePath())
            superpoweredEngine.togglePlayback()
        }
    }

    override fun next(): MediaFile? {
        if (fileListIterator.hasNext()) {
            val mediaFile = fileListIterator.next()
            if (mediaFile != null) {
                currentMediaFile = mediaFile
            }
        }
        return currentMediaFile
    }

    override fun previous(): MediaFile? {
        if (fileListIterator.hasPrevious()) {
            val mediaFile = fileListIterator.previous()
            if (mediaFile != null) {
                currentMediaFile = mediaFile
            }
        }
        return currentMediaFile
    }

    override fun play() {
        isPlaying = true
        superpoweredEngine.togglePlayback()
    }

    override fun pause() {
        isPlaying = false
        superpoweredEngine.togglePlayback()
    }

    override fun seek(seek: Int) {
        superpoweredEngine.setSeek(seek)
    }

    override fun position(position: Int) {
        superpoweredEngine.setPosition(position)
    }

    override fun pitchShift(pitchShift: Int) {
        superpoweredEngine.setPitchShift(pitchShift)
    }

    override fun tempo(tempo: Double) {
        superpoweredEngine.setTempo(tempo)
    }

    override fun volume(volume: Float) {
        superpoweredEngine.setVolume(volume)
    }

    override fun shuffle() {
        currentMediaFilesList?.shuffled()
    }

    override fun isPlaying(): Boolean {
        return isPlaying
    }

    override fun background() {
        superpoweredEngine.onBackground()
    }

    override fun foreground() {
        superpoweredEngine.onForeground()
    }

    override fun shutdown() {
        superpoweredEngine.unregisterCallback()
        superpoweredEngine.onDestroyAudioPlayer()
    }

    fun setCurrentMediaFile(mediaFile: MediaFile) {
        currentMediaFile = mediaFile
        fileListIterator.findPosition()
    }

    fun setCurrentMediaFilesList(mediaFiles: List<MediaFile>) {
        currentMediaFilesList = mediaFiles
        fileListIterator.findPosition()
    }

    private inner class MediaFileListIterator : ListIterator<MediaFile?> {

        override fun hasNext(): Boolean {
            return !lastTrack()
        }

        override fun hasPrevious(): Boolean {
            return !firstTrack()
        }

        override fun next(): MediaFile? {
            nextIndex()
            return currentMediaFilesList?.get(position)
        }

        override fun previous(): MediaFile? {
            previousIndex()
            return currentMediaFilesList?.get(position)
        }

        override fun nextIndex(): Int {
            return position++
        }

        override fun previousIndex(): Int {
            return position--
        }

        fun lastTrack(): Boolean {
            return if (currentMediaFilesList == null) {
                true
            } else position == currentMediaFilesList!!.size - 1
        }

        fun firstTrack(): Boolean {
            return if (currentMediaFilesList == null) {
                true
            } else position == 0
        }

        fun findPosition() {
            if (currentMediaFilesList != null && currentMediaFilesList!!.contains(currentMediaFile)) {
                position = currentMediaFilesList!!.indexOf(currentMediaFile)
            }
        }
    }
}