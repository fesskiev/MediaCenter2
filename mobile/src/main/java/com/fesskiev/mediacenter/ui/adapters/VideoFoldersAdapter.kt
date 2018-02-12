package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.VideoFolder
import com.fesskiev.mediacenter.ui.media.video.VideoFragment
import com.fesskiev.mediacenter.widgets.items.VideoCardView
import kotlinx.android.synthetic.main.item_video.view.*
import kotlinx.android.synthetic.main.layout_video_card_view.view.*


class VideoFoldersAdapter(videoFragment: VideoFragment) : RecyclerView.Adapter<VideoFoldersAdapter.ViewHolder>() {

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
                itemView.videoFolderCardView.selectedFolderVisibility(videoFolder.videoFolderSelected)
                itemView.videoFolderName.text = videoFolder.videoFolderName
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoFoldersAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_video, parent, false)
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