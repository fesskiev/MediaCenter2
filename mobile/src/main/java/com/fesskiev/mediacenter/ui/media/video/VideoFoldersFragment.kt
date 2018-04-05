package com.fesskiev.mediacenter.ui.media.video

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.adapters.VideoFoldersAdapter
import com.fesskiev.mediacenter.ui.media.video.details.VideoFilesActivity
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_VIDEO_FOLDER
import com.fesskiev.mediacenter.utils.invisible
import com.fesskiev.mediacenter.utils.showToast
import com.fesskiev.mediacenter.utils.visible
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_audio.*
import javax.inject.Inject

class VideoFoldersFragment : DaggerFragment(), VideoFoldersContract.View, VideoFoldersAdapter.OnVideoFolderAdapterListener {

    companion object {
        fun newInstance(): VideoFoldersFragment {
            return VideoFoldersFragment()
        }
    }

    @Inject
    @JvmField
    var foldersPresenter: VideoFoldersPresenter? = null

    private lateinit var adapter: VideoFoldersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val columns = resources.getInteger(R.integer.folder_columns)
        val gridLayoutManager = GridLayoutManager(activity, columns)
        recyclerView.layoutManager = gridLayoutManager
        adapter = VideoFoldersAdapter(foldersPresenter)
        adapter.setHasStableIds(true)
        adapter.setOnVideoFolderAdapterListener(this)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        foldersPresenter?.fetchVideoFolders()
    }

    override fun showProgressBar() {
        progressBar.visible()
    }

    override fun hideProgressBar() {
        progressBar.invisible()
    }


    override fun showVideoFolders(videoFolders: List<VideoFolder>) {
        adapter.refresh(videoFolders)
    }

    override fun showVideoFolderNotExist() {
        showToast(R.string.toast_video_folder_not_exists)
    }

    override fun onVideoFolderClick(videoFolder: VideoFolder) {
        val exist = foldersPresenter?.checkVideoFolderExist(videoFolder) ?: false
        if (exist) {
            val i = Intent(context, VideoFilesActivity::class.java)
            i.putExtra(EXTRA_VIDEO_FOLDER, videoFolder)
            startActivity(i)
        }
    }

    override fun onPopupMenuClick(view: View, videoFolder: VideoFolder) {

    }

    override fun onDestroy() {
        super.onDestroy()
        foldersPresenter?.detach()
    }
}