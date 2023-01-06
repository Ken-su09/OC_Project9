package com.suonk.oc_project9.utils.sort

sealed class SortType {

//    abstract fun invoke(realEstates: List<RealEstateEntityWithPhotos>): List<RealEstateEntityWithPhotos>

    object Asc : SortType()
    object Desc : SortType()



//    object Asc : SortType() {
//        override fun invoke(realEstates: List<RealEstateEntityWithPhotos>) : List<RealEstateEntityWithPhotos> =
//            realEstates.sortedBy { it.realEstateEntity.entryDate }
//    }

//    object Desc : SortType()

    /*
    when (sortField.sortType) {
                is SortType.Asc -> {
                    when (sortField) {
                        is SortField.Date -> {
                            realEstates.sortedBy { it.realEstateEntity.entryDate }
                        }
                        is SortField.Price -> {
                            realEstates.sortedBy { it.realEstateEntity.price }
                        }
                        is SortField.LivingSpace -> {
                            realEstates.sortedBy { it.realEstateEntity.livingSpace }
                        }
                        is SortField.RoomsNumber -> {
                            realEstates.sortedBy { it.realEstateEntity.numberRooms }
                        }
                        is SortField.BedroomsNumber -> {
                            realEstates.sortedBy { it.realEstateEntity.numberBedroom }
                        }
                        is SortField.BathroomsNumber -> {
                            realEstates.sortedBy { it.realEstateEntity.numberBathroom }
                        }
                    }
                }
                is SortType.Desc -> {
                    when (sortField) {
                        is SortField.Date -> {
                            realEstates.sortedByDescending { it.realEstateEntity.entryDate }
                        }
                        is SortField.Price -> {
                            realEstates.sortedByDescending { it.realEstateEntity.price }
                        }
                        is SortField.LivingSpace -> {
                            realEstates.sortedByDescending { it.realEstateEntity.livingSpace }
                        }
                        is SortField.RoomsNumber -> {
                            realEstates.sortedByDescending { it.realEstateEntity.numberRooms }
                        }
                        is SortField.BedroomsNumber -> {
                            realEstates.sortedByDescending { it.realEstateEntity.numberBedroom }
                        }
                        is SortField.BathroomsNumber -> {
                            realEstates.sortedByDescending { it.realEstateEntity.numberBathroom }
                        }
                    }
                }
                else -> {
                    realEstates.sortedBy { it.realEstateEntity.entryDate }
                }
            }
     */
}
