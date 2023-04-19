package com.suonk.oc_project9.ui.real_estates

import junit.framework.TestCase.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized

//@RunWith(Parameterized::class)
class RealEstatesListViewModelParameterizedTest(
    private val entities: List<TotoEntities>,
    private val sortingType: SortingType?,
    private val filteringType: FilteringType?,
    private val expected: List<TotoEntities>,
) {
//    companion object {
//        @JvmStatic
//        @Parameterized.Parameters(
//            name = "{index}: " +
//                "Given sortingType {1}, " +
//                "filteringType {2}, " +
//                "Then entities should be {3}"
//        )
//        fun getValue() = listOf(
//            arrayOf(getDefaultEntities(), null, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.NAME_ASC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.NAME_DSC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.PRICE_ASC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.PRICE_DSC, null, getDefaultEntities()),
//
//            arrayOf(getDefaultEntities(), null, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.NAME_ASC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.NAME_DSC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.PRICE_ASC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.PRICE_DSC, null, getDefaultEntities()),
//
//            arrayOf(getDefaultEntities(), null, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.NAME_ASC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.NAME_DSC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.PRICE_ASC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.PRICE_DSC, null, getDefaultEntities()),
//
//            arrayOf(getDefaultEntities(), null, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.NAME_ASC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.NAME_DSC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.PRICE_ASC, null, getDefaultEntities()),
//            arrayOf(getDefaultEntities(), SortingType.PRICE_DSC, null, getDefaultEntities()),
//        )
//
//        private fun getDefaultEntities(): List<TotoEntities> = List(3) {
//            TotoEntities(
//                id = it,
//                name = ('A'.code + it).toChar() + "blabla",
//                price = it * 800
//            )
//        }
//    }

//    @Test
//    fun test() {
//        val result = MyUseCase().doStuff(entities, sortingType, filteringType)
//
//        assertEquals(expected, result)
//    }
}

class MyUseCase {
    fun doStuff(
        entities: List<TotoEntities>,
        sortingType: SortingType?,
        filteringType: FilteringType?,
    ): List<TotoEntities> {
        return entities.let {
            when (sortingType) {
                SortingType.NAME_ASC -> it.sortedBy { it.name }
                SortingType.NAME_DSC -> it.sortedByDescending { it.name }
                SortingType.PRICE_ASC -> it.sortedBy { it.price }
                SortingType.PRICE_DSC -> it.sortedByDescending { it.name }
                null -> it
            }
        }
    }
}

data class TotoEntities(
    val id: Int,
    val name: String,
    val price: Int,
)

enum class SortingType {
    NAME_ASC,
    NAME_DSC,
    PRICE_ASC,
    PRICE_DSC
}

enum class FilteringType {
    HIGHER_THAN_THOUSAND,
    NAME_STARTING_BY_A,
}