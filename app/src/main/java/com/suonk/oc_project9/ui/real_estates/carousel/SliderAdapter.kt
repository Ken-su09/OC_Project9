package com.suonk.oc_project9.ui.real_estates.carousel

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.modernstorage.storage.AndroidFileSystem
import com.google.modernstorage.storage.toOkioPath
import com.suonk.oc_project9.databinding.SliderItemContainerLayoutBinding
import okio.Path.Companion.toOkioPath
import java.io.File

class SliderAdapter :
    ListAdapter<PhotoViewState, SliderAdapter.SliderViewHolder>(PhotosComparator) {

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

        fun onBind(photoViewState: PhotoViewState) {
            val photo = if (photoViewState.isUri) {
                Uri.parse(photoViewState.photo)
            } else {
                photoViewState.photo
            }

            Glide.with(binding.realEstateImage).load(photo).into(binding.realEstateImage)
        }
    }

    object PhotosComparator : DiffUtil.ItemCallback<PhotoViewState>() {
        override fun areItemsTheSame(
            oldItem: PhotoViewState, newItem: PhotoViewState
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: PhotoViewState, newItem: PhotoViewState
        ): Boolean = oldItem == newItem
    }
}