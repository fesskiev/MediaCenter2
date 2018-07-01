package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.playlist.PlaylistPresenter
import com.fesskiev.mediacenter.utils.StringUtils
import com.fesskiev.mediacenter.utils.inflate
import com.fesskiev.mediacenter.widgets.items.PlaylistCardView
import kotlinx.android.synthetic.main.item_playlist.view.*
import kotlinx.android.synthetic.main.layout_playlist_card_view.view.*


class PlaylistAdapter(val presenter: PlaylistPresenter?) : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    interface OnPlaylistAdapterListener {

        fun onDeletePlaylistFile(mediaFile: MediaFile, position: Int, lastItem: Boolean)

        fun onClickFile(mediaFile: MediaFile)
    }

    private var listener: OnPlaylistAdapterListener? = null
    private var mediaFiles: MutableList<MediaFile> = ArrayList()
    private var cards: MutableList<PlaylistCardView> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindVideoFile(mediaFile: MediaFile) {
            with(mediaFile) {
                itemView.playlistCardView.setOnPlaylistCardListener(object : PlaylistCardView.OnPlaylistCardListener {

                    override fun onDeleteClick() {
                        listener?.onDeletePlaylistFile(mediaFiles[adapterPosition],
                                adapterPosition, isLastItem())
                    }

                    override fun onClick() {
                        listener?.onClickFile(mediaFiles[adapterPosition])
                    }

                    override fun onAnimateChanged(view: PlaylistCardView, open: Boolean) {
                        if (open) {
                            cards.add(view)
                        } else {
                            cards.remove(view)
                        }
                    }
                })
                itemView.playlistCardView.itemTitle.text = getTitle()
                itemView.playlistCardView.filePath.text = getFilePath()
                itemView.playlistCardView.itemSize.text = StringUtils.humanReadableByteCount(getSize(), false)
                itemView.playlistCardView.itemDuration.text = StringUtils.getDurationString(getDuration())
                presenter?.getMediaFileArtwork(mediaFile)?.subscribe({ bitmap ->
                    itemView.itemCover.setImageBitmap(bitmap)
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.inflate(R.layout.item_playlist)
        return ViewHolder(v)
    }

    override fun getItemId(position: Int) = mediaFiles[position].getTimestamp()

    override fun getItemCount() = mediaFiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindVideoFile(mediaFiles[position])
    }

    fun refresh(mediaFiles: List<MediaFile>) {
        this.mediaFiles.clear()
        this.mediaFiles.addAll(mediaFiles)
        notifyDataSetChanged()
    }

    fun remove(position: Int) {
        mediaFiles.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    fun hideOpenCards() {
        if (cards.isNotEmpty()) {
            cards.filter { it.isOpen() }.forEach { it.close() }
            cards.clear()
        }
    }

    fun isLastItem(): Boolean {
        return mediaFiles.size == 1
    }

    fun setOnMediaFilesAdapterListener(l: OnPlaylistAdapterListener) {
        this.listener = l
    }
}