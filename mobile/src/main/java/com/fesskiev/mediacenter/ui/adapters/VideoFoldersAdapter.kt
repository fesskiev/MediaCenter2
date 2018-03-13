package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.media.video.VideoPresenter
import com.fesskiev.mediacenter.utils.inflate
import com.fesskiev.mediacenter.widgets.items.VideoCardView
import kotlinx.android.synthetic.main.item_video.view.*
import kotlinx.android.synthetic.main.layout_video_card_view.view.*


class VideoFoldersAdapter(private var presenter: VideoPresenter?) : RecyclerView.Adapter<VideoFoldersAdapter.ViewHolder>() {

    interface OnVideoFolderAdapterListener {

        fun onVideoFolderClick(videoFolder: VideoFolder)
    }

    private var videoFolders: MutableList<VideoFolder> = ArrayList()
    private var listener: OnVideoFolderAdapterListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindVideoFolder(videoFolder: VideoFolder) {
            with(videoFolder) {
                itemView.videoFolderCardView.setOnVideoCardViewListener(object :
                        VideoCardView.OnVideoCardViewListener {

                    override fun onPopupMenuClick(view: View) {

                    }

                    override fun onVideoFolderClick() {
                        listener?.onVideoFolderClick(videoFolders[adapterPosition])
                    }
                })
                itemView.videoFolderCardView.selectedFolderVisibility(videoFolderSelected)
                itemView.videoFolderName.text = videoFolderName
                presenter?.getVideoFolderArtwork(videoFolder)?.subscribe({ bitmap ->
                    itemView.videoFrame.setImageBitmap(bitmap) })
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoFoldersAdapter.ViewHolder {
        val v = parent.inflate(R.layout.item_video)
        return ViewHolder(v)
    }

    override fun getItemId(position: Int) = videoFolders[position].videoFolderTimestamp

    override fun getItemCount(): Int {
        return videoFolders.size
    }

    override fun onBindViewHolder(holder: VideoFoldersAdapter.ViewHolder, position: Int) {
        holder.bindVideoFolder(videoFolders[position])
    }

    fun refresh(videoFolders: List<VideoFolder>) {
        this.videoFolders.clear()
        this.videoFolders.addAll(videoFolders)
        notifyDataSetChanged()
    }

    fun setOnVideoFolderAdapterListener(l: OnVideoFolderAdapterListener) {
        this.listener = l
    }
}