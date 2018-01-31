package com.fesskiev.mediacenter.widgets.nestedscrolling

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.support.v4.view.ViewCompat
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R


class CustomBehavior(context: Context?, attrs: AttributeSet?) : CoordinatorLayout.Behavior<NestedScrollView>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: NestedScrollView,
                                 dependency: View): Boolean {
        // List the toolbar container as a dependency to ensure that it will
        // always be laid out before the child (which depends on the toolbar
        // container's height in onLayoutChild() below).
        return dependency.id == R.id.toolbarContainer
    }

    override fun onLayoutChild(parent: CoordinatorLayout,
                               child: NestedScrollView, layoutDirection: Int): Boolean {
        // First layout the child as normal.
        parent.onLayoutChild(child, layoutDirection)

        // Center the FAB vertically along the top edge of the card.
        val fabHalfHeight = child.findViewById<View>(R.id.fab).height / 2
        setTopMargin(child.findViewById(R.id.cardview), fabHalfHeight)

        // Give the RecyclerView a maximum height to ensure the card will never
        // overlap the toolbar as it scrolls.
        val rvMaxHeight = (child.height - fabHalfHeight
                - child.findViewById<View>(R.id.card_title).height
                - child.findViewById<View>(R.id.card_subtitle).height)
        val rv = child.findViewById<MaxHeightRecyclerView>(R.id.card_recyclerview)
        rv.setMaxHeight(rvMaxHeight)

        // Give the card container top padding so that only the top edge of the card
        // initially appears at the bottom of the screen. The total padding will
        // be the distance from the top of the screen to the FAB's top edge.
        val cardContainer = child.findViewById<View>(R.id.card_container)
        val toolbarContainerHeight = 128
        setPaddingTop(cardContainer, rvMaxHeight - toolbarContainerHeight)

        // Offset the child's height so that its bounds don't overlap the
        // toolbar container.
        ViewCompat.offsetTopAndBottom(child, toolbarContainerHeight)

        // Add the same amount of bottom padding to the RecyclerView so it doesn't
        // display its content underneath the navigation bar.
        setPaddingBottom(rv, toolbarContainerHeight)

        // Return true so that the parent doesn't waste time laying out the
        // child again (any modifications made above will have triggered a second
        // layout pass anyway).
        return true
    }

    override fun onInterceptTouchEvent(parent: CoordinatorLayout, child: NestedScrollView,
                                       ev: MotionEvent): Boolean {
        // Block all touch events that originate within the bounds of our
        // NestedScrollView but do *not* originate within the bounds of its
        // inner CardView and FloatingActionButton.
        return (ev.actionMasked == MotionEvent.ACTION_DOWN
                && parent.isPointInChildBounds(child, ev.x.toInt(), ev.y.toInt())
                && !parent.isPointInChildBounds(parent.findViewById(R.id.mainContent), ev.x.toInt(), ev.y.toInt())
                && !parent.isPointInChildBounds(child.findViewById(R.id.cardview), ev.x.toInt(), ev.y.toInt())
                && !parent.isPointInChildBounds(child.findViewById(R.id.fab), ev.x.toInt(), ev.y.toInt()

        ))
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