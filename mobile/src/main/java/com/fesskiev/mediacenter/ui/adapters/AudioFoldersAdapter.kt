package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.ui.media.audio.AudioFragment
import kotlinx.android.synthetic.main.layout_audio_card_view.view.*
import kotlinx.android.synthetic.main.item_audio.view.*


class AudioFoldersAdapter(audioFragment: AudioFragment) : RecyclerView.Adapter<AudioFoldersAdapter.ViewHolder>() {

    private var audioFolders: MutableList<AudioFolder> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindAudioFolder(audioFolder: AudioFolder) {
            with(audioFolder) {
                itemView.audioFolderCardView.selectedFolderVisibility(audioFolder.audioFolderSelected)
                itemView.albumName.text = audioFolder.audioFolderName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_audio, parent, false)
        return ViewHolder(v)
    }

    override fun getItemId(position: Int) = audioFolders[position].audioFolderTimestamp

    override fun getItemCount() = audioFolders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAudioFolder(audioFolders[position])
    }

    fun refresh(audioFolders: List<AudioFolder>) {
        this.audioFolders.clear()
        this.audioFolders.addAll(audioFolders)
        notifyDataSetChanged()
    }
}