package com.fesskiev.mediacenter.ui.media.audio.details

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.adapters.AudioFilesAdapter
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_AUDIO_FOLDER
import com.fesskiev.mediacenter.widgets.recycler.HidingScrollListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_audio_files.*
import kotlinx.android.synthetic.main.layout_playback.*
import javax.inject.Inject
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.AppBarLayout
import com.fesskiev.mediacenter.utils.*


class AudioFilesActivity : DaggerAppCompatActivity(), AudioFilesContract.View,
        AudioFilesAdapter.OnAudioFilesAdapterListener {

    @Inject
    @JvmField
    var presenter: AudioFilesPresenter? = null
    private lateinit var audioFolder: AudioFolder
    private lateinit var adapter: AudioFilesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_files)
        setupToolbar(toolbar)
        setupRecyclerView()
        setupPlaybackView()
        audioFolder = if (savedInstanceState == null) {
            intent.extras.getParcelable(EXTRA_AUDIO_FOLDER)
        } else {
            savedInstanceState.getParcelable(EXTRA_AUDIO_FOLDER)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(EXTRA_AUDIO_FOLDER, audioFolder)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        presenter?.fetchBackdropBitmap(audioFolder)
        presenter?.fetchAudioFiles(audioFolder)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }

    override fun showAudioFiles(audioFiles: List<AudioFile>) {
        adapter.refresh(audioFiles)
    }

    override fun onDeleteFile(mediaFile: MediaFile) {

    }

    override fun onEditFile(mediaFile: MediaFile) {

    }

    override fun onPlayListFile(mediaFile: MediaFile) {

    }

    override fun onClickFile(mediaFile: MediaFile) {

    }

    override fun audioFileNotExist() {
        showToast(R.string.toast_audio_file_not_exists)
    }

    override fun showBackdropBitmap(bitmap: Bitmap) {
        backdrop.setImageBitmap(bitmap)
        collapseToolbar()
    }

    override fun showPaletteColors(paletteColors: BitmapUtils.PaletteColors) {
        setupPalette(paletteColors)
    }

    override fun showProgressBar() {
        progressBar.visible()
    }

    override fun hideProgressBar() {
        progressBar.invisible()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        adapter = AudioFilesAdapter()
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
        recyclerView.addOnScrollListener(object : HidingScrollListener() {
            override fun onHide() {

            }

            override fun onShow() {

            }

            override fun onItemPosition(position: Int) {
                adapter.hideOpenCards()
            }

            override fun onPaging(lastPosition: Int) {

            }
        })
        adapter.setOnAudioFilesAdapterListener(this)
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

    private fun collapseToolbar() {
        val params = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior?
        behavior?.onNestedPreScroll(coordinatorLayout, appBarLayout,
                appBarLayout, 0, backdrop.height / 2, intArrayOf(0, 0), 0)
    }

    private fun animateTopView(value: Float) {
        fabPlayPause.animate().alpha(value)
        cardTitle.animate().alpha(value)
        cardSubtitle.animate().alpha(value)
    }

    private fun setupPalette(paletteColors: BitmapUtils.PaletteColors) {
        toolbarLayout.setContentScrimColor(paletteColors.muted)
        toolbarLayout.setStatusBarScrimColor(paletteColors.vibrantDark)
        toolbarLayout.setBackgroundColor(paletteColors.vibrantDark)
    }
}