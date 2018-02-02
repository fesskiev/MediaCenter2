package com.fesskiev.mediacenter.ui.media.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import dagger.android.support.DaggerFragment


class FilesFragment : DaggerFragment(), FilesContract.View {

    companion object {
        fun newInstance(): FilesFragment {
            return FilesFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}