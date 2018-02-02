package com.fesskiev.mediacenter.ui.media.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import dagger.android.support.DaggerFragment
import javax.inject.Inject

class VideoFragment : DaggerFragment(), VideoContract.View {

    companion object {
        fun newInstance(): VideoFragment {
            return VideoFragment()
        }
    }

    @Inject
    @JvmField
    var presenter: VideoPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        super.onResume()
        presenter?.fetchVideoFolders()
    }

    override fun showVideoFolders(videoFolders: List<VideoFolder>) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}