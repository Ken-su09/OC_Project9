package com.suonk.oc_project9.ui.filter

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.domain.filter.ToggleFilterUseCase
import com.suonk.oc_project9.domain.filter.model.FilterQuery
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.filter.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val toggleFilterUseCase: ToggleFilterUseCase,
    private val searchRepository: SearchRepository,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context
) : ViewModel() {

    val viewStateLiveData: LiveData<List<SearchViewState>> = liveData(coroutineDispatcherProvider.io) {
        searchRepository.getCurrentSearchParametersFlow().collect { filters: List<Filter> ->
            val searchViewStateList = arrayListOf<SearchViewState>()

            if (filters.isEmpty()) {
                searchViewStateList.addAll(arrayListOf(
                    Filter.LivingSpaceFilter(null, null),
                    Filter.PriceFilter(null, null),
                    Filter.NbRoomsFilter(null, null),
                    Filter.NbBedroomsFilter(null, null),
                    Filter.EntryDateFilter(null, null),
                    Filter.SaleDateFilter(null, null),
                ).map { filter ->
                    when (filter) {
                        is Filter.LivingSpaceFilter -> SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                            max = filter.max?.toString() ?: "",
                            title = context.getString(R.string.living_space_title),
                            onValuesSelected = { value, isMax ->
                                updateFilter(value, isMax, FilterType.LivingSpace)
                            })
                        is Filter.PriceFilter -> SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                            max = filter.max?.toString() ?: "",
                            title = context.getString(R.string.price_title),
                            onValuesSelected = { value, isMax ->
                                updateFilter(value, isMax, FilterType.Price)
                            })
                        is Filter.NbRoomsFilter -> SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                            max = filter.max?.toString() ?: "",
                            title = context.getString(R.string.nb_rooms_title),
                            onValuesSelected = { value, isMax ->
                                updateFilter(value, isMax, FilterType.NbRooms)
                            })
                        is Filter.NbBedroomsFilter -> SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                            max = filter.max?.toString() ?: "",
                            title = context.getString(R.string.nb_bedrooms_title),
                            onValuesSelected = { value, isMax ->
                                updateFilter(value, isMax, FilterType.NbBedrooms)
                            })
                        is Filter.EntryDateFilter -> SearchViewState.Date(min = filter.from?.toString() ?: "",
                            max = filter.to?.toString() ?: "",
                            title = context.getString(R.string.entry_date),
                            onValuesSelected = { year, month, day, isDateFrom ->
                                updateFilterDate(year, month, day, FilterType.EntryDate, isDateFrom)
                            })
                        is Filter.SaleDateFilter -> SearchViewState.Date(min = filter.from?.toString() ?: "",
                            max = filter.to?.toString() ?: "",
                            title = context.getString(R.string.sale_date),
                            onValuesSelected = { year, month, day, isDateFrom ->
                                updateFilterDate(year, month, day, FilterType.SaleDate, isDateFrom)
                            })
                    }
                })
            } else {
                filters.forEach { filter ->
                    when (filter) {
                        is Filter.LivingSpaceFilter -> searchViewStateList.add(
                            SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                                max = filter.max?.toString() ?: "",
                                title = context.getString(R.string.living_space_title),
                                onValuesSelected = { min, max ->
                                    updateFilter(min, max, FilterType.LivingSpace)
                                })
                        )
                        is Filter.PriceFilter -> searchViewStateList.add(
                            SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                                max = filter.max?.toString() ?: "",
                                title = context.getString(R.string.price_title),
                                onValuesSelected = { min, max ->
                                    updateFilter(min, max, FilterType.Price)
                                })
                        )
                        is Filter.NbRoomsFilter -> searchViewStateList.add(
                            SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                                max = filter.max?.toString() ?: "",
                                title = context.getString(R.string.nb_rooms_title),
                                onValuesSelected = { min, max ->
                                    updateFilter(min, max, FilterType.NbRooms)
                                })
                        )
                        is Filter.NbBedroomsFilter -> searchViewStateList.add(
                            SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                                max = filter.max?.toString() ?: "",
                                title = context.getString(R.string.nb_bedrooms_title),
                                onValuesSelected = { min, max ->
                                    updateFilter(min, max, FilterType.NbBedrooms)
                                })
                        )
                        is Filter.EntryDateFilter -> searchViewStateList.add(
                            SearchViewState.Date(min = filter.from?.toString() ?: "",
                                max = filter.to?.toString() ?: "",
                                title = context.getString(R.string.entry_date),
                                onValuesSelected = { year, month, day, isDateFrom ->
                                    updateFilterDate(year, month, day, FilterType.EntryDate, isDateFrom)
                                })
                        )
                        is Filter.SaleDateFilter -> searchViewStateList.add(
                            SearchViewState.Date(min = filter.from?.toString() ?: "",
                                max = filter.to?.toString() ?: "",
                                title = context.getString(R.string.sale_date),
                                onValuesSelected = { year, month, day, isDateFrom ->
                                    updateFilterDate(year, month, day, FilterType.SaleDate, isDateFrom)
                                })
                        )
                    }
                }

                if (!filters.any { it is Filter.LivingSpaceFilter }) {
                    searchViewStateList.add(
                        SearchViewState.Bounded(
                            min = "",
                            max = "",
                            title = context.getString(R.string.living_space_title),
                            onValuesSelected = { min, max ->
                                updateFilter(min, max, FilterType.LivingSpace)
                            })
                    )
                }

                if (!filters.any { it is Filter.PriceFilter }) {
                    searchViewStateList.add(
                        SearchViewState.Bounded(
                            min = "",
                            max = "",
                            title = context.getString(R.string.price_title),
                            onValuesSelected = { min, max ->
                                updateFilter(min, max, FilterType.Price)
                            })
                    )
                }

                if (!filters.any { it is Filter.NbRoomsFilter }) {
                    searchViewStateList.add(
                        SearchViewState.Bounded(
                            min = "",
                            max = "",
                            title = context.getString(R.string.nb_rooms_title),
                            onValuesSelected = { min, max ->
                                updateFilter(min, max, FilterType.NbRooms)
                            })
                    )
                }

                if (!filters.any { it is Filter.NbBedroomsFilter }) {
                    searchViewStateList.add(
                        SearchViewState.Bounded(
                            min = "",
                            max = "",
                            title = context.getString(R.string.nb_bedrooms_title),
                            onValuesSelected = { min, max ->
                                updateFilter(min, max, FilterType.NbBedrooms)
                            })
                    )
                }

                if (!filters.any { it is Filter.EntryDateFilter }) {
                    searchViewStateList.add(
                        SearchViewState.Date(min = "",
                            max = "",
                            title = context.getString(R.string.entry_date),
                            onValuesSelected = { year, month, day, isDateFrom ->
                                updateFilterDate(year, month, day, FilterType.EntryDate, isDateFrom)
                            })
                    )
                }

                if (!filters.any { it is Filter.SaleDateFilter }) {
                    searchViewStateList.add(
                        SearchViewState.Date(min = "",
                            max = "",
                            title = context.getString(R.string.sale_date),
                            onValuesSelected = { year, month, day, isDateFrom ->
                                updateFilterDate(year, month, day, FilterType.SaleDate, isDateFrom)
                            })
                    )
                }
            }
            emit(searchViewStateList)
        }
    }

    //region =============================================================== FILTERS ===============================================================

    fun onResetFiltersClicked() {
        searchRepository.reset()
    }

    private fun updateFilter(value: String?, isMax: Boolean, filterType: FilterType) {
        when (filterType) {
            FilterType.LivingSpace -> {
                val casted = value?.toDoubleOrNull()

                if (isMax) {
                    toggleFilterUseCase.invoke(FilterQuery.LivingSpaceFilter(min = FilterQuery.SearchParam.NoOp,
                        max = casted?.let { FilterQuery.SearchParam.Update(casted) } ?: FilterQuery.SearchParam.Delete))
                } else {
                    toggleFilterUseCase.invoke(FilterQuery.LivingSpaceFilter(min = casted?.let { FilterQuery.SearchParam.Update(casted) }
                        ?: FilterQuery.SearchParam.Delete, max = FilterQuery.SearchParam.NoOp))
                }
            }
            FilterType.Price -> {
                val casted = value?.toDoubleOrNull()

                if (isMax) {
                    toggleFilterUseCase.invoke(FilterQuery.PriceFilter(min = FilterQuery.SearchParam.NoOp,
                        max = casted?.let { FilterQuery.SearchParam.Update(casted) } ?: FilterQuery.SearchParam.Delete))
                } else {
                    toggleFilterUseCase.invoke(FilterQuery.PriceFilter(min = casted?.let { FilterQuery.SearchParam.Update(casted) }
                        ?: FilterQuery.SearchParam.Delete, max = FilterQuery.SearchParam.NoOp))
                }
            }
            FilterType.NbRooms -> {
                val casted = value?.toIntOrNull()

                if (isMax) {
                    toggleFilterUseCase.invoke(FilterQuery.NbRoomsFilter(min = FilterQuery.SearchParam.NoOp,
                        max = casted?.let { FilterQuery.SearchParam.Update(casted) } ?: FilterQuery.SearchParam.Delete))
                } else {
                    toggleFilterUseCase.invoke(FilterQuery.NbRoomsFilter(min = casted?.let { FilterQuery.SearchParam.Update(casted) }
                        ?: FilterQuery.SearchParam.Delete, max = FilterQuery.SearchParam.NoOp))
                }
            }
            FilterType.NbBedrooms -> {
                val casted = value?.toIntOrNull()

                if (isMax) {
                    toggleFilterUseCase.invoke(FilterQuery.NbBedroomsFilter(min = FilterQuery.SearchParam.NoOp,
                        max = casted?.let { FilterQuery.SearchParam.Update(casted) } ?: FilterQuery.SearchParam.Delete))
                } else {
                    toggleFilterUseCase.invoke(FilterQuery.NbBedroomsFilter(min = casted?.let { FilterQuery.SearchParam.Update(casted) }
                        ?: FilterQuery.SearchParam.Delete, max = FilterQuery.SearchParam.NoOp))
                }
            }
        }
    }

    private fun updateFilterDate(
        year: Int, month: Int, dayOfMonth: Int, filterType: FilterType, isDateFrom: Boolean
    ) {
        when (filterType) {
            FilterType.EntryDate -> {
                val entryDate = try {
                    LocalDateTime.of(year, month, dayOfMonth, 0, 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                if (isDateFrom) {
                    toggleFilterUseCase.invoke(
                        FilterQuery.EntryDateFilter(
                            from = if (entryDate != null) {
                                FilterQuery.SearchParam.Update(entryDate)
                            } else {
                                FilterQuery.SearchParam.Delete
                            }, to = FilterQuery.SearchParam.NoOp
                        )
                    )
                } else {
                    toggleFilterUseCase.invoke(
                        FilterQuery.EntryDateFilter(
                            from = FilterQuery.SearchParam.NoOp, to = if (entryDate != null) {
                                FilterQuery.SearchParam.Update(entryDate)
                            } else {
                                FilterQuery.SearchParam.Delete
                            }
                        )
                    )
                }
            }
            FilterType.SaleDate -> {
                val saleDate = try {
                    LocalDateTime.of(year, month, dayOfMonth, 0, 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                if (isDateFrom) {
                    toggleFilterUseCase.invoke(
                        FilterQuery.SaleDateFilter(
                            from = if (saleDate != null) {
                                FilterQuery.SearchParam.Update(saleDate)
                            } else {
                                FilterQuery.SearchParam.Delete
                            }, to = FilterQuery.SearchParam.NoOp
                        )
                    )
                } else {
                    toggleFilterUseCase.invoke(
                        FilterQuery.SaleDateFilter(
                            from = FilterQuery.SearchParam.NoOp,
                            to = if (saleDate != null) {
                                FilterQuery.SearchParam.Update(saleDate)
                            } else {
                                FilterQuery.SearchParam.Delete
                            }
                        )
                    )
                }
            }
        }
    }

    //endregion
}