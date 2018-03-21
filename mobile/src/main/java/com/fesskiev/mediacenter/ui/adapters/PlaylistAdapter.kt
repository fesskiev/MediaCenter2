package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.utils.inflate


class PlaylistAdapter : RecyclerView.Adapter<PlaylistAdapter.ViewHolder>() {

    private var mediaFiles: MutableList<MediaFile> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindVideoFile(mediaFile: MediaFile) {
            with(mediaFile) {

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
}