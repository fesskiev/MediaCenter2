package com.fesskiev.mediacenter.ui.media.audio

import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.ui.adapters.AudioFoldersAdapter
import com.fesskiev.mediacenter.ui.media.audio.details.AudioFilesActivity
import com.fesskiev.mediacenter.utils.Constants.Companion.EXTRA_AUDIO_FOLDER
import com.fesskiev.mediacenter.utils.invisible
import com.fesskiev.mediacenter.utils.visible
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_audio.*
import javax.inject.Inject


class AudioFoldersFragment : DaggerFragment(), AudioFoldersContact.View, AudioFoldersAdapter.OnAudioFolderAdapterListener {

    companion object {
        fun newInstance(): AudioFoldersFragment {
            return AudioFoldersFragment()
        }
    }

    @Inject
    @JvmField
    var foldersPresenter: AudioFoldersPresenter? = null

    private lateinit var adapter: AudioFoldersAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_audio, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val columns = resources.getInteger(R.integer.folder_columns)
        val gridLayoutManager = GridLayoutManager(activity, columns)
        recyclerView.layoutManager = gridLayoutManager
        adapter = AudioFoldersAdapter(foldersPresenter)
        adapter.setHasStableIds(true)
        adapter.setOnAudioFolderAdapterListener(this)
        recyclerView.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        foldersPresenter?.fetchAudioFolders()
    }

    override fun showProgressBar() {
        progressBar.visible()
    }

    override fun hideProgressBar() {
        progressBar.invisible()
    }

    override fun showAudioFolders(audioFolders: List<AudioFolder>) {
        adapter.refresh(audioFolders)
    }

    override fun showAudioFolderNotExist() {

    }

    override fun onAudioFolderClick(audioFolder: AudioFolder) {
        val exist = foldersPresenter?.checkAudioFolderExist(audioFolder) ?: false
        if (exist) {
            val i = Intent(context, AudioFilesActivity::class.java)
            i.putExtra(EXTRA_AUDIO_FOLDER, audioFolder)
            startActivity(i)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        foldersPresenter?.detach()
    }
}