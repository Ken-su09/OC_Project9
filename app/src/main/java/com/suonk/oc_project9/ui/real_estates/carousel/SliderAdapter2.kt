package com.suonk.oc_project9.ui.real_estates.carousel

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suonk.oc_project9.databinding.SliderItemContainerLayoutBinding
import java.io.File

class SliderAdapter2 :
    ListAdapter<Uri, SliderAdapter2.SliderViewHolder>(PhotosComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return (SliderViewHolder(
            SliderItemContainerLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        ))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class SliderViewHolder(private val binding: SliderItemContainerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(uri: Uri) {
            Glide.with(binding.realEstateImage).load(uri).into(binding.realEstateImage)
        }
    }

    object PhotosComparator : DiffUtil.ItemCallback<Uri>() {
        override fun areItemsTheSame(
            oldItem: Uri, newItem: Uri
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: Uri, newItem: Uri
        ): Boolean = oldItem == newItem
    }
}