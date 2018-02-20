package com.fesskiev.mediacenter.widgets.nestedscrolling

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R

class BottomSheetBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<NestedScrollView>(context, attrs) {

    override fun onLayoutChild(parent: CoordinatorLayout, child: NestedScrollView, layoutDirection: Int): Boolean {
        parent.onLayoutChild(child, layoutDirection)

        val fabHalfHeight = child.findViewById<View>(R.id.fab).height / 2
        setTopMargin(child.findViewById(R.id.cardview), fabHalfHeight)

        val maxHeight = (child.height - fabHalfHeight
                - child.findViewById<View>(R.id.cardTitle).height
                - child.findViewById<View>(R.id.cardSubtitle).height)

        val frameLayout = child.findViewById<MaxHeightView>(R.id.contentContainer)
        frameLayout.setMaxHeight(maxHeight + fabHalfHeight)

        val cardContainer = child.findViewById<View>(R.id.cardContainer)
        setPaddingTop(cardContainer, maxHeight)
        return true
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: NestedScrollView, ev: MotionEvent): Boolean {
        if (ev.actionMasked == MotionEvent.ACTION_DOWN) {
            if (parent.isPointInChildBounds(child.findViewById(R.id.contentContainer), ev.x.toInt(), ev.y.toInt()) ||
                    parent.isPointInChildBounds(child.findViewById(R.id.cardview), ev.x.toInt(), ev.y.toInt()) ||
                    parent.isPointInChildBounds(child.findViewById(R.id.fab), ev.x.toInt(), ev.y.toInt())) {
                val nestedScrollView: CustomNestedScrollView2 = child as CustomNestedScrollView2
                nestedScrollView.enableScrolling = true
            } else {
                val nestedScrollView: CustomNestedScrollView2 = child as CustomNestedScrollView2
                nestedScrollView.enableScrolling = false
            }
        }
        return false
    }

    private fun setTopMargin(v: View, topMargin: Int) {
        val lp = v.layoutParams as ViewGroup.MarginLayoutParams
        if (lp.topMargin != topMargin) {
            lp.topMargin = topMargin
            v.layoutParams = lp
        }
    }

    private fun setPaddingTop(v: View, top: Int) {
        if (v.paddingTop != top) {
            v.setPadding(v.paddingLeft, top, v.paddingRight, v.paddingBottom)
        }
    }

    private fun setPaddingBottom(v: View, bottom: Int) {
        if (v.paddingBottom != bottom) {
            v.setPadding(v.paddingLeft, v.paddingTop, v.paddingRight, bottom)
        }
    }

}