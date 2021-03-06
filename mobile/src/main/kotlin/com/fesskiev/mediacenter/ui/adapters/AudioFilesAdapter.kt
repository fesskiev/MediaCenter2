package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.AudioFile
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.utils.StringUtils
import com.fesskiev.mediacenter.utils.inflate
import com.fesskiev.mediacenter.widgets.items.AudioFileCardView
import kotlinx.android.synthetic.main.item_audio_file.view.*
import kotlinx.android.synthetic.main.layout_audio_file_card_view.view.*

class AudioFilesAdapter : RecyclerView.Adapter<AudioFilesAdapter.ViewHolder>() {

    interface OnAudioFilesAdapterListener {

        fun onDeleteFile(audioFile: AudioFile, position: Int)

        fun onEditFile(audioFile: AudioFile)

        fun onPlaylistFile(audioFile: AudioFile)

        fun onClickFile(audioFile: AudioFile)
    }

    private var listener: OnAudioFilesAdapterListener? = null
    private var audioFiles: MutableList<AudioFile> = ArrayList()
    private var cards: MutableList<AudioFileCardView> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindAudioFile(audioFile: AudioFile) {
            with(audioFile) {
                itemView.cardAudioFile.setOnFileCardListener(object : AudioFileCardView.OnFileCardListener {
                    override fun onDeleteClick() {
                        listener?.onDeleteFile(audioFiles[adapterPosition], adapterPosition)
                    }

                    override fun onEditClick() {
                        listener?.onEditFile(audioFiles[adapterPosition])
                    }

                    override fun onPlaylistClick() {
                        listener?.onPlaylistFile(audioFiles[adapterPosition])
                    }

                    override fun onClick() {
                        listener?.onClickFile(audioFiles[adapterPosition])
                    }

                    override fun onAnimateChanged(view: AudioFileCardView, open: Boolean) {
                        if (open) {
                            cards.add(view)
                        } else {
                            cards.remove(view)
                        }
                    }
                })
                itemView.cardAudioFile.itemTitle.text = getTitle()
                itemView.cardAudioFile.filePath.text = getFilePath()
                itemView.cardAudioFile.itemSize.text = StringUtils.humanReadableByteCount(getSize(), false)
                itemView.cardAudioFile.itemDuration.text = StringUtils.getDurationString(getDuration())
                itemView.cardAudioFile.itemTrackNumber.text = audioFileTrackNumber.toString()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.inflate(R.layout.item_audio_file)
        return ViewHolder(v)
    }

    override fun getItemId(position: Int) = audioFiles[position].getTimestamp()

    override fun getItemCount() = audioFiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindAudioFile(audioFiles[position])
    }

    fun refresh(mediaFiles: List<AudioFile>) {
        this.audioFiles.clear()
        this.audioFiles.addAll(mediaFiles)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        audioFiles.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun hideOpenCards() {
        if (cards.isNotEmpty()) {
            cards.filter { it.isOpen() }.forEach { it.close() }
            cards.clear()
        }
    }

    fun setOnAudioFilesAdapterListener(l: OnAudioFilesAdapterListener) {
        this.listener = l
    }
}