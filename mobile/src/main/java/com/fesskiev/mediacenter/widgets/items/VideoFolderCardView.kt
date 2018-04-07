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
import com.fesskiev.mediacenter.utils.isPointInsideView
import kotlinx.android.synthetic.main.layout_video_card_view.view.*


class VideoFolderCardView(context: Context, attrs: AttributeSet) : CardView(context, attrs) {

    interface OnVideoCardViewListener {

        fun onPopupMenuClick(view: View)

        fun onVideoFolderClick()
    }

    init {
        init(context)
    }

    private var listener: OnVideoCardViewListener? = null
    private lateinit var detector: GestureDetector

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_video_card_view, this, true)

        detector = GestureDetector(context, GestureListener())
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        detector.onTouchEvent(ev)
        return true
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (isPointInsideView(e.rawX, e.rawY, popupMenu)) {
                listener?.onPopupMenuClick(popupMenu)
            } else {
                listener?.onVideoFolderClick()
            }
            return true
        }
    }

    fun setOnVideoCardViewListener(l: OnVideoCardViewListener) {
        this.listener = l
    }

    fun selectedFolderVisibility(visible: Boolean) {
        if (visible) {
            selectFolder.visibility = View.VISIBLE
        } else {
            selectFolder.visibility = View.INVISIBLE
        }
    }

    fun getVideoFrame(): ImageView {
        return videoFrame
    }
}