package com.fesskiev.mediacenter.ui.media.files

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.LinearLayoutManager.VERTICAL
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.adapters.MediaFilesAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_audio.*
import javax.inject.Inject


class FilesFragment : DaggerFragment(), FilesContract.View {

    companion object {
        fun newInstance(): FilesFragment {
            return FilesFragment()
        }
    }

    @Inject
    @JvmField
    var presenter: FilesPresenter? = null
    private lateinit var adapter: MediaFilesAdapter

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
    }

    override fun onResume() {
        super.onResume()
        presenter?.fetchMediaFiles()
    }

    override fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun showMediaFiles(mediaFiles: List<MediaFile>) {
        adapter.refresh(mediaFiles)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}