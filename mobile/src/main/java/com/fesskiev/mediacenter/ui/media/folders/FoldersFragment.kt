package com.fesskiev.mediacenter.ui.media.folders

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import dagger.android.support.DaggerFragment


class FoldersFragment : DaggerFragment(), FoldersContract.View {

    companion object {
        fun newInstance(): FoldersFragment {
            return FoldersFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_folders, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {

    }

    override fun showProgressBar() {

    }

    override fun hideProgressBar() {

    }
}