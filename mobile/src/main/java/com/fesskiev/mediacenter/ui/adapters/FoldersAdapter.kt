package com.fesskiev.mediacenter.ui.adapters

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.fesskiev.mediacenter.R
import com.fesskiev.mediacenter.ui.media.folders.FoldersPresenter
import com.fesskiev.mediacenter.utils.inflate
import java.io.File
import java.util.ArrayList
import kotlinx.android.synthetic.main.item_folder_content.view.*

class FoldersAdapter(private var presenter: FoldersPresenter?): RecyclerView.Adapter<FoldersAdapter.ViewHolder>() {

    interface OnFoldersAdapterListener {

        fun onClickFile(file: File)
    }

    private val files: MutableList<File> = ArrayList()
    private var listener: OnFoldersAdapterListener? = null

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bindFile(file: File) {
            with(file) {
                itemView.itemFileName.text = name
            }
            when {
                file.isDirectory -> itemView.itemIcon.setImageResource(R.drawable.ic_folder)
                file.isFile -> itemView.itemIcon.setImageResource(R.drawable.ic_file)
                else -> itemView.itemIcon.setImageResource(0)
            }
            itemView.setOnClickListener { listener?.onClickFile(files[adapterPosition]) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = parent.inflate(R.layout.item_folder_content)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindFile(files[position])
    }

    override fun getItemCount(): Int {
        return files.size
    }

    fun refresh(files: List<File>) {
        this.files.clear()
        this.files.addAll(files)
        notifyDataSetChanged()
    }

    fun setOnFoldersAdapterListener(l: OnFoldersAdapterListener) {
        this.listener = l
    }
}