package com.fesskiev.mediacenter.widgets

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet
import android.view.View

class ScrollChildSwipeRefreshLayout(context: Context, attrs: AttributeSet) : SwipeRefreshLayout(context, attrs) {

    private var scrollUpChild: View? = null

    override fun canChildScrollUp(): Boolean {
        return if (scrollUpChild != null) {
            scrollUpChild!!.canScrollVertically(-1)
        } else super.canChildScrollUp()
    }

    fun setScrollUpChild(view: View) {
        scrollUpChild = view
    }
}
