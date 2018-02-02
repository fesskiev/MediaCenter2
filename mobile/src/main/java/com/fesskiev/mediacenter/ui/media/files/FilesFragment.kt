package com.fesskiev.mediacenter.ui.media.files

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import dagger.android.support.DaggerFragment
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_files, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter?.fetchMediaFiles()
    }

    override fun showMediaFiles(mediaFiles: List<MediaFile>) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}