package com.fesskiev.mediacenter.ui.media.video.details

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.adapters.VideoFilesAdapter
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_VIDEO_FOLDER
import com.fesskiev.mediacenter.utils.invisible
import com.fesskiev.mediacenter.utils.visible
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_video_files.*
import javax.inject.Inject

class VideoFilesActivity : DaggerAppCompatActivity(), VideoFilesContract.View {

    @Inject
    @JvmField
    var presenter: VideoFilesPresenter? = null
    private lateinit var videoFolder: VideoFolder
    private lateinit var adapter: VideoFilesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_files)
        setupToolbar()
        setupRecyclerView()
        setupPlaybackView()
        videoFolder = if (savedInstanceState == null) {
            intent.extras.getParcelable(EXTRA_VIDEO_FOLDER)
        } else {
            savedInstanceState.getParcelable(EXTRA_VIDEO_FOLDER)
        }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        adapter = VideoFilesAdapter()
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }

    private fun setupPlaybackView() {

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        outState?.putParcelable(EXTRA_VIDEO_FOLDER, videoFolder)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        presenter?.fetchVideoFiles(videoFolder)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }

    private fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun showVideoFiles(videoFiles: List<VideoFile>) {
        adapter.refresh(videoFiles)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun showProgressBar() {
        progressBar.visible()
    }

    override fun hideProgressBar() {
        progressBar.invisible()
    }
}