package com.fesskiev.mediacenter.widgets.buttons

import android.animation.AnimatorSet
import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.support.v4.content.ContextCompat
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import android.view.animation.DecelerateInterpolator
import com.fesskiev.mediacenter.R


class PlayPauseButton(c: Context?, attrs: AttributeSet?) : AppCompatImageView(c, attrs) {

    interface OnClickListener {
        fun onPlay(play: Boolean)
    }

    companion object {
        private const val PLAY_PAUSE_ANIMATION_DURATION: Long = 200
    }

    private var listener: OnClickListener? = null
    private var drawable: PlayPauseDrawable
    private var animatorSet: AnimatorSet? = null
    private var timerDrawable: Drawable? = null
    private var showTimer: Boolean = false

    init {
        val res = context.resources
        timerDrawable = ContextCompat.getDrawable(context, R.drawable.avd_clock_timer)
        drawable = PlayPauseDrawable(context)
        drawable.callback = this
        drawable.setColor(ContextCompat.getColor(context, R.color.audio_control))
        drawable.setPauseBarWidth(res.getDimensionPixelSize(R.dimen.pause_bar_big_width).toFloat())
        drawable.setPauseBarHeight(res.getDimensionPixelSize(R.dimen.pause_bar_big_height).toFloat())
        drawable.setPauseBarDistance(res.getDimensionPixelSize(R.dimen.pause_bar_big_distance).toFloat())
        setOnClickListener { togglePlay() }
        setPlay(false)
    }

    private fun togglePlay() {
        val play = !drawable.isPlay()
        listener?.onPlay(play)
        setPlay(play)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        drawable.setBounds(0, 0, w, h)
    }

    override fun verifyDrawable(who: Drawable): Boolean {
        return who === drawable || super.verifyDrawable(who)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (!showTimer) {
            drawable.draw(canvas)
        }
    }

    fun setPlay(play: Boolean) {
        animatorSet?.cancel()
        animatorSet = AnimatorSet()
        val pausePlayAnim = drawable.getPausePlayAnimator(play)
        animatorSet?.interpolator = DecelerateInterpolator()
        animatorSet?.duration = PLAY_PAUSE_ANIMATION_DURATION
        animatorSet?.playTogether(pausePlayAnim)
        animatorSet?.start()
    }

    fun startLoading() {
        showTimer = true
        setImageDrawable(timerDrawable)
        (getDrawable() as AnimatedVectorDrawable).start()

    }

    fun finishLoading() {
        showTimer = false
        val drawable = getDrawable() as AnimatedVectorDrawable
        drawable.stop()
        setImageDrawable(null)
    }

    fun setOnClickListener(listener: OnClickListener) {
        this.listener = listener
    }
}