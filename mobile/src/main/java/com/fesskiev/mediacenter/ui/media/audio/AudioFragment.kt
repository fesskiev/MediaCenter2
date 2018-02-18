package com.fesskiev.mediacenter.ui.media.audio

import android.content.Intent
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
import com.fesskiev.mediacenter.ui.media.audio.details.AudioFilesActivity
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_AUDIO_FOLDER
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_audio.*
import javax.inject.Inject


class AudioFragment : DaggerFragment(), AudioContact.View, AudioFoldersAdapter.OnAudioFolderAdapterListener {

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
        recyclerView.layoutManager = gridLayoutManager
        adapter = AudioFoldersAdapter(presenter)
        adapter.setHasStableIds(true)
        adapter.setOnAudioFolderAdapterListener(this)
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
        adapter.refresh(audioFolders)
    }

    override fun showAudioFolderNotExist() {

    }

    override fun onAudioFolderClick(audioFolder: AudioFolder) {
        val exist = presenter?.checkAudioFolderExist(audioFolder) ?: false
        if (exist) {
            val i = Intent(context, AudioFilesActivity::class.java)
            i.putExtra(EXTRA_AUDIO_FOLDER, audioFolder)
            startActivity(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.detach()
    }
}