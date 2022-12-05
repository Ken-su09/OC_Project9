package com.suonk.oc_project9.ui.real_estates.create

import android.content.Context
import androidx.lifecycle.ViewModel
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.real_estate.AddNewRealEstateUseCase
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import com.suonk.oc_project9.ui.real_estates.details.RealEstateDetailsViewState
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateRealEstateViewModel @Inject constructor(
    private val addNewRealEstateUseCase: AddNewRealEstateUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    fun saveRealEstateDetails(estate: RealEstateDetailsViewState) {
        CoroutineScope(coroutineDispatcherProvider.io).launch {
            addNewRealEstateUseCase.invoke(
                RealEstateEntityWithPhotos(
                    realEstateEntity = RealEstateEntity(
                        id = estate.id,
                        type = estate.type,
                        price = estate.price.toDouble(),
                        livingSpace = estate.livingSpace.toDouble(),
                        numberRooms = estate.numberRooms.toInt(),
                        numberBedroom = estate.numberBedroom.toInt(),
                        numberBathroom = estate.numberBathroom.toInt(),
                        description = estate.description,
                        fullAddress = context.getString(
                            R.string.full_address,
                            estate.gridZone,
                            estate.streetName,
                            estate.city,
                            estate.state,
                            estate.postalCode,
                        ),
                        postalCode = estate.postalCode,
                        state = estate.state,
                        city = estate.city,
                        streetName = estate.streetName,
                        gridZone = estate.gridZone,
                        "",
                        "Available",
                        System.currentTimeMillis(),
                        null,
                        1L,
                    ),
                    photos = estate.photos.map {
                        PhotoEntity(0, estate.id, it)
                    }
                )
            )
        }
    }

    fun addNewRealEstateUseCase(realEstate: CreateRealEstateViewState) {
//        CoroutineScope(coroutineDispatcherProvider.io).launch {
//            addNewRealEstateUseCase.invoke(
//                RealEstateEntityWithPhotos(
//                    realEstateEntity = RealEstateEntity(
//                        realEstate.
//                    ),
//                    photos = Photo()
//                )
//            )
//        }
    }
}