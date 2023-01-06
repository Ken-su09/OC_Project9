package com.suonk.oc_project9.utils.sort

sealed class SortField(val sortType: SortType) {

    class Date(sortType: SortType) : SortField(sortType)
    class Price(sortType: SortType) : SortField(sortType)
    class LivingSpace(sortType: SortType) : SortField(sortType)
    class RoomsNumber(sortType: SortType) : SortField(sortType)
    class BedroomsNumber(sortType: SortType) : SortField(sortType)
    class BathroomsNumber(sortType: SortType) : SortField(sortType)

    override fun toString(): String {
        return "SortField(sortType=$sortType)"
    }
}