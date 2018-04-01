package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.ui.media.video.details.VideoFilesPresenter
import com.fesskiev.mediacenter.utils.StringUtils
import com.fesskiev.mediacenter.utils.inflate
import com.fesskiev.mediacenter.widgets.items.VideoFileCardView
import kotlinx.android.synthetic.main.item_video_file.view.*
import kotlinx.android.synthetic.main.layout_video_file_card_view.view.*


class VideoFilesAdapter(private var presenter: VideoFilesPresenter?) : RecyclerView.Adapter<VideoFilesAdapter.ViewHolder>() {

    interface OnVideoFilesAdapterListener {

        fun onDeleteFile(mediaFile: MediaFile)

        fun onEditFile(mediaFile: MediaFile)

        fun onPlayListFile(mediaFile: MediaFile)

        fun onClickFile(mediaFile: MediaFile)
    }

    private var listener: OnVideoFilesAdapterListener? = null
    private var videoFiles: MutableList<VideoFile> = ArrayList()
    private var cards: MutableList<VideoFileCardView> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindVideoFile(videoFile: VideoFile) {
            with(videoFile) {
                itemView.cardVideoFile.setOnFileCardListener(object : VideoFileCardView.OnFileCardListener {
                    override fun onDeleteClick() {
                        listener?.onDeleteFile(videoFiles[adapterPosition])
                    }

                    override fun onEditClick() {
                        listener?.onEditFile(videoFiles[adapterPosition])
                    }

                    override fun onPlayListClick() {
                        listener?.onPlayListFile(videoFiles[adapterPosition])
                    }

                    override fun onClick() {
                        listener?.onClickFile(videoFiles[adapterPosition])
                    }

                    override fun onAnimateChanged(view: VideoFileCardView, open: Boolean) {
                        if (open) {
                            cards.add(view)
                        } else {
                            cards.remove(view)
                        }
                    }
                })
                itemView.cardVideoFile.itemTitle.text = getTitle()
                itemView.cardVideoFile.filePath.text = getFilePath()
                itemView.cardVideoFile.itemSize.text = StringUtils.humanReadableByteCount(getSize(), false)
                itemView.cardVideoFile.itemDuration.text = StringUtils.getDurationString(getDuration())
                itemView.cardVideoFile.itemResolution.text = videoFileResolution
                presenter?.getVideoFileArtwork(videoFile)?.subscribe({ bitmap ->
                    itemView.itemCover.setImageBitmap(bitmap)
                })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.inflate(R.layout.item_video_file)
        return ViewHolder(v)
    }

    override fun getItemId(position: Int) = videoFiles[position].getTimestamp()

    override fun getItemCount() = videoFiles.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindVideoFile(videoFiles[position])
    }

    fun refresh(videoFiles: List<VideoFile>) {
        this.videoFiles.clear()
        this.videoFiles.addAll(videoFiles)
        notifyDataSetChanged()
    }

    fun hideOpenCards() {
        if (cards.isNotEmpty()) {
            cards.filter { it.isOpen() }.forEach { it.close() }
            cards.clear()
        }
    }

    fun setOnVideoFilesAdapterListener(l: OnVideoFilesAdapterListener) {
        this.listener = l
    }
}