package com.fesskiev.mediacenter.ui.media.audio.details

import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.view.View
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_AUDIO_FOLDER
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.layout_playback.*
import javax.inject.Inject


class AudioFilesActivity : DaggerAppCompatActivity(), AudioFilesContract.View {

    @Inject
    @JvmField
    var presenter: AudioFilesPresenter? = null

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
        nestedScrollview.overScrollMode = View.OVER_SCROLL_NEVER
        nestedScrollview.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { v, scrollX, scrollY,
                                                                                             oldScrollX, oldScrollY ->
            val height = (v.getChildAt(0).measuredHeight - v.measuredHeight).toFloat()
            val value = scrollY / height
            animateTopView(1f - value)
        })
    }

    private fun animateTopView(value: Float) {
        fabPlayPause.animate().alpha(value)
        cardTitle.animate().alpha(value)
        cardSubtitle.animate().alpha(value)
    }
}