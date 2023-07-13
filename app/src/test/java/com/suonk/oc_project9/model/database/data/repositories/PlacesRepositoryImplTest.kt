package com.suonk.oc_project9.model.database.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.suonk.oc_project9.api.PlacesApiService
import com.suonk.oc_project9.model.database.data.entities.places.NearbyPlaceResponse
import com.suonk.oc_project9.model.database.data.entities.places.NearbyPlaceResult
import com.suonk.oc_project9.model.database.data.entities.places.PointOfInterest
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Assert
import org.junit.Rule
import org.junit.Test
import org.mockito.ArgumentMatchers.any

class PlacesRepositoryImplTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val apiService: PlacesApiService = mockk()

    private val placesRepositoryImpl = PlacesRepositoryImpl(apiService)


    private val defaultPlaceId = "defaultPlaceId"
    private val defaultName = "defaultName"
    private val defaultAddress = "defaultAddress"
    private val defaultIcon = "defaultIcon"
    private val defaultTypes = "defaultTypes"
    private val DEFAULT_LOCATION = ""

    @Test
    fun `test nearby places response`() = testCoroutineRule.runTest {
        // GIVEN
        coEvery { apiService.getNearbyPlacesResponse(DEFAULT_LOCATION) } returns defaultNearbyResponse()

        // WHEN
        val list = placesRepositoryImpl.getNearbyPlaceResponse(DEFAULT_LOCATION)
        assertNotNull(list)
        assertEquals(defaultPointOfInterestList(), list)

        coVerify { apiService.getNearbyPlacesResponse(DEFAULT_LOCATION) }
    }

    private fun defaultPointOfInterestList(): List<PointOfInterest> {
        return listOf(defaultPointOfInterest())
    }

    private fun defaultPointOfInterest(): PointOfInterest {
        return PointOfInterest(
            defaultPlaceId,
            defaultName,
            defaultAddress,
            defaultIcon,
            listOf(),
        )
    }



    private fun defaultNearbyResponse(): NearbyPlaceResponse {
        return NearbyPlaceResponse(any(), "", defaultNearbyPlaceResultsList(), any())
    }

    private fun defaultNearbyPlaceResultsList(): List<NearbyPlaceResult> {
        return listOf(defaultNearbyPlaceResult())
    }

    private fun defaultNearbyPlaceResult(): NearbyPlaceResult {
        return NearbyPlaceResult(
           business_status = any(),
           geometry = any(),
           icon = any(),
           icon_background_color = any(),
           icon_mask_base_uri = any(),
           name = defaultName,
           openingHours = any(),
           permanently_closed = any(),
           photos = listOf(),
           placeId = defaultPlaceId,
           plus_code = any(),
           price_level = any(),
           rating = any(),
           reference = any(),
           scope = any(),
           types = listOf(),
           user_ratings_total = any(),
           vicinity = defaultAddress,
        )
    }
}