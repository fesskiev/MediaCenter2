package com.fesskiev.mediacenter.widgets.items

import android.content.Context
import android.support.v7.widget.CardView
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.widget.ImageView
import com.fesskiev.mediacenter.R
import kotlinx.android.synthetic.main.layout_audio_card_view.view.*


class AudioCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    interface OnAudioCardViewListener {

        fun onPopupMenuButton(view: View)

        fun onOpenTrackList()
    }

    init {
        init(context)
    }

    private var listener: OnAudioCardViewListener? = null
    private var detector: GestureDetector? = null

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_audio_card_view, this, true)

        detector = GestureDetector(context, GestureListener())
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        detector?.onTouchEvent(ev)
        return true
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (isPointInsideView(e.rawX, e.rawY, popupMenu)) {
                listener?.onPopupMenuButton(popupMenu)
            } else {
                listener?.onOpenTrackList()
            }
            return true
        }
    }

    private fun isPointInsideView(x: Float, y: Float, view: View): Boolean {
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val viewX = location[0]
        val viewY = location[1]
        return x > viewX && x < viewX + view.width && y > viewY && y < viewY + view.height
    }

    fun setOnAudioCardViewListener(l: OnAudioCardViewListener) {
        this.listener = l
    }

    fun setAlbumName(name: String) {
        albumName.text = name
    }

    fun selectedFolderVisibility(visible : Boolean) {
        if(visible) {
            selectFolder.visibility = View.VISIBLE
        } else {
            selectFolder.visibility = View.INVISIBLE
        }
    }

    fun getAlbumCover(): ImageView {
        return albumCover
    }

}