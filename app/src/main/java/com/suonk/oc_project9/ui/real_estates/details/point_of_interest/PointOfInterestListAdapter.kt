package com.suonk.oc_project9.ui.real_estates.details.point_of_interest

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.suonk.oc_project9.databinding.ItemPointOfInterestBinding
import com.suonk.oc_project9.databinding.ItemRealEstatesListBinding
import com.suonk.oc_project9.model.database.data.entities.places.PointOfInterest
import kotlin.math.abs

class PointOfInterestListAdapter : ListAdapter<PointOfInterestViewState, PointOfInterestListAdapter.ViewHolder>(PointOfInterestComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemPointOfInterestBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class ViewHolder(private val binding: ItemPointOfInterestBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(pointOfInterest: PointOfInterestViewState) {
            binding.name.text = pointOfInterest.name
            binding.types.text = pointOfInterest.types
            binding.address.text = pointOfInterest.address

            Glide.with(binding.image).load(pointOfInterest.icon).into(binding.image)
        }
    }

    object PointOfInterestComparator : DiffUtil.ItemCallback<PointOfInterestViewState>() {
        override fun areItemsTheSame(
            oldItem: PointOfInterestViewState, newItem: PointOfInterestViewState
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: PointOfInterestViewState, newItem: PointOfInterestViewState
        ): Boolean = oldItem == newItem
    }
}