package com.suonk.oc_project9.utils

import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import com.suonk.oc_project9.ui.real_estates.details.DetailsPhotoViewState
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModel
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_CITY
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_DESCRIPTION
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_GRID_ZONE
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_ID
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_LAT
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_LIVING_SPACE
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_LIVING_SPACE_STRING
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_LONG
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_NUMBER_BATHROOM
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_NUMBER_BATHROOM_STRING
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_NUMBER_BEDROOM
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_NUMBER_BEDROOM_STRING
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_NUMBER_ROOM
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_NUMBER_ROOM_STRING
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_POSTAL_CODE
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_PRICE
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_PRICE_STRING
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_STATE
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_STATUS_AVAILABLE
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_STREET_NAME
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_TIMESTAMP_LONG
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_TYPE
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModelTest.Companion.DEFAULT_TYPE_POSITION
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewState
import java.math.BigDecimal
import java.time.*

object Defaults {

    //region ================================================================ DETAILS ===============================================================

    //region ================================================================= EMPTY ================================================================

    fun getDefaultEmptyRealEstateDetailsViewState(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = getDefaultEmptyRealEstateEntity().id,
            type = getDefaultEmptyRealEstateEntity().type,
            typePosition = 0,
            price = "0.0",
            livingSpace = "0.0",
            numberRooms = "0",
            numberBedroom = "0",
            numberBathroom = "0",
            description = "",
            arrayListOf(),
            city = "",
            postalCode = "",
            state = "",
            streetName = "",
            gridZone = "",
            latitude = 0.0,
            longitude = 0.0,
            noPhoto = true,
            entryDate = null,
            saleDate = null,
            isSold = false,
            pointsOfInterest = emptyList(),
        )
    }

    private fun getDefaultEmptyRealEstateEntity(): RealEstateEntity {
        val fixedClock = Clock.fixed(Instant.EPOCH.plusMillis(DEFAULT_TIMESTAMP_LONG), ZoneId.systemDefault())

        return RealEstateEntity(
            id = 0L,
            type = "",
            price = BigDecimal(0.0),
            livingSpace = 0.0,
            numberRooms = 0,
            numberBedroom = 0,
            numberBathroom = 0,
            description = "",
            "",
            "",
            "",
            "",
            "",
            "Available",
            LocalDateTime.now(fixedClock),
            null,
            0.0,
            0.0,
            1L,
        )
    }

    //endregion

     fun getAllDefaultRealEstatesWithPhotos(): List<RealEstateEntityWithPhotos> {
        return arrayListOf(
            getDefaultRealEstateEntityWithPhotos(), RealEstateEntityWithPhotos(
                RealEstateEntity(
                    id = 1L,
                    type = "Penthouse",
                    price = BigDecimal(29872000.0),
                    livingSpace = 8072.900,
                    numberRooms = 8,
                    numberBedroom = 4,
                    numberBathroom = 2,
                    description = "Anchored by a vast marble gallery with sweeping staircase, ",
                    postalCode = "10010",
                    state = "NY",
                    city = "New York",
                    streetName = "25th Street",
                    gridZone = "55 West",
                    status = "Available",
                    entryDate = LocalDateTime.now(),
                    saleDate = null,
                    latitude = 40.744080,
                    longitude = -73.991302,
                    agentInChargeId = 1L,
                ), arrayListOf(
                    PhotoEntity(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/cfef7cd3d01074fe7ac38e6fdfd0c657-se_extra_large_1500_800.webp"
                    ), PhotoEntity(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/f6def35d74b83ffa18e93d5bd56c6390-se_extra_large_1500_800.webp"
                    )
                )
            ), RealEstateEntityWithPhotos(
                RealEstateEntity(
                    3L,
                    "Duplex",
                    BigDecimal(15995000.0),
                    11756.9652,
                    11,
                    3,
                    3,
                    "",
                    "11357",
                    "NY",
                    "Whitestone",
                    "25th Ave",
                    "156-0-156-34",
                    "Available",
                    LocalDateTime.now(),
                    null,
                    40.775070,
                    -73.806640,
                    2L
                ), arrayListOf(
                    PhotoEntity(
                        0, 3L, "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg"
                    )
                )
            )
        )
    }

    private fun getDefaultRealEstateDetailsViewState(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = DEFAULT_ID,
            type = DEFAULT_TYPE,
            typePosition = DEFAULT_TYPE_POSITION,
            price = DEFAULT_PRICE_STRING,
            livingSpace = DEFAULT_LIVING_SPACE_STRING,
            numberRooms = DEFAULT_NUMBER_ROOM_STRING,
            numberBedroom = DEFAULT_NUMBER_BEDROOM_STRING,
            numberBathroom = DEFAULT_NUMBER_BATHROOM_STRING,
            description = DEFAULT_DESCRIPTION,
            arrayListOf(
                DetailsPhotoViewState(
                    "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp",
                    EquatableCallback {}),
                DetailsPhotoViewState("https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp",
                    EquatableCallback {}),
                DetailsPhotoViewState("https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp",
                    EquatableCallback {})
            ),
            city = DEFAULT_CITY,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            noPhoto = false,
            entryDate = Instant.now(
                Clock.fixed(
                    Instant.ofEpochSecond(DEFAULT_TIMESTAMP_LONG),
                    ZoneOffset.UTC
                )
            ),
            saleDate = null,
            isSold = false,
            pointsOfInterest = arrayListOf(),
        )
    }

    private fun getDefaultRealEstateEntityWithPhotos(): RealEstateEntityWithPhotos {
        return RealEstateEntityWithPhotos(getDefaultRealEstateEntity(), getDefaultPhotoEntities())
    }

    fun getDefaultRealEstateEntity(): RealEstateEntity {
        return RealEstateEntity(
            id = DEFAULT_ID,
            type = DEFAULT_TYPE,
            price = DEFAULT_PRICE,
            livingSpace = DEFAULT_LIVING_SPACE,
            numberRooms = DEFAULT_NUMBER_ROOM,
            numberBedroom = DEFAULT_NUMBER_BEDROOM,
            numberBathroom = DEFAULT_NUMBER_BATHROOM,
            description = DEFAULT_DESCRIPTION,
            postalCode = DEFAULT_POSTAL_CODE,
            state = DEFAULT_STATE,
            city = DEFAULT_CITY,
            streetName = DEFAULT_STREET_NAME,
            gridZone = DEFAULT_GRID_ZONE,
            status = DEFAULT_STATUS_AVAILABLE,
            entryDate = LocalDateTime.now(),
            saleDate = null,
            latitude = DEFAULT_LAT,
            longitude = DEFAULT_LONG,
            agentInChargeId = 1L,
        )
    }

    private fun getDefaultPhotoEntities(): List<PhotoEntity> {
        return listOf(
            PhotoEntity(
                0, 1L, "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"
            ), PhotoEntity(
                0, 1L, "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"
            ), PhotoEntity(
                0, 1L, "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
            )
        )
    }

    fun getDefaultPhotoViewStates(): List<DetailsPhotoViewState> {
        return listOf(
            DetailsPhotoViewState(uri = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp",
                EquatableCallback {}),
            DetailsPhotoViewState(uri = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp",
                EquatableCallback {}),
            DetailsPhotoViewState(uri = "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp",
                EquatableCallback {})
        )
    }

    fun getDefaultAggregatedPhotos(): List<RealEstateDetailsViewModel.AggregatedPhoto> {
        return listOf(
            RealEstateDetailsViewModel.AggregatedPhoto(
                uri = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"

            ), RealEstateDetailsViewModel.AggregatedPhoto(
                uri = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"

            ), RealEstateDetailsViewModel.AggregatedPhoto(
                uri = "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
            )
        )
    }

    //endregion

