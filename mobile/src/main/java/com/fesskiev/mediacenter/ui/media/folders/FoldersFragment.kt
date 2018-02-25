package com.fesskiev.mediacenter.ui.media.folders

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.ui.adapters.FoldersAdapter
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTERNAL_STORAGE
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_folders.*
import java.io.File
import javax.inject.Inject


class FoldersFragment : DaggerFragment(), FoldersContract.View {

    companion object {
        fun newInstance(): FoldersFragment {
            return FoldersFragment()
        }
    }

    @Inject
    @JvmField
    var presenter: FoldersPresenter? = null
    private lateinit var adapter: FoldersAdapter
    private var selectedPath: File? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_folders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupButtonUp()
        setupRecyclerView()

        changeDirectory(File(EXTERNAL_STORAGE))
    }

    private fun setupButtonUp() {
        buttonUp.setOnClickListener({ processUpClick() })
    }

    private fun processUpClick() {
        val parent = selectedPath?.parentFile
        if (parent != null) {
            changeDirectory(parent)
        }
    }

    private fun changeDirectory(dir: File) {
        val contents = dir.listFiles()
        if (contents != null) {
            selectedPath = dir
            textSelectedPath.text = dir.absolutePath
            adapter.refresh(contents.asList())
        }
    }

    private fun setupRecyclerView() {
        val linearLayoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,
                false)
        recyclerView.layoutManager = linearLayoutManager
        adapter = FoldersAdapter(presenter)
        recyclerView.adapter = adapter
        adapter.setOnFoldersAdapterListener(object : FoldersAdapter.OnFoldersAdapterListener {
            override fun onClickFile(file: File) {
                processFileClick(file)
            }
        })
    }

    private fun processFileClick(file: File) {
        if (file.isDirectory) {
            changeDirectory(file)
        } else {
            selectedPath = file
            textSelectedPath.text = file.absolutePath
        }
    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
}