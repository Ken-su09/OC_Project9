package com.suonk.oc_project9.ui.filter

import android.app.DatePickerDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.suonk.oc_project9.R
import com.suonk.oc_project9.databinding.ItemFilterDateListBinding
import com.suonk.oc_project9.databinding.ItemSearchListBinding
import java.time.LocalDate

class SearchListAdapter() : ListAdapter<SearchViewState, RecyclerView.ViewHolder>(SearchViewStateComparator) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (SearchViewState.SearchType.values()[viewType]) {
            SearchViewState.SearchType.BOUNDED -> {
                BoundedViewHolder(
                    ItemSearchListBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            SearchViewState.SearchType.DATE -> {
                DateViewHolder(
                    ItemFilterDateListBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItem(position)) {
            is SearchViewState.Bounded -> (holder as BoundedViewHolder).onBind(item)
            is SearchViewState.Date -> (holder as DateViewHolder).onBind(item)
        }
    }

    override fun getItemViewType(position: Int): Int = getItem(position).type.ordinal

    class BoundedViewHolder(private val binding: ItemSearchListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun onBind(searchViewState: SearchViewState) {
            binding.title.text = searchViewState.title
            binding.min.setText(searchViewState.min)
            binding.max.setText(searchViewState.max)

            binding.min.doAfterTextChanged {
                (searchViewState as SearchViewState.Bounded).onValuesSelected(it?.toString(), false)
            }

            binding.max.doAfterTextChanged {
                (searchViewState as SearchViewState.Bounded).onValuesSelected(it?.toString(), true)
            }
        }
    }

    class DateViewHolder(private val binding: ItemFilterDateListBinding) : RecyclerView.ViewHolder(binding.root) {

        fun onBind(searchViewState: SearchViewState) {
            val current = LocalDate.now()

            binding.title.text = searchViewState.title
            binding.from.setText(searchViewState.min)
            binding.to.setText(searchViewState.max)


            binding.from.setOnClickListener { view ->
                val datePickerDialog = DatePickerDialog(
                    view.context, { _, year, month, dayOfMonth ->
                        (searchViewState as SearchViewState.Date).onValuesSelected(year, month, dayOfMonth, true)

                        if (month + 1 < 10) {
                            binding.from.setText(
                                view.context.getString(R.string.start_time_date_picker_0, month + 1, dayOfMonth, year)
                            )
                        } else {
                            binding.from.setText(
                                view.context.getString(
                                    R.string.start_time_date_picker, month + 1, dayOfMonth, year
                                )
                            )
                        }
                    }, current.year, current.month.value - 2, current.dayOfMonth
                )
                datePickerDialog.show()
            }

            binding.to.setOnClickListener { view ->
                val datePickerDialog = DatePickerDialog(
                    view.context, { p0, year, month, dayOfMonth ->
                        (searchViewState as SearchViewState.Date).onValuesSelected(year, month, dayOfMonth, false)
                        if (month + 1 < 10) {
                            binding.to.setText(
                                view.context.getString(
                                    R.string.start_time_date_picker_0, month + 1, dayOfMonth, year
                                )
                            )
                        } else {
                            binding.to.setText(
                                view.context.getString(
                                    R.string.start_time_date_picker, month + 1, dayOfMonth, year
                                )
                            )
                        }
                    }, current.year, current.month.value - 2, current.dayOfMonth
                )
                datePickerDialog.show()
            }
        }
    }

    object SearchViewStateComparator : DiffUtil.ItemCallback<SearchViewState>() {
        override fun areItemsTheSame(
            oldItem: SearchViewState, newItem: SearchViewState
        ): Boolean = oldItem.min == newItem.min

        override fun areContentsTheSame(
            oldItem: SearchViewState, newItem: SearchViewState
        ): Boolean = oldItem == newItem
    }
}