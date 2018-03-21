package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.VideoFile
import com.fesskiev.mediacenter.utils.inflate


class VideoFilesAdapter : RecyclerView.Adapter<VideoFilesAdapter.ViewHolder>() {

    private var videoFiles: MutableList<VideoFile> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindVideoFile(videoFile: VideoFile) {
            with(videoFile) {

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
}