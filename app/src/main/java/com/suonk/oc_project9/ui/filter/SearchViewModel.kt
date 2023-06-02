package com.suonk.oc_project9.ui.filter

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.domain.more_criteria.ToggleFilterUseCase
import com.suonk.oc_project9.domain.more_criteria.model.FilterQuery
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.SingleLiveEvent
import com.suonk.oc_project9.utils.filter.FilterType
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val toggleFilterUseCase: ToggleFilterUseCase,
    private val searchRepository: SearchRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val filtersFlow = MutableStateFlow(listOf<SearchFilterForm>())
    val finishSavingSingleLiveEvent = SingleLiveEvent<Unit>()

    private var previousMin = 0.0
    private var previousMax = 0.0

    val viewStateLiveData: LiveData<List<SearchViewState>> = liveData(coroutineDispatcherProvider.io) {
        searchRepository.getCurrentFilterParametersFlow().collect { filters: List<Filter> ->
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
                            onValuesSelected = { minValue, maxValue ->
                                updateFilter(minValue, maxValue, FilterType.LivingSpace)
                            })
                        is Filter.PriceFilter -> SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                            max = filter.max?.toString() ?: "",
                            title = context.getString(R.string.price_title),
                            onValuesSelected = { minValue, maxValue ->
                                updateFilter(minValue, maxValue, FilterType.Price)
                            })
                        is Filter.NbRoomsFilter -> SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                            max = filter.max?.toString() ?: "",
                            title = context.getString(R.string.nb_rooms_title),
                            onValuesSelected = { minValue, maxValue ->
                                updateFilter(minValue, maxValue, FilterType.NbRooms)
                            })
                        is Filter.NbBedroomsFilter -> SearchViewState.Bounded(min = filter.min?.toString() ?: "",
                            max = filter.max?.toString() ?: "",
                            title = context.getString(R.string.nb_bedrooms_title),
                            onValuesSelected = { minValue, maxValue ->
                                updateFilter(minValue, maxValue, FilterType.NbBedrooms)
                            })
                        is Filter.EntryDateFilter -> SearchViewState.Date(min = filter.from?.toString() ?: "",
                            max = filter.to?.toString() ?: "",
                            title = context.getString(R.string.entry_date),
                            onValuesSelected = { yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo ->
                                updateFilterDate(yearFrom, monthFrom, dayFrom, FilterType.EntryDate, yearTo, monthTo, dayTo)
                            })
                        is Filter.SaleDateFilter -> SearchViewState.Date(min = filter.from?.toString() ?: "",
                            max = filter.to?.toString() ?: "",
                            title = context.getString(R.string.sale_date),
                            onValuesSelected = { yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo ->
                                updateFilterDate(yearFrom, monthFrom, dayFrom, FilterType.SaleDate, yearTo, monthTo, dayTo)
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
                                onValuesSelected = { yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo ->
                                    updateFilterDate(yearFrom, monthFrom, dayFrom, FilterType.EntryDate, yearTo, monthTo, dayTo)
                                })
                        )
                        is Filter.SaleDateFilter -> searchViewStateList.add(
                            SearchViewState.Date(min = filter.from?.toString() ?: "",
                                max = filter.to?.toString() ?: "",
                                title = context.getString(R.string.sale_date),
                                onValuesSelected = { yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo ->
                                    updateFilterDate(yearFrom, monthFrom, dayFrom, FilterType.SaleDate, yearTo, monthTo, dayTo)
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
                            onValuesSelected = { yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo ->
                                updateFilterDate(yearFrom, monthFrom, dayFrom, FilterType.EntryDate, yearTo, monthTo, dayTo)
                            })
                    )
                }

                if (!filters.any { it is Filter.SaleDateFilter }) {
                    searchViewStateList.add(
                        SearchViewState.Date(min = "",
                            max = "",
                            title = context.getString(R.string.sale_date),
                            onValuesSelected = { yearFrom, monthFrom, dayFrom, yearTo, monthTo, dayTo ->
                                updateFilterDate(yearFrom, monthFrom, dayFrom, FilterType.SaleDate, yearTo, monthTo, dayTo)
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

    fun onValidateClicked(
        livingSpaceMin: String?,
        livingSpaceMax: String?,
        priceMin: String?,
        priceMax: String?,
        nbRoomsMin: String?,
        nbRoomsMax: String?,
        nbBedroomsMin: String?,
        nbBedroomsMax: String?,
    ) {
        val castedLivingSpaceMin = livingSpaceMin?.toDoubleOrNull()
        val castedLivingSpaceMax = livingSpaceMax?.toDoubleOrNull()

        toggleFilterUseCase.invoke(
            FilterQuery.LivingSpaceFilter(
                min = FilterQuery.SearchParam.Update(castedLivingSpaceMin), max = FilterQuery.SearchParam.Update(castedLivingSpaceMax)
            )
        )

        val castedPriceMin = if (priceMin?.isNotEmpty() == true) {
            BigDecimal(priceMin)
        } else {
            null
        }
        val castedPriceMax = if (priceMax?.isNotEmpty() == true) {
            BigDecimal(priceMax)
        } else {
            null
        }

        toggleFilterUseCase.invoke(
            FilterQuery.PriceFilter(
                min = FilterQuery.SearchParam.Update(castedPriceMin), max = FilterQuery.SearchParam.Update(castedPriceMax)
            )
        )

        val castedNbRoomsMin = nbRoomsMin?.toIntOrNull()
        val castedNbRoomsMax = nbRoomsMax?.toIntOrNull()

        toggleFilterUseCase.invoke(
            FilterQuery.NbRoomsFilter(
                min = FilterQuery.SearchParam.Update(castedNbRoomsMin), max = FilterQuery.SearchParam.Update(castedNbRoomsMax)
            )
        )

        val castedNbBedroomsMin = nbBedroomsMin?.toIntOrNull()
        val castedNbBedroomsMax = nbBedroomsMax?.toIntOrNull()

        toggleFilterUseCase.invoke(
            FilterQuery.NbBedroomsFilter(
                min = FilterQuery.SearchParam.Update(castedNbBedroomsMin), max = FilterQuery.SearchParam.Update(castedNbBedroomsMax)
            )
        )

//        CoroutineScope(coroutineDispatcherProvider.io).launch {
//            filtersFlow.collect {
//                it.forEach { filter ->
//                    Log.i("FilterList", "filter : $filter")
//
//                    when (filter.filterType) {
//                        FilterType.LivingSpace -> {
//                            val castedMin = (filter as SearchFilterForm.FilterLivingSpace).minValue
//                            val castedMax = filter.maxValue
//
//                            toggleFilterUseCase.invoke(
//                                FilterQuery.LivingSpaceFilter(
//                                    min = FilterQuery.SearchParam.Update(castedMin), max = FilterQuery.SearchParam.Update(castedMax)
//                                )
//                            )
//                        }
//                        FilterType.Price -> {
//                            val castedMin = (filter as SearchFilterForm.FilterPrice).minValue
//                            val castedMax = filter.maxValue
//
//                            toggleFilterUseCase.invoke(
//                                FilterQuery.PriceFilter(
//                                    min = FilterQuery.SearchParam.Update(castedMin), max = FilterQuery.SearchParam.Update(castedMax)
//                                )
//                            )
//                        }
//                        FilterType.NbRooms -> {
//                            val castedMin = (filter as SearchFilterForm.FilterNbRooms).minValue
//                            val castedMax = filter.maxValue
//
//                            toggleFilterUseCase.invoke(
//                                FilterQuery.NbRoomsFilter(
//                                    min = FilterQuery.SearchParam.Update(castedMin), max = FilterQuery.SearchParam.Update(castedMax)
//                                )
//                            )
//                        }
//                        FilterType.NbBedrooms -> {
//                            val castedMin = (filter as SearchFilterForm.FilterNbBedrooms).minValue
//                            val castedMax = filter.maxValue
//
//                            toggleFilterUseCase.invoke(
//                                FilterQuery.NbBedroomsFilter(
//                                    min = FilterQuery.SearchParam.Update(castedMin), max = FilterQuery.SearchParam.Update(castedMax)
//                                )
//                            )
//                        }
//
//                        FilterType.EntryDate -> {
//                            val yearFrom = (filter as SearchFilterForm.FilterEntryDate).yearFrom
//                            val monthFrom = filter.monthFrom
//                            val dayOfMonthFrom = filter.dayFrom
//                            val yearTo = filter.yearTo
//                            val monthTo = filter.monthTo
//                            val dayOfMonthTo = filter.dayTo
//
//                            val entryDateFrom = try {
//                                LocalDateTime.of(yearFrom, monthFrom, dayOfMonthFrom, 0, 0)
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                                null
//                            }
//                            val entryDateTo = try {
//                                LocalDateTime.of(yearTo, monthTo, dayOfMonthTo, 0, 0)
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                                null
//                            }
//
//                            toggleFilterUseCase.invoke(
//                                FilterQuery.EntryDateFilter(
//                                    from = if (entryDateFrom != null) {
//                                        FilterQuery.SearchParam.Update(entryDateFrom)
//                                    } else {
//                                        FilterQuery.SearchParam.Delete
//                                    }, to = if (entryDateTo != null) {
//                                        FilterQuery.SearchParam.Update(entryDateTo)
//                                    } else {
//                                        FilterQuery.SearchParam.Delete
//                                    }
//                                )
//                            )
//                        }
//                        FilterType.SaleDate -> {
//                            val yearFrom = (filter as SearchFilterForm.FilterEntryDate).yearFrom
//                            val monthFrom = filter.monthFrom
//                            val dayOfMonthFrom = filter.dayFrom
//                            val yearTo = filter.yearTo
//                            val monthTo = filter.monthTo
//                            val dayOfMonthTo = filter.dayTo
//
//                            val saleDateFrom = try {
//                                LocalDateTime.of(yearFrom, monthFrom, dayOfMonthFrom, 0, 0)
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                                null
//                            }
//                            val saleDateTo = try {
//                                LocalDateTime.of(yearTo, monthTo, dayOfMonthTo, 0, 0)
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                                null
//                            }
//
//                            toggleFilterUseCase.invoke(
//                                FilterQuery.EntryDateFilter(
//                                    from = if (saleDateFrom != null) {
//                                        FilterQuery.SearchParam.Update(saleDateFrom)
//                                    } else {
//                                        FilterQuery.SearchParam.Delete
//                                    }, to = if (saleDateTo != null) {
//                                        FilterQuery.SearchParam.Update(saleDateTo)
//                                    } else {
//                                        FilterQuery.SearchParam.Delete
//                                    }
//                                )
//                            )
//                        }
//                    }
//                }
//            }
//        }
        finishSavingSingleLiveEvent.setValue(Unit)
    }

    private fun updateFilter(min: String?, max: String?, filterType: FilterType) {
        Log.i("FilterList", "filterType : $filterType")
    }

//    private fun updateFilter(value: String?, isMax: Boolean) {
//        when (filterType) {
//            FilterType.LivingSpace -> {
//                val casted = value?.toDoubleOrNull()
//
//                if (isMax) {
//                    filters + SearchViewState.Bounded()
//
//                    toggleFilterUseCase.invoke(FilterQuery.LivingSpaceFilter(min = FilterQuery.SearchParam.NoOp,
//                        max = casted?.let { FilterQuery.SearchParam.Update(casted) } ?: FilterQuery.SearchParam.Delete))
//                } else {
//                    toggleFilterUseCase.invoke(FilterQuery.LivingSpaceFilter(min = casted?.let { FilterQuery.SearchParam.Update(casted) }
//                        ?: FilterQuery.SearchParam.Delete, max = FilterQuery.SearchParam.NoOp))
//                }
//            }
//            FilterType.Price -> {
//                val casted = value?.toDoubleOrNull()
//
//                if (isMax) {
//                    toggleFilterUseCase.invoke(FilterQuery.PriceFilter(min = FilterQuery.SearchParam.NoOp,
//                        max = casted?.let { FilterQuery.SearchParam.Update(casted) } ?: FilterQuery.SearchParam.Delete))
//                } else {
//                    toggleFilterUseCase.invoke(FilterQuery.PriceFilter(min = casted?.let { FilterQuery.SearchParam.Update(casted) }
//                        ?: FilterQuery.SearchParam.Delete, max = FilterQuery.SearchParam.NoOp))
//                }
//            }
//            FilterType.NbRooms -> {
//                val casted = value?.toIntOrNull()
//
//                if (isMax) {
//                    toggleFilterUseCase.invoke(FilterQuery.NbRoomsFilter(min = FilterQuery.SearchParam.NoOp,
//                        max = casted?.let { FilterQuery.SearchParam.Update(casted) } ?: FilterQuery.SearchParam.Delete))
//                } else {
//                    toggleFilterUseCase.invoke(FilterQuery.NbRoomsFilter(min = casted?.let { FilterQuery.SearchParam.Update(casted) }
//                        ?: FilterQuery.SearchParam.Delete, max = FilterQuery.SearchParam.NoOp))
//                }
//            }
//            FilterType.NbBedrooms -> {
//                val casted = value?.toIntOrNull()
//
//                if (isMax) {
//                    toggleFilterUseCase.invoke(FilterQuery.NbBedroomsFilter(min = FilterQuery.SearchParam.NoOp,
//                        max = casted?.let { FilterQuery.SearchParam.Update(casted) } ?: FilterQuery.SearchParam.Delete))
//                } else {
//                    toggleFilterUseCase.invoke(FilterQuery.NbBedroomsFilter(min = casted?.let { FilterQuery.SearchParam.Update(casted) }
//                        ?: FilterQuery.SearchParam.Delete, max = FilterQuery.SearchParam.NoOp))
//                }
//            }
//        }
//    }

    private fun updateFilterDate(
        yearFrom: Int, monthFrom: Int, dayOfMonthFrom: Int, filterType: FilterType, yearTo: Int, monthTo: Int, dayOfMonthTo: Int
    ) {
        // TODO
        when (filterType) {
            FilterType.EntryDate -> {
//                filtersFlow + SearchFilterForm.FilterEntryDate(yearFrom, monthFrom, dayOfMonthFrom, yearTo, monthTo, dayOfMonthTo)

                val entryDateFrom = try {
                    LocalDateTime.of(yearFrom, monthFrom, dayOfMonthFrom, 0, 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }

                val entryDateTo = try {
                    LocalDateTime.of(yearTo, monthTo, dayOfMonthTo, 0, 0)
                } catch (e: Exception) {
                    e.printStackTrace()
                    null
                }
//                if (isDateFrom) {
//                    toggleFilterUseCase.invoke(
//                        FilterQuery.EntryDateFilter(
//                            from = if (entryDate != null) {
//                                FilterQuery.SearchParam.Update(entryDate)
//                            } else {
//                                FilterQuery.SearchParam.Delete
//                            }, to = FilterQuery.SearchParam.NoOp
//                        )
//                    )
//                } else {
//                    toggleFilterUseCase.invoke(
//                        FilterQuery.EntryDateFilter(
//                            from = FilterQuery.SearchParam.NoOp, to = if (entryDate != null) {
//                                FilterQuery.SearchParam.Update(entryDate)
//                            } else {
//                                FilterQuery.SearchParam.Delete
//                            }
//                        )
//                    )
//                }
            }
            FilterType.SaleDate -> {
//                filtersFlow + SearchFilterForm.FilterSaleDate(yearFrom, monthFrom, dayOfMonthFrom, yearTo, monthTo, dayOfMonthTo)

//                val saleDate = try {
//                    LocalDateTime.of(year, month, dayOfMonth, 0, 0)
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                    null
//                }
//
//                if (isDateFrom) {
//                    toggleFilterUseCase.invoke(
//                        FilterQuery.SaleDateFilter(
//                            from = if (saleDate != null) {
//                                FilterQuery.SearchParam.Update(saleDate)
//                            } else {
//                                FilterQuery.SearchParam.Delete
//                            }, to = FilterQuery.SearchParam.NoOp
//                        )
//                    )
//                } else {
//                    toggleFilterUseCase.invoke(
//                        FilterQuery.SaleDateFilter(
//                            from = FilterQuery.SearchParam.NoOp, to = if (saleDate != null) {
//                                FilterQuery.SearchParam.Update(saleDate)
//                            } else {
//                                FilterQuery.SearchParam.Delete
//                            }
//                        )
//                    )
//                }
            }
        }
    }

//endregion
}