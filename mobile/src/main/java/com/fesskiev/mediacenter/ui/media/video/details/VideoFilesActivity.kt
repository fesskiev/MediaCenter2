package com.fesskiev.mediacenter.ui.media.video.details

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.adapters.VideoFilesAdapter
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_VIDEO_FOLDER
import com.fesskiev.mediacenter.utils.invisible
import com.fesskiev.mediacenter.utils.showToast
import com.fesskiev.mediacenter.utils.visible
import com.fesskiev.mediacenter.widgets.recycler.HidingScrollListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_video_files.*
import javax.inject.Inject


class VideoFilesActivity : DaggerAppCompatActivity(), VideoFilesContract.View, VideoFilesAdapter.OnVideoFilesAdapterListener {

    @Inject
    @JvmField
    var presenter: VideoFilesPresenter? = null
    private lateinit var videoFolder: VideoFolder
    private lateinit var adapter: VideoFilesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_files)
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
        adapter = VideoFilesAdapter(presenter)
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
        adapter.setOnVideoFilesAdapterListener(this)
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

    override fun onDeleteFile(videoFile: VideoFile, position: Int) {
        presenter?.deleteFile(videoFile, position)
    }

    override fun onEditFile(videoFile: VideoFile) {
        presenter?.editFile(videoFile)
    }

    override fun onPlaylistFile(videoFile: VideoFile) {
        presenter?.toPlaylistFile(videoFile)
    }

    override fun onClickFile(videoFile: VideoFile) {
        presenter?.playFile(videoFile)
    }

    override fun fileNotExists() {
        showToast(R.string.toast_video_file_not_exists)
    }

    override fun showFileDeleted() {
        showToast(R.string.toast_video_file_deleted)
    }

    override fun showFileNotDeleted() {
        showToast(R.string.toast_video_file_not_deleted)
    }

    override fun showFileAddedPlaylist() {
        showToast(R.string.toast_video_file_added_playlist)
    }

    override fun showEditFileView() {

    }

    override fun removeFileAdapter(position: Int) {
        adapter.remove(position)
    }

    override fun showVideoFiles(videoFiles: List<VideoFile>) {
        adapter.refresh(videoFiles)
    }

    override fun showProgressBar() {
        progressBar.visible()
    }

    override fun hideProgressBar() {
        progressBar.invisible()
    }
}