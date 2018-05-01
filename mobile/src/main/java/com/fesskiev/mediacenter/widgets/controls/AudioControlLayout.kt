package com.fesskiev.mediacenter.widgets.controls

import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import android.widget.SeekBar
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.widgets.buttons.PlayPauseButton
import kotlinx.android.synthetic.main.layout_audio_control.view.*
import java.util.*

class AudioControlLayout(context: Context) : FrameLayout(context) {

    interface OnAudioControlListener {

        fun changedPlaying(play: Boolean)

        fun changedVolume(volume: Int)

        fun changedSeek(seek: Int)

        fun changedPitchShift(value: Int)

        fun changedTempo(value: Int)

        fun onNextTrack()

        fun onPreviousTrack()
    }

    private var listener: OnAudioControlListener? = null

    init {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_audio_control, this, true)
        audioControlView.setOnAudioVolumeSeekListener(object : AudioControlView.OnAudioVolumeSeekListener {
            override fun changeVolumeStart(volume: Int) {
                listener?.changedVolume(volume)
            }

            override fun changeVolumeFinish() {

            }

            override fun changeSeekStart(seek: Int) {
                listener?.changedSeek(seek)
            }

            override fun changeSeekFinish() {

            }
        })

        playPauseButton.setOnPlayPauseClickListener(object : PlayPauseButton.OnPlayPauseClickListener {
            override fun onPlay(play: Boolean) {
                listener?.changedPlaying(play)
            }
        })
        nextTrack.setOnClickListener { listener?.onNextTrack() }
        previousTrack.setOnClickListener({ listener?.onPreviousTrack() })

        seekTempo.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                listener?.changedTempo(progress)
                tempoValue.text = String.format(Locale.ENGLISH, "%1$.2f X", progress.toFloat() / 50)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        seekTempo.setOnTouchListener({ v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
            }
            v.onTouchEvent(event)
            true
        })

        seekPitchShift.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                listener?.changedPitchShift(progress)
                pitchShiftValue.text = String.format(Locale.ENGLISH, "%1$.2f X", progress.toFloat() / 50)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })
        seekPitchShift.setOnTouchListener({ v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> v.parent.requestDisallowInterceptTouchEvent(true)
                MotionEvent.ACTION_UP -> v.parent.requestDisallowInterceptTouchEvent(false)
            }
            v.onTouchEvent(event)
            true
        })
    }

    fun setTrackInformation(audioFile: AudioFile) {
        album.text = audioFile.audioFileAlbum
        genre.text = audioFile.audioFileGenre

        val sb = StringBuilder()
        sb.append(audioFile.audioFileSampleRate)
        sb.append("::")
        sb.append(audioFile.audioFileBitrate)
        sb.append("::")
        sb.append(audioFile.getFilePath())
        /**
         * http://stackoverflow.com/questions/3332924/textview-marquee-not-working?noredirect=1&lq=1
         */
        trackDescription.isSelected = true
        trackDescription.text = sb.toString()
    }

    fun setArtwork(bitmap: Bitmap) {
        cover.setImageBitmap(bitmap)
    }
}