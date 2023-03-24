package com.suonk.oc_project9.domain.filter.model

import java.time.LocalDateTime

sealed class FilterQuery {

    data class LivingSpaceFilter(val min: SearchParam<Double>, val max: SearchParam<Double>) : FilterQuery()
    data class PriceFilter(val min: SearchParam<Double>, val max: SearchParam<Double>) : FilterQuery()
    data class NbRoomsFilter(val min: SearchParam<Int>, val max: SearchParam<Int>) : FilterQuery()
    data class NbBedroomsFilter(val min: SearchParam<Int>, val max: SearchParam<Int>) : FilterQuery()

    data class EntryDateFilter(val from: SearchParam<LocalDateTime>, val to: SearchParam<LocalDateTime>) : FilterQuery()
    data class SaleDateFilter(val from: SearchParam<LocalDateTime>, val to: SearchParam<LocalDateTime>) : FilterQuery()

    sealed class SearchParam<out T> {
        data class Update<T>(val value: T) : SearchParam<T>()
        object NoOp : SearchParam<Nothing>()
        object Delete : SearchParam<Nothing>()
    }
}