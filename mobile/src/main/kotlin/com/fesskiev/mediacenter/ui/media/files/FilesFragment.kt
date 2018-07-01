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
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_SEARCH_FILES
import com.fesskiev.mediacenter.utils.invisible
import com.fesskiev.mediacenter.utils.visible
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
    private var query: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        query = savedInstanceState?.getString(EXTRA_SEARCH_FILES)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(EXTRA_SEARCH_FILES, query)
        super.onSaveInstanceState(outState)
    }

    override fun onResume() {
        super.onResume()
        val query = this.query
        if (query != null) {
            presenter?.queryFiles(query)
        }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity, VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        adapter = MediaFilesAdapter(presenter)
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

    override fun onDeleteFile(mediaFile: MediaFile) {

    }

    override fun onEditFile(mediaFile: MediaFile) {

    }

    override fun onPlayListFile(mediaFile: MediaFile) {

    }

    override fun onClickFile(mediaFile: MediaFile) {

    }

    override fun showProgressBar() {
        progressBar.visible()
    }

    override fun hideProgressBar() {
        progressBar.invisible()
    }


    fun queryFiles(query: String) {
        this.query = query
        presenter?.queryFiles(query)
    }

    override fun showQueryFiles(mediaFiles: List<MediaFile>) {
        adapter.refresh(mediaFiles)
    }

    override fun showEmptyQuery() {
        adapter.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}