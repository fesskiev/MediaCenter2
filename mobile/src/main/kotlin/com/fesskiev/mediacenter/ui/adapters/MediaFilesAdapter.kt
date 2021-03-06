package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.media.files.FilesPresenter
import com.fesskiev.mediacenter.utils.StringUtils
import com.fesskiev.mediacenter.utils.inflate
import com.fesskiev.mediacenter.widgets.items.MediaFileCardView
import kotlinx.android.synthetic.main.item_media_file.view.*
import kotlinx.android.synthetic.main.layout_media_file_card_view.view.*


class MediaFilesAdapter(private var presenter: FilesPresenter?) : RecyclerView.Adapter<MediaFilesAdapter.ViewHolder>() {

    interface OnMediaFilesAdapterListener {

        fun onDeleteFile(mediaFile: MediaFile)

        fun onEditFile(mediaFile: MediaFile)

        fun onPlayListFile(mediaFile: MediaFile)

        fun onClickFile(mediaFile: MediaFile)
    }

    private var listener: OnMediaFilesAdapterListener? = null
    private var mediaFiles: MutableList<MediaFile> = ArrayList()
    private var cards: MutableList<MediaFileCardView> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindMediaFile(mediaFile: MediaFile) {
            with(mediaFile) {
                itemView.cardFile.setOnFileCardListener(object : MediaFileCardView.OnFileCardListener {
                    override fun onDeleteClick() {
                        listener?.onDeleteFile(mediaFiles[adapterPosition])
                    }

                    override fun onEditClick() {
                        listener?.onEditFile(mediaFiles[adapterPosition])
                    }

                    override fun onPlayListClick() {
                        listener?.onPlayListFile(mediaFiles[adapterPosition])
                    }

                    override fun onClick() {
                        listener?.onClickFile(mediaFiles[adapterPosition])
                    }

                    override fun onAnimateChanged(view: MediaFileCardView, open: Boolean) {
                        if (open) {
                            cards.add(view)
                        } else {
                            cards.remove(view)
                        }
                    }
                })
                itemView.cardFile.itemTitle.text = getTitle()
                itemView.cardFile.filePath.text = getFilePath()
                itemView.cardFile.itemSize.text = StringUtils.humanReadableByteCount(getSize(), false)
                itemView.cardFile.itemDuration.text = StringUtils.getDurationString(getDuration())
                presenter?.getMediaFileArtwork(mediaFile)?.subscribe({ bitmap ->
                    itemView.itemCover.setImageBitmap(bitmap)
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.inflate(R.layout.item_media_file)
        return ViewHolder(v)
    }

    override fun getItemId(position: Int) = mediaFiles[position].getTimestamp()

    override fun getItemCount() = mediaFiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindMediaFile(mediaFiles[position])
    }

    fun refresh(mediaFiles: List<MediaFile>) {
        this.mediaFiles.clear()
        this.mediaFiles.addAll(mediaFiles)
        notifyDataSetChanged()
    }

    fun clear() {
        this.mediaFiles.clear()
        notifyDataSetChanged()
    }

    fun hideOpenCards() {
        if (cards.isNotEmpty()) {
            cards.filter { it.isOpen() }.forEach { it.close() }
            cards.clear()
        }
    }

    fun setOnMediaFilesAdapterListener(l: OnMediaFilesAdapterListener) {
        this.listener = l
    }
}