package com.fesskiev.mediacenter.widgets.nestedscrolling

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout

class MaxHeightView(context: Context?, attrs: AttributeSet?) : FrameLayout(context, attrs) {

    private var maxHeight = -1

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        val mode = View.MeasureSpec.getMode(heightSpec)
        val height = View.MeasureSpec.getSize(heightSpec)

        var newHeight = heightSpec
        if (maxHeight >= 0 && (mode == View.MeasureSpec.UNSPECIFIED || height > maxHeight)) {
            newHeight = View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.AT_MOST)
        }
        super.onMeasure(widthSpec, newHeight)
    }

    fun setMaxHeight(maxHeight: Int) {
        if (this.maxHeight != maxHeight) {
            this.maxHeight = maxHeight
            requestLayout()
        }
    }
}