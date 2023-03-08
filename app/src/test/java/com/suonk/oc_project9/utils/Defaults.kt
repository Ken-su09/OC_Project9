package com.suonk.oc_project9.utils

import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import com.suonk.oc_project9.ui.real_estates.details.DetailsPhotoViewState
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewModel
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewState

object Defaults {

    fun getAllDefaultRealEstatesWithPhotos(): List<RealEstateEntityWithPhotos> {
        return arrayListOf(
            getDefaultRealEstateEntityWithPhotos(), RealEstateEntityWithPhotos(
                RealEstateEntity(
                    id = 1L,
                    type = "Penthouse",
                    price = 29872000.0,
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
                    pointOfInterest = "",
                    status = "Available",
                    entryDate = System.currentTimeMillis(),
                    saleDate = null,
                    latitude = 40.744080,
                    longitude = -73.991302,
                    agentInChargeId = 1L,
                ), arrayListOf(
                    PhotoEntity(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/cfef7cd3d01074fe7ac38e6fdfd0c657-se_extra_large_1500_800.webp",
                        false
                    ), PhotoEntity(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/f6def35d74b83ffa18e93d5bd56c6390-se_extra_large_1500_800.webp",
                        false
                    )
                )
            ), RealEstateEntityWithPhotos(
                RealEstateEntity(
                    3L,
                    "Duplex",
                    15995000.0,
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
                    "",
                    "Available",
                    System.currentTimeMillis(),
                    null,
                    40.775070,
                    -73.806640,
                    2L
                ), arrayListOf(
                    PhotoEntity(
                        0, 3L, "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg", false
                    )
                )
            )
        )
    }

    fun getDefaultRealEstateDetailsViewState(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = 1L,
            type = "Penthouse",
            typePosition = 1,
            price = "29872000.0",
            livingSpace = "8072.900",
            numberRooms = "8",
            numberBedroom = "4",
            numberBathroom = "2",
            description = "Anchored by a vast marble gallery with sweeping staircase, ",
            arrayListOf(
                DetailsPhotoViewState(
                    false, "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"
                ), DetailsPhotoViewState(
                    false, "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"
                ), DetailsPhotoViewState(
                    false, "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
                )
            ),
            city = "New York",
            postalCode = "10010",
            state = "NY",
            streetName = "25th Street",
            gridZone = "55 West",
            latitude = 2.1,
            longitude = -7.8,
            noPhoto = false
        )
    }

    fun getDefaultEmptyRealEstateDetailsViewState(): RealEstateDetailsViewState {
        return RealEstateDetailsViewState(
            id = 0L,
            type = "",
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
            noPhoto = true
        )
    }

    fun getAllDefaultRealEstatesViewState(): List<RealEstateDetailsViewState> {
        return arrayListOf(
            getDefaultRealEstateDetailsViewState(), RealEstateDetailsViewState(
                id = 2L,
                type = "Loft",
                typePosition = 3,
                price = "29872000.0",
                livingSpace = "8072.900",
                numberRooms = "8",
                numberBedroom = "4",
                numberBathroom = "2",
                description = "Anchored by a vast marble gallery with sweeping staircase, ",
                photos = arrayListOf(
                    DetailsPhotoViewState(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/cfef7cd3d01074fe7ac38e6fdfd0c657-se_extra_large_1500_800.webp",
                        false
                    ), PhotoEntity(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/f6def35d74b83ffa18e93d5bd56c6390-se_extra_large_1500_800.webp",
                        false
                    )
                ),
                postalCode = "10010",
                state = "NY",
                city = "New York",
                streetName = "25th Street",
                gridZone = "55 West",
                latitude = 40.744080,
                longitude = -73.991302,
                noPhoto = false
            )
        )
    }

    fun getAllDefaultRealEstatesViewStateSortByAscPrice(): List<RealEstateDetailsViewState> {
        return arrayListOf(
            getDefaultRealEstateEntityWithPhotos(), RealEstateEntityWithPhotos(
                RealEstateEntity(
                    id = 1L,
                    type = "Penthouse",
                    price = 29872000.0,
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
                    pointOfInterest = "",
                    status = "Available",
                    entryDate = System.currentTimeMillis(),
                    saleDate = null,
                    latitude = 40.744080,
                    longitude = -73.991302,
                    agentInChargeId = 1L,
                ), arrayListOf(
                    PhotoEntity(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/cfef7cd3d01074fe7ac38e6fdfd0c657-se_extra_large_1500_800.webp",
                        false
                    ), PhotoEntity(
                        id = 0,
                        realEstateId = 2L,
                        "https://photos.zillowstatic.com/fp/f6def35d74b83ffa18e93d5bd56c6390-se_extra_large_1500_800.webp",
                        false
                    )
                )
            ), RealEstateEntityWithPhotos(
                RealEstateEntity(
                    3L,
                    "Duplex",
                    15995000.0,
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
                    "",
                    "Available",
                    System.currentTimeMillis(),
                    null,
                    40.775070,
                    -73.806640,
                    2L
                ), arrayListOf(
                    PhotoEntity(
                        0, 3L, "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg", false
                    )
                )
            )
        )
    }


    //region ======================================== REAL ESTATE 1 =========================================

    private fun getDefaultRealEstateEntityWithPhotos(): RealEstateEntityWithPhotos {
        return RealEstateEntityWithPhotos(getDefaultRealEstateEntity(), getDefaultPhotoEntities())
    }

    fun getDefaultRealEstateEntity(): RealEstateEntity {
        return RealEstateEntity(
            id = 1L,
            type = "Penthouse",
            price = 29872000.0,
            livingSpace = 8072.900,
            numberRooms = 8,
            numberBedroom = 4,
            numberBathroom = 2,
            description = "Anchored by a vast marble gallery with sweeping staircase, ",
            "10010",
            "NY",
            "New York",
            "25th Street",
            "55 West",
            "",
            "Available",
            System.currentTimeMillis(),
            null,
            40.744080,
            -73.991302,
            1L,
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
            DetailsPhotoViewState(
                uri = "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp",
                EquatableCallback {}),
            DetailsPhotoViewState(
                uri = "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp",
                EquatableCallback {}),
            DetailsPhotoViewState(
                uri = "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp",
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

    fun testPhotosList(): List<DetailsPhotoViewState> {
        return listOf(
            DetailsPhotoViewState("https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg",
                EquatableCallback {}),
            DetailsPhotoViewState("https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg",
                EquatableCallback {}),
            DetailsPhotoViewState("https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg",
                EquatableCallback {})
        )
    }

    //endregion
}


