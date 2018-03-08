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
        setupToolbar()
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

    override fun showBackdropBitmap(bitmap: Bitmap) {
        backdrop.setImageBitmap(bitmap)
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
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

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
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