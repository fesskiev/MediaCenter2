package com.fesskiev.mediacenter.ui.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.fesskiev.mediacenter.R

internal class BottomSheetAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return object : RecyclerView.ViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.view_holder_item, parent, false)) {
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {}

    override fun getItemCount() = 5
}