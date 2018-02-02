package com.fesskiev.mediacenter.ui.media.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import dagger.android.support.DaggerFragment

class VideoFragment : DaggerFragment(), VideoContract.View {

    companion object {
        fun newInstance(): VideoFragment {
            return VideoFragment()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

}