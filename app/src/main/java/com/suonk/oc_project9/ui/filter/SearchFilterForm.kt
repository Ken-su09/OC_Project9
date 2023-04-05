package com.suonk.oc_project9.ui.filter

import com.suonk.oc_project9.utils.filter.FilterType
import java.math.BigDecimal

sealed class SearchFilterForm(val filterType: FilterType) {

    data class FilterLivingSpace(val minValue: Double, val maxValue: Double) : SearchFilterForm(filterType = FilterType.LivingSpace)
    data class FilterPrice(val minValue: BigDecimal, val maxValue: BigDecimal) : SearchFilterForm(filterType = FilterType.Price)

    data class FilterNbRooms(val minValue: Int, val maxValue: Int) : SearchFilterForm(filterType = FilterType.NbRooms)
    data class FilterNbBedrooms(val minValue: Int, val maxValue: Int) : SearchFilterForm(filterType = FilterType.NbBedrooms)

    data class FilterEntryDate(
        val yearFrom: Int, val monthFrom: Int, val dayFrom: Int,
        val yearTo: Int, val monthTo: Int, val dayTo: Int,
    ) : SearchFilterForm(filterType = FilterType.EntryDate)

    data class FilterSaleDate(
        val yearFrom: Int, val monthFrom: Int, val dayFrom: Int,
        val yearTo: Int, val monthTo: Int, val dayTo: Int,
    ) : SearchFilterForm(filterType = FilterType.SaleDate)
}