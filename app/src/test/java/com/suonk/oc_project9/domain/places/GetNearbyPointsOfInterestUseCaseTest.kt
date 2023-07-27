package com.suonk.oc_project9.domain.places

import com.suonk.oc_project9.model.database.data.entities.places.PointOfInterest
import com.suonk.oc_project9.model.database.data.repositories.PlacesRepositoryImpl
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.*
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test

class GetNearbyPointsOfInterestUseCaseTest {

    private companion object {
        private const val defaultPlaceId = "defaultPlaceId"
        private const val defaultName = "defaultName"
        private const val defaultAddress = "defaultAddress"

        private const val DEFAULT_LAT = 40.744081
        private const val DEFAULT_LONG = -73.991302
        private const val DEFAULT_LAT_EMPTY = 0.0
        private const val DEFAULT_LONG_EMPTY = 0.0

        private const val defaultLocation = "$DEFAULT_LAT,$DEFAULT_LONG"
        private const val defaultLocationEmpty = "$DEFAULT_LAT_EMPTY,$DEFAULT_LONG_EMPTY"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val placesRepository: PlacesRepositoryImpl = mockk()

    private val getNearbyPointsOfInterestUseCase = GetNearbyPointsOfInterestUseCase(placesRepository)

    @Test
    fun `test invoke`() = testCoroutineRule.runTest {
        // GIVEN
        coEvery { placesRepository.getNearbyPlaceResponse(defaultLocation) } returns defaultPointOfInterestList()

        // WHEN
        val listOfPointOfInterest = getNearbyPointsOfInterestUseCase.invoke(DEFAULT_LAT, DEFAULT_LONG)
        assertNotNull(listOfPointOfInterest)
        assertEquals(defaultPointOfInterestList(), listOfPointOfInterest)

        coVerify { placesRepository.getNearbyPlaceResponse(defaultLocation) }
        confirmVerified(placesRepository)
    }

    @Test
    fun `test invoke if get nearby response returns null`() = testCoroutineRule.runTest {
        // GIVEN
        coEvery { placesRepository.getNearbyPlaceResponse(defaultLocationEmpty) } returns null

        // WHEN
        val listOfPointOfInterest = getNearbyPointsOfInterestUseCase.invoke(DEFAULT_LAT_EMPTY, DEFAULT_LONG_EMPTY)
        assertNotNull(listOfPointOfInterest)
        assertEquals(defaultPointOfInterestEmptyList(), listOfPointOfInterest)

        coVerify { placesRepository.getNearbyPlaceResponse(defaultLocationEmpty) }
        confirmVerified(placesRepository)
    }

    private fun defaultPointOfInterestEmptyList(): List<PointOfInterest> {
        return emptyList()
    }

    private fun defaultPointOfInterestList(): List<PointOfInterest> {
        return listOf(defaultPointOfInterest(), defaultPointOfInterest2(), defaultPointOfInterest3())
    }

    private fun defaultPointOfInterest(): PointOfInterest {
        return PointOfInterest(
            defaultPlaceId,
            defaultName,
            defaultAddress,
            listOf(),
        )
    }

    private fun defaultPointOfInterest2(): PointOfInterest {
        return PointOfInterest(
            defaultPlaceId,
            defaultName,
            defaultAddress,
            listOf(),
        )
    }

    private fun defaultPointOfInterest3(): PointOfInterest {
        return PointOfInterest(
            defaultPlaceId,
            defaultName,
            defaultAddress,
            listOf(),
        )
    }
}