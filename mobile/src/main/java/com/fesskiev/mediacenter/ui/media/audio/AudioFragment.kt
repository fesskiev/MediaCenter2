package com.fesskiev.mediacenter.ui.media.audio

import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.ui.adapters.AudioFoldersAdapter
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_audio.*
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

    private lateinit var adapter: AudioFoldersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_audio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val gridLayoutManager = GridLayoutManager(activity, 2)
        val spacing = resources.getDimensionPixelOffset(R.dimen.default_spacing_small)
        recyclerView.layoutManager = gridLayoutManager
        adapter = AudioFoldersAdapter(this)
        adapter.setHasStableIds(true)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        presenter?.fetchAudioFolders()
    }

    override fun showProgressBar() {
        progressBar.visibility = VISIBLE
    }

    override fun hideProgressBar() {
        progressBar.visibility = GONE
    }

    override fun showAudioFolders(audioFolders: List<AudioFolder>) {

    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}