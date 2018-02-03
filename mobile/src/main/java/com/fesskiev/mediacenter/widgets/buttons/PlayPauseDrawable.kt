package com.fesskiev.mediacenter.widgets.buttons

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.Property
import com.fesskiev.mediacenter.R

class PlayPauseDrawable : Drawable {

    private var PROGRESS = object : Property<PlayPauseDrawable,
            Float>(Float::class.java, "progress") {
        override fun get(d: PlayPauseDrawable): Float {
            return d.getProgress()
        }

        override fun set(d: PlayPauseDrawable, value: Float) {
            d.setProgress(value)
        }
    }

    private val leftPauseBar = Path()
    private val rightPauseBar = Path()
    private val paint = Paint()
    private val bounds = RectF()
    private var pauseBarWidth: Float = 0.toFloat()
    private var pauseBarHeight: Float = 0.toFloat()
    private var pauseBarDistance: Float = 0.toFloat()

    private var width: Float = 0.toFloat()
    private var height: Float = 0.toFloat()

    private var progress: Float = 0.toFloat()
    private var isPlay: Boolean = false

    constructor(context: Context) : super() {
        val res = context.resources
        paint.isAntiAlias = true
        paint.style = Paint.Style.FILL
        paint.color = Color.WHITE
        pauseBarWidth = res.getDimensionPixelSize(R.dimen.pause_bar_width).toFloat()
        pauseBarHeight = res.getDimensionPixelSize(R.dimen.pause_bar_height).toFloat()
        pauseBarDistance = res.getDimensionPixelSize(R.dimen.pause_bar_distance).toFloat()
    }

    override fun onBoundsChange(bounds: Rect) {
        super.onBoundsChange(bounds)
        this.bounds.set(bounds)
        width = this.bounds.width()
        height = this.bounds.height()
    }

    override fun draw(canvas: Canvas) {
        leftPauseBar.rewind()
        rightPauseBar.rewind()
        // The current distance between the two pause bars.
        val barDist = lerp(pauseBarDistance, 0f, progress)
        // The current width of each pause bar.
        val barWidth = lerp(pauseBarWidth, pauseBarHeight / 2f, progress)
        // The current position of the left pause bar's top left coordinate.
        val firstBarTopLeft = lerp(0f, barWidth, progress)
        // The current position of the right pause bar's top right coordinate.
        val secondBarTopRight = lerp(2 * barWidth + barDist, barWidth + barDist, progress)

        // Draw the left pause bar. The left pause bar transforms into the
        // top half of the play button triangle by animating the position of the
        // rectangle's top left coordinate and expanding its bottom width.
        leftPauseBar.moveTo(0f, 0f)
        leftPauseBar.lineTo(firstBarTopLeft, -pauseBarHeight)
        leftPauseBar.lineTo(barWidth, -pauseBarHeight)
        leftPauseBar.lineTo(barWidth, 0f)
        leftPauseBar.close()

        // Draw the right pause bar. The right pause bar transforms into the
        // bottom half of the play button triangle by animating the position of the
        // rectangle's top right coordinate and expanding its bottom width.
        rightPauseBar.moveTo(barWidth + barDist, 0f)
        rightPauseBar.lineTo(barWidth + barDist, -pauseBarHeight)
        rightPauseBar.lineTo(secondBarTopRight, -pauseBarHeight)
        rightPauseBar.lineTo(2 * barWidth + barDist, 0f)
        rightPauseBar.close()

        canvas.save()

        // Translate the play button a tiny bit to the right so it looks more centered.
        canvas.translate(lerp(0f, pauseBarHeight / 8f, progress), 0f)

        Log.w("test", " isPlay: " + isPlay)

        // (1) Pause --> Play: rotate 0 to 90 degrees clockwise.
        // (2) Play --> Pause: rotate 90 to 180 degrees clockwise.
        val rotationProgress = if (isPlay) 1 - progress else progress
        val startingRotation = (if (isPlay) 90 else 0).toFloat()
        canvas.rotate(lerp(startingRotation, startingRotation + 90, rotationProgress), width / 2f, height / 2f)

        // Position the pause/play button in the center of the drawable's bounds.
        canvas.translate(width / 2f - (2 * barWidth + barDist) / 2f, height / 2f + pauseBarHeight / 2f)

        // Draw the two bars that form the animated pause/play button.
        canvas.drawPath(leftPauseBar, paint)
        canvas.drawPath(rightPauseBar, paint)

        canvas.restore()
    }

    fun getPausePlayAnimator(play: Boolean): Animator {
        isPlay = play
        return ObjectAnimator.ofFloat(this, PROGRESS,
                if (isPlay) 1f else 0f, if (isPlay) 0f else 1f)
    }

    fun isPlay(): Boolean {
        return isPlay
    }

    private fun setProgress(progress: Float) {
        this.progress = progress
        invalidateSelf()
    }

    private fun getProgress(): Float {
        return progress
    }

    override fun setAlpha(alpha: Int) {
        paint.alpha = alpha
        invalidateSelf()
    }

    override fun setColorFilter(cf: ColorFilter?) {
        paint.colorFilter = cf
        invalidateSelf()
    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    /**
     * Linear interpolate between a and b with parameter t.
     */
    private fun lerp(a: Float, b: Float, t: Float): Float {
        return a + (b - a) * t
    }

    fun setPauseBarWidth(pauseBarWidth: Float) {
        this.pauseBarWidth = pauseBarWidth
    }

    fun setPauseBarHeight(pauseBarHeight: Float) {
        this.pauseBarHeight = pauseBarHeight
    }

    fun setPauseBarDistance(pauseBarDistance: Float) {
        this.pauseBarDistance = pauseBarDistance
    }

    fun setColor(color: Int) {
        paint.color = color
    }
}