package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFolder
import com.fesskiev.mediacenter.ui.media.audio.AudioPresenter
import com.fesskiev.mediacenter.utils.inflate
import com.fesskiev.mediacenter.widgets.items.AudioCardView
import kotlinx.android.synthetic.main.layout_audio_card_view.view.*
import kotlinx.android.synthetic.main.item_audio.view.*


class AudioFoldersAdapter(private var presenter: AudioPresenter?) : RecyclerView.Adapter<AudioFoldersAdapter.ViewHolder>() {

    interface OnAudioFolderAdapterListener {

        fun onAudioFolderClick(audioFolder: AudioFolder)
    }

    private var audioFolders: MutableList<AudioFolder> = ArrayList()
    private var listener: OnAudioFolderAdapterListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindAudioFolder(audioFolder: AudioFolder) {
            with(audioFolder) {
                itemView.audioFolderCardView.setOnAudioCardViewListener(object :
                        AudioCardView.OnAudioCardViewListener {

                    override fun onPopupMenuClick(view: View) {

                    }

                    override fun onAudioFolderClick() {
                        listener?.onAudioFolderClick(audioFolders[adapterPosition])
                    }
                })
                itemView.audioFolderCardView.selectedFolderVisibility(audioFolderSelected)
                itemView.albumName.text = audioFolderName
                presenter?.getAudioFolderArtwork(audioFolder)?.subscribe({ bitmap ->
                    itemView.albumCover.setImageBitmap(bitmap) })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.inflate(R.layout.item_audio)
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

    fun setOnAudioFolderAdapterListener(l: OnAudioFolderAdapterListener) {
        this.listener = l
    }
}