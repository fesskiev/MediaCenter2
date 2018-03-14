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
import com.fesskiev.mediacenter.utils.visible
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_audio.*
import javax.inject.Inject

class VideoFragment : DaggerFragment(), VideoContract.View, VideoFoldersAdapter.OnVideoFolderAdapterListener {

    companion object {
        fun newInstance(): VideoFragment {
            return VideoFragment()
        }
    }

    @Inject
    @JvmField
    var presenter: VideoPresenter? = null

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
        adapter = VideoFoldersAdapter(presenter)
        adapter.setHasStableIds(true)
        adapter.setOnVideoFolderAdapterListener(this)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        presenter?.fetchVideoFolders()
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

    }

    override fun onVideoFolderClick(videoFolder: VideoFolder) {
        val exist = presenter?.checkVideoFolderExist(videoFolder) ?: false
        if (exist) {
            val i = Intent(context, VideoFilesActivity::class.java)
            i.putExtra(EXTRA_VIDEO_FOLDER, videoFolder)
            startActivity(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}