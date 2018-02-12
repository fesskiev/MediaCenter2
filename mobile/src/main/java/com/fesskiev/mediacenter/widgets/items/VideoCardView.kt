package com.fesskiev.mediacenter.widgets.items

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import com.fesskiev.mediacenter.R
import kotlinx.android.synthetic.main.layout_audio_card_view.view.*


class VideoCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    init {
        init(context)
    }

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_video_card_view, this, true)
    }


    fun selectedFolderVisibility(visible : Boolean) {
        if(visible) {
            selectFolder.visibility = View.VISIBLE
        } else {
            selectFolder.visibility = View.INVISIBLE
        }
    }
}