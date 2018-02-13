package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.domain.entity.media.MediaFile
import com.fesskiev.mediacenter.ui.media.files.FilesFragment
import com.fesskiev.mediacenter.utils.StringUtils
import com.fesskiev.mediacenter.widgets.items.FileCardView
import kotlinx.android.synthetic.main.item_file.view.*
import kotlinx.android.synthetic.main.layout_file_card_view.view.*


class MediaFilesAdapter(filesFragment: FilesFragment) : RecyclerView.Adapter<MediaFilesAdapter.ViewHolder>() {

    private var mediaFiles: MutableList<MediaFile> = ArrayList()

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindMediaFile(mediaFile: MediaFile) {
            with(mediaFile) {
                itemView.cardFile.setOnFileCardListener(object : FileCardView.OnFileCardListener {

                    override fun onDeleteClick() {

                    }

                    override fun onEditClick() {

                    }

                    override fun onPlayListClick() {

                    }

                    override fun onClick() {

                    }

                    override fun onAnimateChanged(view: FileCardView, open: Boolean) {

                    }
                })
                itemView.cardFile.itemTitle.text = mediaFile.getTitle()
                itemView.cardFile.filePath.text = mediaFile.getFilePath()
                itemView.cardFile.itemDuration.text = StringUtils.getDurationString(mediaFile.getDuration())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
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
}