//    fun getAllDefaultRealEstatesViewState(): List<RealEstateDetailsViewState> {
//        return arrayListOf(
//            getDefaultRealEstateDetailsViewState(), RealEstateDetailsViewState(
//                id = 2L,
//                type = "Loft",
//                typePosition = 3,
//                price = "29872000.0",
//                livingSpace = "8072.900",
//                numberRooms = "8",
//                numberBedroom = "4",
//                numberBathroom = "2",
//                description = "Anchored by a vast marble gallery with sweeping staircase, ",
//                photos = arrayListOf(
//                    DetailsPhotoViewState(
//                        id = 0,
//                        realEstateId = 2L,
//                        "https://photos.zillowstatic.com/fp/cfef7cd3d01074fe7ac38e6fdfd0c657-se_extra_large_1500_800.webp",
//                        false
//                    ), PhotoEntity(
//                        id = 0,
//                        realEstateId = 2L,
//                        "https://photos.zillowstatic.com/fp/f6def35d74b83ffa18e93d5bd56c6390-se_extra_large_1500_800.webp",
//                        false
//                    )
//                ),
//                postalCode = "10010",
//                state = "NY",
//                city = "New York",
//                streetName = "25th Street",
//                gridZone = "55 West",
//                latitude = 40.744080,
//                longitude = -73.991302,
//                noPhoto = false
//            )
//        )
//    }
//
//    fun getAllDefaultRealEstatesViewStateSortByAscPrice(): List<RealEstateDetailsViewState> {
//        return arrayListOf(
//            getDefaultRealEstateEntityWithPhotos(), RealEstateEntityWithPhotos(
//                RealEstateEntity(
//                    id = 1L,
//                    type = "Penthouse",
//                    price = 29872000.0,
//                    livingSpace = 8072.900,
//                    numberRooms = 8,
//                    numberBedroom = 4,
//                    numberBathroom = 2,
//                    description = "Anchored by a vast marble gallery with sweeping staircase, ",
//                    postalCode = "10010",
//                    state = "NY",
//                    city = "New York",
//                    streetName = "25th Street",
//                    gridZone = "55 West",
//                    pointOfInterest = "",
//                    status = "Available",
//                    entryDate = System.currentTimeMillis(),
//                    saleDate = null,
//                    latitude = 40.744080,
//                    longitude = -73.991302,
//                    agentInChargeId = 1L,
//                ), arrayListOf(
//                    PhotoEntity(
//                        id = 0,
//                        realEstateId = 2L,
//                        "https://photos.zillowstatic.com/fp/cfef7cd3d01074fe7ac38e6fdfd0c657-se_extra_large_1500_800.webp",
//                        false
//                    ), PhotoEntity(
//                        id = 0,
//                        realEstateId = 2L,
//                        "https://photos.zillowstatic.com/fp/f6def35d74b83ffa18e93d5bd56c6390-se_extra_large_1500_800.webp",
//                        false
//                    )
//                )
//            ), RealEstateEntityWithPhotos(
//                RealEstateEntity(
//                    3L,
//                    "Duplex",
//                    15995000.0,
//                    11756.9652,
//                    11,
//                    3,
//                    3,
//                    "",
//                    "11357",
//                    "NY",
//                    "Whitestone",
//                    "25th Ave",
//                    "156-0-156-34",
//                    "",
//                    "Available",
//                    System.currentTimeMillis(),
//                    null,
//                    40.775070,
//                    -73.806640,
//                    2L
//                ), arrayListOf(
//                    PhotoEntity(
//                        0, 3L, "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg", false
//                    )
//                )
//            )
//        )
//    }
}


