package com.fesskiev.mediacenter.widgets.controls

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import com.fesskiev.mediacenter.R
import android.graphics.Bitmap
import android.view.MotionEvent

class AudioControlView : View {

    interface OnAudioVolumeSeekListener {

        fun changeVolumeStart(volume: Int)

        fun changeVolumeFinish()

        fun changeSeekStart(seek: Int)

        fun changeSeekFinish()
    }

    private var listener: OnAudioVolumeSeekListener? = null

    private lateinit var seekSlider: Slider
    private lateinit var volumeSlider: Slider
    private lateinit var progressPaint: Paint
    private lateinit var circleFillPaint: Paint
    private lateinit var circlePaint: Paint
    private lateinit var volumeRect: RectF
    private lateinit var seekRect: RectF

    private var radiusVolume: Int = 0
    private var radiusSeek: Int = 0
    private var lineRadius: Int = 0
    private var padding: Int = 0

    private var enableChangeVolume: Boolean = false

    private var cx: Float = 0.toFloat()
    private var cy: Float = 0.toFloat()

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs, defStyleAttr)
    }

    private fun init(attrs: AttributeSet?, defStyle: Int) {
        val resources = resources
        val context = context
        val a = context.obtainStyledAttributes(attrs, R.styleable.AudioControlView, defStyle, 0)
        val circleColor = a.getColor(
                R.styleable.AudioControlView_circleColor,
                ContextCompat.getColor(context, R.color.audio_control))
        val progressColor = a.getColor(
                R.styleable.AudioControlView_progressColor,
                ContextCompat.getColor(context, R.color.audio_control))
        a.recycle()

        seekSlider = Slider(resources.getDimensionPixelSize(R.dimen.seek_slider).toFloat(),
                R.drawable.ic_seek)
        volumeSlider = Slider(resources.getDimensionPixelSize(R.dimen.volume_slider).toFloat(),
                R.drawable.ic_volume)

        radiusVolume = resources.getDimensionPixelSize(R.dimen.volume_radius)
        radiusSeek = resources.getDimensionPixelSize(R.dimen.seek_radius)

        lineRadius = resources.getDimensionPixelSize(R.dimen.line_radius)
        val circleStrokeWidth = resources.getDimensionPixelSize(R.dimen.circle_stroke_width).toFloat()

        padding = resources.getDimensionPixelSize(R.dimen.audio_control_padding)

        circlePaint = Paint()
        circlePaint.style = Paint.Style.STROKE
        circlePaint.color = circleColor
        circlePaint.isAntiAlias = true
        circlePaint.strokeWidth = circleStrokeWidth

        circleFillPaint = Paint()
        circleFillPaint.style = Paint.Style.FILL
        circleFillPaint.color = progressColor
        circleFillPaint.isAntiAlias = true
        circleFillPaint.strokeWidth = circleStrokeWidth

        progressPaint = Paint()
        progressPaint.style = Paint.Style.STROKE
        progressPaint.strokeCap = Paint.Cap.ROUND
        progressPaint.color = progressColor
        progressPaint.isAntiAlias = true
        progressPaint.strokeWidth = circleStrokeWidth

        setBackgroundColor(Color.TRANSPARENT)

        enableChangeVolume = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        volumeRect = RectF((w / 2 - radiusVolume).toFloat(), (h / 2 - radiusVolume).toFloat(),
                (w / 2 + radiusVolume).toFloat(), (h / 2 + radiusVolume).toFloat())

        seekRect = RectF((w / 2 - radiusSeek).toFloat(), (h / 2 - radiusSeek).toFloat(),
                (w / 2 + radiusSeek).toFloat(), (h / 2 + radiusSeek).toFloat())

        cx = width / 2f
        cy = height / 2f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(cx, cy, radiusVolume.toFloat(), circlePaint)
        canvas.drawArc(volumeRect, 274f, volumeSlider.progress, false, progressPaint)

        volumeSlider.x = (cx + radiusVolume * Math.sin(Math.toRadians(volumeSlider.progress.toDouble()))).toFloat()
        volumeSlider.y = (cy - radiusVolume * Math.cos(Math.toRadians(volumeSlider.progress.toDouble()))).toFloat()

        canvas.drawCircle(volumeSlider.x, volumeSlider.y, volumeSlider.radius, circleFillPaint)
        canvas.drawBitmap(volumeSlider.bitmap, volumeSlider.x - volumeSlider.radius / 2 - padding,
                volumeSlider.y - volumeSlider.radius / 2 - padding, null)

        canvas.drawCircle(cx, cy, radiusSeek.toFloat(), circlePaint)
        canvas.drawArc(seekRect, 273f, seekSlider.progress, false, progressPaint)

        seekSlider.x = (cx + radiusSeek * Math.sin(Math.toRadians(seekSlider.progress.toDouble()))).toFloat()
        seekSlider.y = (cy - radiusSeek * Math.cos(Math.toRadians(seekSlider.progress.toDouble()))).toFloat()


        canvas.drawCircle(seekSlider.x, seekSlider.y, seekSlider.radius, circleFillPaint)
        canvas.drawBitmap(seekSlider.bitmap, seekSlider.x - seekSlider.radius / 2 - padding,
                seekSlider.y - seekSlider.radius / 2 - padding, null)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val action = event.actionMasked
        val x = event.x
        val y = event.y
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                if (inCircle(x, y, seekSlider.x, seekSlider.y, seekSlider.radius)) {
                    seekSlider.check = true
                }
                if (enableChangeVolume) {
                    if (inCircle(x, y, volumeSlider.x, volumeSlider.y, volumeSlider.radius)) {
                        volumeSlider.check = true
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (seekSlider.check) {
                    setSeekProgress(x, y)
                }
                if (volumeSlider.check) {
                    setVolumeProgress(x, y)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                if (volumeSlider.check) {
                    listener?.changeVolumeFinish()
                }
                if (seekSlider.check) {
                    listener?.changeSeekFinish()
                }
                volumeSlider.check = false
                seekSlider.check = false
            }
        }
        postInvalidate()
        return true
    }

    private fun setSeekProgress(dx: Float, dy: Float) {
        seekSlider.progress = getAngle(dx, dy)
        val scaleValue = seekSlider.progress * (100f / 360)
        listener?.changeSeekStart(scaleValue.toInt())
    }

    private fun setVolumeProgress(dx: Float, dy: Float) {
        volumeSlider.progress = getAngle(dx, dy)
        val scaleValue = volumeSlider.progress * (100f / 360)
        listener?.changeVolumeStart(scaleValue.toInt())
    }

    private fun inCircle(x: Float, y: Float, centerX: Float, centerY: Float, radius: Float): Boolean {
        return Math.sqrt(Math.pow((x - centerX).toDouble(), 2.0) + Math.pow((y - centerY).toDouble(), 2.0)).toFloat() < radius

    }

    private fun angleBetween2Lines(centerX: Float, centerY: Float, x1: Float,
                                   y1: Float, x2: Float, y2: Float): Double {
        val angle1 = Math.atan2((y1 - centerY).toDouble(), (x1 - centerX).toDouble())
        val angle2 = Math.atan2((y2 - centerY).toDouble(), (x2 - centerX).toDouble())
        return angle1 - angle2
    }

    private fun getAngle(x: Float, y: Float): Float {
        var angle = Math.toDegrees(angleBetween2Lines(cx, cy, 0f, 0f, x, y)).toFloat() * -1
        angle -= 45f
        if (angle < 0) {
            angle += 360f
        }
        return angle
    }

    fun setOnAudioVolumeSeekListener(l: OnAudioVolumeSeekListener) {
        this.listener = l
    }

    private inner class Slider(internal var radius: Float, res: Int) {

        internal var bitmap: Bitmap = getBitmap(res)
        internal var x: Float = 0.toFloat()
        internal var y: Float = 0.toFloat()
        internal var progress: Float = 0.toFloat()
        internal var check: Boolean = false

        private fun getBitmap(res: Int): Bitmap {
            val drawable = ContextCompat.getDrawable(context, res)
            val canvas = Canvas()
            val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
            canvas.setBitmap(bitmap)
            drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
            drawable.draw(canvas)
            return bitmap
        }
    }
}