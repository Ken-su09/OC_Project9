package com.suonk.oc_project9.utils

import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import com.suonk.oc_project9.ui.real_estates.carousel.PhotoViewState
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
                    "55 West 25th Street, New York, NY, 10010",
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
                ), arrayListOf(
                    PhotoEntity(
                        0,
                        2L,
                        "https://photos.zillowstatic.com/fp/cfef7cd3d01074fe7ac38e6fdfd0c657-se_extra_large_1500_800.webp",
                        false
                    ), PhotoEntity(
                        0,
                        2L,
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
                    "156-0-156-34 25th Ave, Whitestone, NY 11357",
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
                        0,
                        3L,
                        "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg",
                        false
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
                PhotoViewState(
                    false,
                    "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"
                ), PhotoViewState(
                    false,
                    "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"
                ), PhotoViewState(
                    false,
                    "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
                )
            ),
            city = "10010",
            postalCode = "NY",
            state = "New York",
            streetName = "25th Street",
            gridZone = "55 West",
            latitude = 2.1,
            longitude = -7.8,
            noPhoto = false
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
            "55 West 25th Street, New York, NY, 10010",
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
        return arrayListOf(
            PhotoEntity(
                0,
                1L,
                "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp",
                false
            ), PhotoEntity(
                0,
                1L,
                "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp",
                false
            ), PhotoEntity(
                0,
                1L,
                "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp",
                false
            )
        )
    }

    fun getDefaultPhotoViewStates(): List<PhotoViewState> {
        return arrayListOf(
            PhotoViewState(
                false,
                "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"

            ), PhotoViewState(
                false,
                "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"

            ), PhotoViewState(
                false,
                "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"

            )
        )
    }

    fun testPhotosList(): List<PhotoViewState> {
        return arrayListOf(
            PhotoViewState(
                false,
                "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg",

                ), PhotoViewState(
                false,
                "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg",
            ), PhotoViewState(
                false,
                "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg",
            )
        )
    }

    //endregion
}


