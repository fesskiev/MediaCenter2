package com.fesskiev.mediacenter.widgets.controls

import android.content.Context
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.fesskiev.mediacenter.R

class AudioControlLayout(context: Context): FrameLayout(context) {

    init {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_audio_control, this, true)
    }
}