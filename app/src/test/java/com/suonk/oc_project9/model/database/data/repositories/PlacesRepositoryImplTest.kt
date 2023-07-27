package com.suonk.oc_project9.model.database.data.repositories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.suonk.oc_project9.api.PlacesApiService
import com.suonk.oc_project9.model.database.data.entities.places.*
import com.suonk.oc_project9.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import org.junit.Rule
import org.junit.Test

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
    private val defaultIconEmpty = ""
    private val defaultIconUrl1 = "defaultIconUrl1"
    private val defaultIconUrl2 = "defaultIconUrl2"
    private val defaultIconUrl3 = "defaultIconUrl3"
    private val defaultTypes = "defaultTypes"
    private val DEFAULT_LOCATION = ""

    @Test
    fun `test nearby places response with photos`() = testCoroutineRule.runTest {
        // GIVEN
        coEvery { apiService.getNearbyPlacesResponse(DEFAULT_LOCATION) } returns defaultNearbyResponse()

        // WHEN
        val list = placesRepositoryImpl.getNearbyPlaceResponse(DEFAULT_LOCATION)
        assertNotNull(list)
        assertEquals(defaultPointOfInterestList(), list)

        coVerify { apiService.getNearbyPlacesResponse(DEFAULT_LOCATION) }
    }

    @Test
    fun `test nearby places response without photos`() = testCoroutineRule.runTest {
        // GIVEN
        coEvery { apiService.getNearbyPlacesResponse(DEFAULT_LOCATION) } returns defaultNearbyResponseWithoutPhotos()

        // WHEN
        val list = placesRepositoryImpl.getNearbyPlaceResponse(DEFAULT_LOCATION)
        assertNotNull(list)
        assertEquals(defaultPointOfInterestListWithoutPhotos(), list)

        coVerify { apiService.getNearbyPlacesResponse(DEFAULT_LOCATION) }
    }

    @Test
    fun `test nearby places response with id and photo null`() = testCoroutineRule.runTest {
        // GIVEN
        coEvery { apiService.getNearbyPlacesResponse(DEFAULT_LOCATION) } returns defaultNearbyResponseWithIdNull()

        // WHEN
        val list = placesRepositoryImpl.getNearbyPlaceResponse(DEFAULT_LOCATION)
        assertNotNull(list)
        assertEquals(defaultPointOfInterestListWithIdNull(), list)

        coVerify { apiService.getNearbyPlacesResponse(DEFAULT_LOCATION) }
    }

    // With Photos

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

    private fun defaultNearbyResponse(): NearbyPlaceResponse {
        return NearbyPlaceResponse(listOf(), "", defaultNearbyPlaceResultsList(), "")
    }

    private fun defaultNearbyPlaceResultsList(): List<NearbyPlaceResult> {
        return listOf(defaultNearbyPlaceResult(), defaultNearbyPlaceResult2(), defaultNearbyPlaceResult3())
    }

    private fun defaultNearbyPlaceResult(): NearbyPlaceResult {
        return NearbyPlaceResult(
            business_status = "",
            geometry = Geometry(getDefaultPositionEmpty(), Viewport(null, null)),
            icon = "",
            icon_background_color = "",
            icon_mask_base_uri = "",
            name = defaultName,
            openingHours = OpeningHours(false),
            permanently_closed = false,
            photos = listOf(Photo(1, listOf(), defaultIconUrl1, 1)),
            placeId = defaultPlaceId,
            plus_code = PlusCode("", ""),
            price_level = 4,
            rating = 4.0,
            reference = "",
            scope = "",
            types = listOf(),
            user_ratings_total = 140,
            vicinity = defaultAddress,
        )
    }

    private fun defaultNearbyPlaceResult2(): NearbyPlaceResult {
        return NearbyPlaceResult(
            business_status = "",
            geometry = Geometry(getDefaultPositionEmpty(), Viewport(null, null)),
            icon = "",
            icon_background_color = "",
            icon_mask_base_uri = "",
            name = defaultName,
            openingHours = OpeningHours(false),
            permanently_closed = false,
            photos = listOf(Photo(1, listOf(), defaultIconUrl2, 1)),
            placeId = defaultPlaceId,
            plus_code = PlusCode("", ""),
            price_level = 4,
            rating = 4.0,
            reference = "",
            scope = "",
            types = listOf(),
            user_ratings_total = 140,
            vicinity = defaultAddress,
        )
    }

    private fun defaultNearbyPlaceResult3(): NearbyPlaceResult {
        return NearbyPlaceResult(
            business_status = "",
            geometry = Geometry(getDefaultPositionEmpty(), Viewport(null, null)),
            icon = "",
            icon_background_color = "",
            icon_mask_base_uri = "",
            name = defaultName,
            openingHours = OpeningHours(false),
            permanently_closed = false,
            photos = listOf(Photo(1, listOf(), defaultIconUrl3, 1)),
            placeId = defaultPlaceId,
            plus_code = PlusCode("", ""),
            price_level = 4,
            rating = 4.0,
            reference = "",
            scope = "",
            types = listOf(),
            user_ratings_total = 140,
            vicinity = defaultAddress,
        )
    }

    // Without Photos

    private fun defaultPointOfInterestListWithoutPhotos(): List<PointOfInterest> {
        return listOf(defaultPointOfInterestWithoutPhotos())
    }

    private fun defaultPointOfInterestWithoutPhotos(): PointOfInterest {
        return PointOfInterest(
            defaultPlaceId,
            defaultName,
            defaultAddress,
            listOf(),
        )
    }

    private fun defaultNearbyResponseWithoutPhotos(): NearbyPlaceResponse {
        return NearbyPlaceResponse(listOf(), "", defaultNearbyPlaceResultsListWithoutPhotos(), "")
    }

    private fun defaultNearbyPlaceResultsListWithoutPhotos(): List<NearbyPlaceResult> {
        return listOf(defaultNearbyPlaceResultWithoutPhotos())
    }

    private fun defaultNearbyPlaceResultWithoutPhotos(): NearbyPlaceResult {
        return NearbyPlaceResult(
            business_status = "",
            geometry = Geometry(getDefaultPositionEmpty(), Viewport(null, null)),
            icon = "",
            icon_background_color = "",
            icon_mask_base_uri = "",
            name = defaultName,
            openingHours = OpeningHours(false),
            permanently_closed = false,
            photos = listOf(),
            placeId = defaultPlaceId,
            plus_code = PlusCode("", ""),
            price_level = 4,
            rating = 4.0,
            reference = "",
            scope = "",
            types = listOf(),
            user_ratings_total = 140,
            vicinity = defaultAddress,
        )
    }

    private fun getDefaultPositionEmpty(): Position {
        return Position(0.0, 0.0)
    }

    // With ID and Photo null

    private fun defaultPointOfInterestListWithIdNull(): List<PointOfInterest> {
        return listOf(defaultPointOfInterestWithIdNull())
    }

    private fun defaultPointOfInterestWithIdNull(): PointOfInterest {
        return PointOfInterest(
            defaultName,
            defaultName,
            defaultAddress,
            listOf(),
        )
    }

    private fun defaultNearbyResponseWithIdNull(): NearbyPlaceResponse {
        return NearbyPlaceResponse(listOf(), "", defaultNearbyPlaceResultsListWithIdNull(), "")
    }

    private fun defaultNearbyPlaceResultsListWithIdNull(): List<NearbyPlaceResult> {
        return listOf(defaultNearbyPlaceResultWithIdNull())
    }

    private fun defaultNearbyPlaceResultWithIdNull(): NearbyPlaceResult {
        return NearbyPlaceResult(
            business_status = "",
            geometry = Geometry(getDefaultPositionEmpty(), Viewport(null, null)),
            icon = "",
            icon_background_color = "",
            icon_mask_base_uri = "",
            name = defaultName,
            openingHours = OpeningHours(false),
            permanently_closed = false,
            photos = null,
            placeId = null,
            plus_code = PlusCode("", ""),
            price_level = 4,
            rating = 4.0,
            reference = "",
            scope = "",
            types = listOf(),
            user_ratings_total = 140,
            vicinity = defaultAddress,
        )
    }
}