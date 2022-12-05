package com.suonk.oc_project9.ui.real_estates.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.suonk.oc_project9.databinding.ItemRealEstatesListBinding
import kotlin.math.abs

class RealEstatesListAdapter() :
    ListAdapter<RealEstatesListViewState, RealEstatesListAdapter.ViewHolder>(
        RealEstatesViewStateComparator
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemRealEstatesListBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class ViewHolder(private val binding: ItemRealEstatesListBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(estate: RealEstatesListViewState) {
            binding.address.text = estate.address
            binding.type.text = estate.type
            binding.nbRooms.text = estate.numberRooms
            binding.squareFoot.text = estate.livingSpace
            binding.price.text = estate.price

            val sliderAdapter = SliderAdapter()
            sliderAdapter.submitList(estate.photos)
            binding.images.adapter = sliderAdapter
            binding.images.clipToPadding = false
            binding.images.clipChildren = false

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(30))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.25f
            }

            binding.images.setPageTransformer(compositePageTransformer)
            binding.images.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                }
            })

            itemView.setOnClickListener {
                estate.onClickedCallback(estate.id)
            }
        }
    }

    object RealEstatesViewStateComparator : DiffUtil.ItemCallback<RealEstatesListViewState>() {
        override fun areItemsTheSame(
            oldItem: RealEstatesListViewState,
            newItem: RealEstatesListViewState
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: RealEstatesListViewState,
            newItem: RealEstatesListViewState
        ): Boolean = oldItem == newItem
    }
}