package com.fesskiev.mediacenter.ui.media.audio.details

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.animation.DecelerateInterpolator
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.ui.adapters.BottomSheetAdapter
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_AUDIO_FOLDER
import com.fesskiev.mediacenter.widgets.nestedscrolling.CustomNestedScrollView2
import dagger.android.support.DaggerAppCompatActivity
import javax.inject.Inject


class AudioFilesActivity : DaggerAppCompatActivity(), AudioFilesContract.View {

    @Inject
    @JvmField
    var presenter: AudioFilesPresenter? = null

    private var isShowingCardHeaderShadow: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_files)
        val audioFolder: AudioFolder = intent.extras.getParcelable(EXTRA_AUDIO_FOLDER)
        setupPlaybackView()
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }

    private fun setupPlaybackView() {
        val recyclerView = findViewById<RecyclerView>(R.id.card_recyclerview)
        val linearLayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.adapter = BottomSheetAdapter()
        recyclerView.addItemDecoration(DividerItemDecoration(this, linearLayoutManager.orientation))

        val cardHeaderShadow = findViewById<View>(R.id.card_header_shadow)
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                // Animate the shadow view in/out as the user scrolls so that it
                // looks like the RecyclerView is scrolling beneath the card header.
                val isRecyclerViewScrolledToTop = linearLayoutManager.findFirstVisibleItemPosition() == 0 &&
                        linearLayoutManager.findViewByPosition(0).top == 0
                if (!isRecyclerViewScrolledToTop && !isShowingCardHeaderShadow) {
                    isShowingCardHeaderShadow = true
                    showOrHideView(cardHeaderShadow, true)
                } else if (isRecyclerViewScrolledToTop && isShowingCardHeaderShadow) {
                    isShowingCardHeaderShadow = false
                    showOrHideView(cardHeaderShadow, false)
                }
            }
        })

        val customNestedScrollView2 = findViewById<CustomNestedScrollView2>(R.id.nestedscrollview)
        customNestedScrollView2.overScrollMode = View.OVER_SCROLL_NEVER
        customNestedScrollView2.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY,
                                                                                                    oldScrollX, oldScrollY ->
            if (scrollY == 0 && oldScrollY > 0) {
                // Reset the RecyclerView's scroll position each time the card
                // returns to its starting position.
                recyclerView.scrollToPosition(0)
                cardHeaderShadow.alpha = 0f
                isShowingCardHeaderShadow = false
            }
        })
    }

    private fun showOrHideView(view: View, shouldShow: Boolean) {
        view.animate().alpha(if (shouldShow) 1f else 0f)
                .setDuration(100).interpolator = DecelerateInterpolator()
    }
}