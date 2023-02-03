package com.suonk.oc_project9.ui.real_estates.details

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suonk.oc_project9.databinding.SliderItemContainerLayoutBinding

class DetailsSliderAdapter : ListAdapter<DetailsPhotoViewState, DetailsSliderAdapter.SliderViewHolder>(PhotosComparator) {

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
        fun onBind(detailsPhotoViewState: DetailsPhotoViewState) {
            binding.realEstateImage.setImageURI(detailsPhotoViewState.uri)
            binding.deletePhoto.setOnClickListener {
                detailsPhotoViewState.onDeleteCallback()
            }
        }
    }

    object PhotosComparator : DiffUtil.ItemCallback<DetailsPhotoViewState>() {
        override fun areItemsTheSame(
            oldItem: DetailsPhotoViewState, newItem: DetailsPhotoViewState
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: DetailsPhotoViewState, newItem: DetailsPhotoViewState
        ): Boolean = true
    }
}