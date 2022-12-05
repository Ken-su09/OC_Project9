package com.suonk.oc_project9.ui.real_estates.details

import android.content.Context
import android.location.Geocoder
import androidx.lifecycle.*
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.real_estate.EditRealEstateUseCase
import com.suonk.oc_project9.domain.real_estate.GetRealEstateFlowByIdUseCase
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.NavArgProducer
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import okhttp3.internal.format
import javax.inject.Inject

@HiltViewModel
class RealEstateDetailsViewModel @Inject constructor(
    private val getRealEstateFlowByIdUseCase: GetRealEstateFlowByIdUseCase,
    private val editRealEstateUseCase: EditRealEstateUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context,
    private val navArgProducer: NavArgProducer,
) : ViewModel() {

    val realEstateDetailsViewStateLiveData = liveData(coroutineDispatcherProvider.io) {
        getRealEstateFlowByIdUseCase.invoke(navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).id).collectLatest { entity ->
            emit(
                RealEstateDetailsViewState(
                    id = entity.realEstateEntity.id,
                    type = entity.realEstateEntity.type,
                    typePosition = realEstateTypeToSpinnerPosition(entity.realEstateEntity.type),
                    price = entity.realEstateEntity.price.toString(),
                    livingSpace = entity.realEstateEntity.livingSpace.toString(),
                    numberRooms = entity.realEstateEntity.numberRooms.toString(),
                    numberBedroom = entity.realEstateEntity.numberBedroom.toString(),
                    numberBathroom = entity.realEstateEntity.numberBathroom.toString(),
                    description = entity.realEstateEntity.description,
                    photos = entity.photos.map { it.url },
                    city = entity.realEstateEntity.city,
                    postalCode = entity.realEstateEntity.postalCode,
                    state = entity.realEstateEntity.state,
                    streetName = entity.realEstateEntity.streetName,
                    gridZone = entity.realEstateEntity.gridZone,
                    latitude = Geocoder(context).getFromLocationName(
                        entity.realEstateEntity.fullAddress,
                        1
                    )[0].latitude,
                    longitude = Geocoder(context).getFromLocationName(
                        entity.realEstateEntity.fullAddress,
                        1
                    )[0].longitude
                )
            )
        }
    }

    fun saveRealEstateDetails(estate: RealEstateDetailsViewState) {
//        Float("123,456.908".replace(',',''))

        CoroutineScope(coroutineDispatcherProvider.io).launch {
            editRealEstateUseCase.invoke(
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

    private fun realEstateTypeToSpinnerPosition(type: String): Int {
        val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
        return types.indexOf(type)
    }
}