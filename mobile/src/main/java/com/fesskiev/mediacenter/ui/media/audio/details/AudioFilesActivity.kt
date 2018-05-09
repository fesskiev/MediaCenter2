package com.fesskiev.mediacenter.ui.media.audio.details

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
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
import com.fesskiev.mediacenter.widgets.controls.AudioControlLayout


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

    override fun onDeleteFile(audioFile: AudioFile, position: Int) {
        presenter?.deleteFile(audioFile, position)
    }

    override fun onEditFile(audioFile: AudioFile) {
        presenter?.editFile(audioFile)
    }

    override fun onPlaylistFile(audioFile: AudioFile) {
        presenter?.toPlaylistFile(audioFile)
    }

    override fun onClickFile(audioFile: AudioFile) {
        presenter?.playFile(audioFolder, audioFile)
    }

    override fun showFileDeleted() {
        showToast(R.string.toast_audio_file_deleted)
    }

    override fun removeFileAdapter(position: Int) {
        adapter.remove(position)
    }

    override fun showFileNotDeleted() {
        showToast(R.string.toast_audio_file_not_deleted)
    }

    override fun showFileAddedPlaylist() {
        showToast(R.string.toast_audio_file_added_playlist)
    }

    override fun showEditFileView() {

    }

    override fun fileNotExists() {
        showToast(R.string.toast_audio_file_not_exists)
    }

    override fun showBackdropBitmap(bitmap: Bitmap) {
        backdrop.setImageBitmap(bitmap)
        collapseToolbar()
    }

    override fun showPaletteColors(paletteColors: BitmapUtils.PaletteColors) {
        toolbarLayout.setContentScrimColor(paletteColors.vibrantLight)
        toolbarLayout.setStatusBarScrimColor(paletteColors.vibrantLight)
        toolbarLayout.setBackgroundColor(paletteColors.vibrantLight)
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
        contentContainer.addView(AudioControlLayout(this))
    }

    private fun collapseToolbar() {
        val params = appBarLayout.layoutParams as CoordinatorLayout.LayoutParams
        val behavior = params.behavior as AppBarLayout.Behavior?
        behavior?.onNestedPreScroll(coordinatorLayout, appBarLayout,
                appBarLayout, 0, backdrop.height / 2, intArrayOf(0, 0), 0)
    }

    private fun animateTopView(value: Float) {
        fabPlayPause.animate().alpha(value)
        playbackTitle.animate().alpha(value)
        playbackDesc.animate().alpha(value)
    }
}