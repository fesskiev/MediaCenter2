package com.fesskiev.mediacenter.ui.playlist

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.adapters.PlaylistAdapter
import com.fesskiev.mediacenter.utils.invisible
import com.fesskiev.mediacenter.utils.showToast
import com.fesskiev.mediacenter.utils.visible
import com.fesskiev.mediacenter.widgets.recycler.HidingScrollListener
import dagger.android.support.DaggerAppCompatActivity
import kotlinx.android.synthetic.main.activity_playlist.*
import javax.inject.Inject


class PlaylistActivity : DaggerAppCompatActivity(), PlaylistContract.View, PlaylistAdapter.OnPlaylistAdapterListener {

    @Inject
    @JvmField
    var presenter: PlaylistPresenter? = null
    private lateinit var adapter: PlaylistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_playlist)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(applicationContext,
                LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        adapter = PlaylistAdapter(presenter)
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
        adapter.setOnMediaFilesAdapterListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter?.getPlaylist()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }

    override fun onDeletePlaylistFile(mediaFile: MediaFile, position: Int, lastItem: Boolean) {
        presenter?.deletePlaylistFile(mediaFile, position, lastItem)
    }

    override fun onClickFile(mediaFile: MediaFile) {

    }

    override fun showPlaylistFileDeleted() {
        showToast(R.string.toast_video_file_deleted_playlist)
    }

    override fun removeFileAdapter(position: Int) {
        adapter.remove(position)
    }

    override fun showPlaylist(mediaFiles: List<MediaFile>) {
        adapter.refresh(mediaFiles)
    }

    override fun showEmptyPlaylist() {
        emptyPlaylistText.visible()
    }

    override fun showProgressBar() {
        progressBar.visible()
    }

    override fun hideProgressBar() {
        progressBar.invisible()
    }
}