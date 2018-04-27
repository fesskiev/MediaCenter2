package com.fesskiev.mediacenter.widgets.controls

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.fesskiev.mediacenter.R
import kotlinx.android.synthetic.main.layout_audio_control.view.*

class AudioControlLayout(context: Context): FrameLayout(context), AudioControlView.OnAudioVolumeSeekListener {

    init {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_audio_control, this, true)
        audioControlView.setOnAudioVolumeSeekListener(this)
        playPauseButton.setOnClickListener({

        })
        nextTrack.setOnClickListener {

        }
        previousTrack.setOnClickListener({

        })
    }

    override fun changeVolumeStart(volume: Int) {

    }

    override fun changeVolumeFinish() {

    }

    override fun changeSeekStart(seek: Int) {

    }

    override fun changeSeekFinish() {

    }
}