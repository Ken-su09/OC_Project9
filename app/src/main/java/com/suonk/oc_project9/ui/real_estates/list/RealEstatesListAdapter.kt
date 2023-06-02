package com.suonk.oc_project9.ui.real_estates.list

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.suonk.oc_project9.databinding.ItemRealEstatesListBinding
import com.suonk.oc_project9.ui.real_estates.details.DetailsSliderAdapter
import kotlin.math.abs

class RealEstatesListAdapter : ListAdapter<RealEstatesListViewState, RealEstatesListAdapter.ViewHolder>(RealEstatesViewStateComparator) {

    private val viewPool = RecyclerView.RecycledViewPool()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(
        ItemRealEstatesListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind(getItem(position), viewPool)
    }

    class ViewHolder(private val binding: ItemRealEstatesListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(estate: RealEstatesListViewState, viewPool: RecyclerView.RecycledViewPool) {
            binding.address.text = estate.address
            binding.type.text = estate.type
            binding.nbRooms.text = estate.numberRooms
            binding.squareFoot.text = estate.livingSpace
            binding.price.text = estate.price
            binding.entryDate.text = estate.entryDate

            binding.saleDate.text = estate.saleDate
//            binding.saleDate.isVisible = estate.isSold
            binding.iconIsSold.isVisible = estate.isSold

            val listSliderAdapter = ListSliderAdapter()
            listSliderAdapter.submitList(estate.photos)
            binding.images.adapter = listSliderAdapter
            binding.images.clipToPadding = false
            binding.images.clipChildren = false

            val compositePageTransformer = CompositePageTransformer()
            compositePageTransformer.addTransformer(MarginPageTransformer(30))
            compositePageTransformer.addTransformer { page, position ->
                val r = 1 - abs(position)
                page.scaleY = 0.85f + r * 0.25f
            }

            binding.images.setPageTransformer(compositePageTransformer)
            binding.images.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {})

            itemView.setOnClickListener {
                estate.onClickedCallback()
            }
        }
    }

    object RealEstatesViewStateComparator : DiffUtil.ItemCallback<RealEstatesListViewState>() {
        override fun areItemsTheSame(
            oldItem: RealEstatesListViewState, newItem: RealEstatesListViewState
        ): Boolean = oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: RealEstatesListViewState, newItem: RealEstatesListViewState
        ): Boolean = oldItem == newItem
    }
}