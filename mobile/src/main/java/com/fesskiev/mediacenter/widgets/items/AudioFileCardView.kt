package com.fesskiev.mediacenter.widgets.items

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.MotionEvent
import android.widget.FrameLayout
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.utils.isPointInsideView
import kotlinx.android.synthetic.main.layout_media_file_card_view.view.*


class AudioFileCardView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {

    interface OnFileCardListener {

        fun onDeleteClick()

        fun onEditClick()

        fun onPlaylistClick()

        fun onClick()

        fun onAnimateChanged(view: AudioFileCardView, open: Boolean)
    }

    companion object {
        private const val MIN_DISTANCE = 50
        private const val DURATION = 200.toLong()
    }

    init {
        init(context)
    }

    private lateinit var detector: GestureDetector
    private var listener: OnFileCardListener? = null
    private var isOpen: Boolean = false
    private var x1: Float = 0.toFloat()
    private var x2: Float = 0.toFloat()

    private fun init(context: Context) {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        inflater.inflate(R.layout.layout_audio_file_card_view, this, true)

        detector = GestureDetector(context, GestureListener())
    }

    override fun dispatchTouchEvent(event: MotionEvent?): Boolean {
        detector.onTouchEvent(event)
        when (event?.actionMasked) {
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                x2 = event.x
                val deltaX = x2 - x1
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    if (x2 > x1) {
                        animateSlidingContainer(false)
                    } else {
                        animateSlidingContainer(true)
                    }
                }
            }
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
                return true
            }
        }
        return true
    }

    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onSingleTapUp(e: MotionEvent): Boolean {
            if (isOpen) {
                if (isPointInsideView(e.rawX, e.rawY, slidingContainer)) {
                    animateSlidingContainer(false)
                    return true
                }
                if (isPointInsideView(e.rawX, e.rawY, addPlaylistButton)) {
                    listener?.onPlaylistClick()
                    return true
                }
                if (isPointInsideView(e.rawX, e.rawY, editButton)) {
                    listener?.onEditClick()
                    return true
                }
                if (isPointInsideView(e.rawX, e.rawY, deleteButton)) {
                    listener?.onDeleteClick()
                    return true
                }
            } else {
                listener?.onClick()
            }
            return true
        }
    }

    private fun animateSlidingContainer(open: Boolean) {
        val marginInPixels = resources.getDimension(R.dimen.card_view_margin_start).toInt()
        isOpen = open
        val value = (if (isOpen) -slidingContainer.width / 2 else marginInPixels).toFloat()
        slidingContainer
                .animate()
                .x(value)
                .setDuration(DURATION)
                .setListener(object : Animator.AnimatorListener {
                    override fun onAnimationStart(animation: Animator) {
                        listener?.onAnimateChanged(this@AudioFileCardView, isOpen)
                    }

                    override fun onAnimationEnd(animation: Animator) {

                    }

                    override fun onAnimationCancel(animation: Animator) {

                    }

                    override fun onAnimationRepeat(animation: Animator) {

                    }
                })
    }

    fun setOnFileCardListener(l: OnFileCardListener) {
        this.listener = l
    }

    fun isOpen(): Boolean {
        return isOpen
    }

    fun close() {
        animateSlidingContainer(false)
    }
}