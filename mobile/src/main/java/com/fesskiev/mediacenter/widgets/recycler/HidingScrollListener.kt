package com.fesskiev.mediacenter.widgets.recycler

import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView

abstract class HidingScrollListener : RecyclerView.OnScrollListener() {

    companion object {
        private const val HIDE_THRESHOLD = 150
    }

    private var scrolledDistance = 0
    private var controlsVisible = true

    abstract fun onHide()

    abstract fun onShow()

    abstract fun onItemPosition(position: Int)

    abstract fun onLoading(lastItem: Int)

    override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
            val linearLayoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
            val completelyPosition = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
            onItemPosition(completelyPosition)
        }
    }

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        val linearLayoutManager: LinearLayoutManager = recyclerView.layoutManager as LinearLayoutManager
        val firstVisibleItem = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
        if(!recyclerView.canScrollVertically(1)){
            onLoading(linearLayoutManager.findLastCompletelyVisibleItemPosition())
        }
        if (firstVisibleItem == 0) {
            if (!controlsVisible) {
                onShow()
                controlsVisible = true
            }
        } else {
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide()
                controlsVisible = false
                scrolledDistance = 0
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow()
                controlsVisible = true
                scrolledDistance = 0
            }
        }
        if (controlsVisible && dy > 0 || !controlsVisible && dy < 0) {
            scrolledDistance += dy
        }
    }
}