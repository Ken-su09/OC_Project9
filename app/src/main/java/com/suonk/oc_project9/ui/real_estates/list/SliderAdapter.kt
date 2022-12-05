package com.suonk.oc_project9.ui.real_estates.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.suonk.oc_project9.databinding.SliderItemContainerLayoutBinding

class SliderAdapter : ListAdapter<String, SliderAdapter.SliderViewHolder>(PhotosComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        return (SliderViewHolder(
            SliderItemContainerLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        ))
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class SliderViewHolder(private val binding: SliderItemContainerLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(photo: String) {
            Glide.with(binding.realEstateImage)
                .load(photo)
                .into(binding.realEstateImage)
        }
    }

    object PhotosComparator : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: String,
            newItem: String
        ): Boolean = oldItem == newItem
    }
}