package com.suonk.oc_project9.ui.real_estates.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suonk.oc_project9.databinding.SliderItemContainerLayoutBinding

class ListSliderAdapter : ListAdapter<ListPhotoViewState, ListSliderAdapter.SliderViewHolder>(PhotosComparator) {

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

    class SliderViewHolder(private val binding: SliderItemContainerLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(listPhotoViewState: ListPhotoViewState) {
            binding.realEstateImage.setImageURI(listPhotoViewState.uri)
            binding.deletePhoto.isVisible = false
        }
    }

    object PhotosComparator : DiffUtil.ItemCallback<ListPhotoViewState>() {
        override fun areItemsTheSame(
            oldItem: ListPhotoViewState, newItem: ListPhotoViewState
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: ListPhotoViewState, newItem: ListPhotoViewState
        ): Boolean = oldItem == newItem
    }
}