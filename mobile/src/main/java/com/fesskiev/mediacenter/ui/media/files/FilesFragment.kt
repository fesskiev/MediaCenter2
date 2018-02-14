package com.fesskiev.mediacenter.ui.media.files

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.adapters.MediaFilesAdapter
import com.fesskiev.mediacenter.widgets.recycler.HidingScrollListener
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_audio.*
import javax.inject.Inject


class FilesFragment : DaggerFragment(), FilesContract.View, MediaFilesAdapter.OnMediaFilesAdapterListener {

    companion object {
        fun newInstance(): FilesFragment {
            return FilesFragment()
        }
    }

    @Inject
    @JvmField
    var presenter: FilesPresenter? = null
    private lateinit var adapter: MediaFilesAdapter
    private val limit = 10
    private var offset = 0
    private var search: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity, VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        adapter = MediaFilesAdapter(this)
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
                if (!search) {
                    offset = lastPosition
                    fetchMediaFiles()
                }
            }
        })
        adapter.setOnMediaFilesAdapterListener(this)
    }

    override fun onDeleteFile(mediaFile: MediaFile) {

    }

    override fun onEditFile(mediaFile: MediaFile) {

    }

    override fun onPlayListFile(mediaFile: MediaFile) {

    }

    override fun onClickFile(mediaFile: MediaFile) {

    }

    override fun onResume() {
        super.onResume()
        fetchMediaFiles()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun fetchMediaFiles() {
        Log.wtf("test", "fetch limit $limit offset $offset")
        search = false
        presenter?.fetchMediaFiles(limit, offset)
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun showMediaFiles(mediaFiles: List<MediaFile>) {
        adapter.add(mediaFiles)
    }

    override fun showQueryFiles(mediaFiles: List<MediaFile>) {
        search = true
        adapter.refresh(mediaFiles)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}