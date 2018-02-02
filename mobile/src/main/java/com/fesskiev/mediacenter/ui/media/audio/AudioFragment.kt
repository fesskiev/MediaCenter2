package com.fesskiev.mediacenter.ui.media.audio

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import dagger.android.support.DaggerFragment
import javax.inject.Inject


class AudioFragment : DaggerFragment(), AudioContact.View {

    companion object {
        fun newInstance(): AudioFragment {
            return AudioFragment()
        }
    }

    @Inject
    @JvmField
    var presenter: AudioPresenter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_audio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}