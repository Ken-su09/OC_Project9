package com.suonk.oc_project9.ui.filter

sealed class SearchViewState(val type: SearchType) {

    abstract val min: String
    abstract val max: String
    abstract val title: String

    data class Bounded(
        override val min: String,
        override val max: String,
        override val title: String,
        val onValuesSelected: (String?, String?) -> Unit
    ) : SearchViewState(SearchType.BOUNDED)

    data class Date(
        override val min: String,
        override val max: String,
        override val title: String,
        val onValuesSelected: (Int, Int, Int, Int, Int, Int) -> Unit
    ) : SearchViewState(SearchType.DATE)

    enum class SearchType {
        BOUNDED, DATE
    }
}