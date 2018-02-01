package com.fesskiev.mediacenter.widgets.nestedscrolling

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View


class CustomNestedScrollView2(context: Context, attrs: AttributeSet) : NestedScrollView2(context, attrs) {

    override fun onNestedPreScroll(target: View, dx: Int, dy: Int, consumed: IntArray, type: Int) {
        val rv = target as RecyclerView
        if (dy < 0 && isRvScrolledToTop(rv) || dy > 0 && !isNsvScrolledToBottom(this)) {
            // The NestedScrollView should steal the scroll event away from the
            // RecyclerView if: (1) the user is scrolling their finger down and the
            // RecyclerView is scrolled to the top of its content, or (2) the user
            // is scrolling their finger up and the NestedScrollView is not scrolled
            // to the bottom of its content.
            scrollBy(0, dy)
            consumed[1] = dy
            return
        }
        super.onNestedPreScroll(target, dx, dy, consumed, type)
    }

    /**
     * Returns true if the [NestedScrollView] is scrolled to the bottom
     * of its content (i.e. the card is completely expanded).
     */
    private fun isNsvScrolledToBottom(nsv: NestedScrollView): Boolean {
        return !nsv.canScrollVertically(1)
    }

    /**
     * Returns true if the [RecyclerView] is scrolled to the
     * top of its content (i.e. its first item is completely visible).
     */
    private fun isRvScrolledToTop(rv: RecyclerView): Boolean {
        val lm = rv.layoutManager as LinearLayoutManager
        return lm.findFirstVisibleItemPosition() == 0 && lm.findViewByPosition(0).top == 0
    }

}