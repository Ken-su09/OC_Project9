package com.suonk.oc_project9.domain.filter

import android.util.Log
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.domain.filter.model.FilterQuery
import com.suonk.oc_project9.ui.filter.Filter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ToggleFilterUseCase @Inject constructor(private val searchRepository: SearchRepository) {

    fun invoke(filterQuery: FilterQuery) {
        Log.i("FilterList", "filterQuery : $filterQuery")

        val filters = searchRepository.getCurrentFilterParametersFlow().value

        when (filterQuery) {
            is FilterQuery.LivingSpaceFilter -> {
                val previousFilter = filters.filterIsInstance<Filter.LivingSpaceFilter>().firstOrNull()

                if (previousFilter == null) {
                    val newFilter = Filter.LivingSpaceFilter(
                        (filterQuery.min as? FilterQuery.SearchParam.Update)?.value,
                        (filterQuery.max as? FilterQuery.SearchParam.Update)?.value,
                    )

                    if (newFilter.min != null || newFilter.max != null) {
                        searchRepository.addFilter(newFilter)
                    }
                } else {
                    val newMin = when (filterQuery.min) {
                        is FilterQuery.SearchParam.Update -> filterQuery.min.value
                        is FilterQuery.SearchParam.Delete -> null
                    }
                    val newMax = when (filterQuery.max) {
                        is FilterQuery.SearchParam.Update -> filterQuery.max.value
                        is FilterQuery.SearchParam.Delete -> null
                    }

                    if (newMin != null || newMax != null) {
                        searchRepository.addFilter(
                            Filter.LivingSpaceFilter(
                                min = newMin, max = newMax
                            )
                        )
                    }
                }
            }
            is FilterQuery.PriceFilter -> {
                val previousFilter = filters.filterIsInstance<Filter.PriceFilter>().firstOrNull()

                if (previousFilter == null) {
                    val newFilter = Filter.PriceFilter(
                        (filterQuery.min as? FilterQuery.SearchParam.Update)?.value,
                        (filterQuery.max as? FilterQuery.SearchParam.Update)?.value,
                    )

                    if (newFilter.min != null || newFilter.max != null) {
                        searchRepository.addFilter(newFilter)
                    }
                } else {
                    val newMin = when (filterQuery.min) {
                        is FilterQuery.SearchParam.Update -> filterQuery.min.value
                        is FilterQuery.SearchParam.Delete -> null
                    }
                    val newMax = when (filterQuery.max) {
                        is FilterQuery.SearchParam.Update -> filterQuery.max.value
                        is FilterQuery.SearchParam.Delete -> null
                    }

                    if (newMin != null || newMax != null) {
                        searchRepository.addFilter(
                            Filter.PriceFilter(
                                min = newMin, max = newMax
                            )
                        )
                    }
                }
            }
            is FilterQuery.NbRoomsFilter -> {
                val previousFilter = filters.filterIsInstance<Filter.NbRoomsFilter>().firstOrNull()

                if (previousFilter == null) {
                    val newFilter = Filter.NbRoomsFilter(
                        (filterQuery.min as? FilterQuery.SearchParam.Update)?.value,
                        (filterQuery.max as? FilterQuery.SearchParam.Update)?.value,
                    )

                    if (newFilter.min != null || newFilter.max != null) {
                        searchRepository.addFilter(newFilter)
                    }
                } else {
                    val newMin = when (filterQuery.min) {
                        is FilterQuery.SearchParam.Update -> filterQuery.min.value
                        is FilterQuery.SearchParam.Delete -> null
                    }
                    val newMax = when (filterQuery.max) {
                        is FilterQuery.SearchParam.Update -> filterQuery.max.value
                        is FilterQuery.SearchParam.Delete -> null
                    }

                    if (newMin != null || newMax != null) {
                        searchRepository.addFilter(
                            Filter.NbRoomsFilter(
                                min = newMin, max = newMax
                            )
                        )
                    }
                }
            }
            is FilterQuery.NbBedroomsFilter -> {
                val previousFilter = filters.filterIsInstance<Filter.NbBedroomsFilter>().firstOrNull()

                if (previousFilter == null) {
                    val newFilter = Filter.NbBedroomsFilter(
                        (filterQuery.min as? FilterQuery.SearchParam.Update)?.value,
                        (filterQuery.max as? FilterQuery.SearchParam.Update)?.value,
                    )

                    if (newFilter.min != null || newFilter.max != null) {
                        searchRepository.addFilter(newFilter)
                    }
                } else {
                    val newMin = when (filterQuery.min) {
                        is FilterQuery.SearchParam.Update -> filterQuery.min.value
                        is FilterQuery.SearchParam.Delete -> null
                    }
                    val newMax = when (filterQuery.max) {
                        is FilterQuery.SearchParam.Update -> filterQuery.max.value
                        is FilterQuery.SearchParam.Delete -> null
                    }

                    if (newMin != null || newMax != null) {
                        searchRepository.addFilter(
                            Filter.NbBedroomsFilter(
                                min = newMin, max = newMax
                            )
                        )
                    }
                }
            }
            is FilterQuery.EntryDateFilter -> {
                val previousFilter = filters.filterIsInstance<Filter.EntryDateFilter>().firstOrNull()

                if (previousFilter == null) {
                    val newFilter = Filter.EntryDateFilter(
                        (filterQuery.from as? FilterQuery.SearchParam.Update)?.value,
                        (filterQuery.to as? FilterQuery.SearchParam.Update)?.value,
                    )

                    if (newFilter.from != null || newFilter.to != null) {
                        searchRepository.addFilter(newFilter)
                    }
                } else {
                    val newMin = when (filterQuery.from) {
                        is FilterQuery.SearchParam.Update -> filterQuery.from.value
                        is FilterQuery.SearchParam.Delete -> null
                    }
                    val newMax = when (filterQuery.to) {
                        is FilterQuery.SearchParam.Update -> filterQuery.to.value
                        is FilterQuery.SearchParam.Delete -> null
                    }


                    if (newMin != null || newMax != null) {
                        searchRepository.addFilter(Filter.EntryDateFilter(from = newMin, to = newMax))
                    }
                }
            }
            is FilterQuery.SaleDateFilter -> {
                val previousFilter = filters.filterIsInstance<Filter.SaleDateFilter>().firstOrNull()

                if (previousFilter == null) {
                    val newFilter = Filter.SaleDateFilter(
                        (filterQuery.from as? FilterQuery.SearchParam.Update)?.value,
                        (filterQuery.to as? FilterQuery.SearchParam.Update)?.value,
                    )

                    if (newFilter.from != null || newFilter.to != null) {
                        searchRepository.addFilter(newFilter)
                    }
                } else {
                    val newMin = when (filterQuery.from) {
                        is FilterQuery.SearchParam.Update -> filterQuery.from.value
                        is FilterQuery.SearchParam.Delete -> null
                    }
                    val newMax = when (filterQuery.to) {
                        is FilterQuery.SearchParam.Update -> filterQuery.to.value
                        is FilterQuery.SearchParam.Delete -> null
                    }

                    if (newMin != null || newMax != null) {
                        searchRepository.addFilter(Filter.SaleDateFilter(from = newMin, to = newMax))
                    }
                }
            }
        }
    }

    // reified (with inline)
}

