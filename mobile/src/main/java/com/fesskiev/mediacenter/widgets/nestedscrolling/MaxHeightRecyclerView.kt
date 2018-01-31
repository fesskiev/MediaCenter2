package com.fesskiev.mediacenter.widgets.nestedscrolling

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View


class MaxHeightRecyclerView(context: Context?, attrs: AttributeSet?) : RecyclerView(context, attrs) {

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