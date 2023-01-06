package com.suonk.oc_project9.ui.real_estates.create

import android.content.Context
import androidx.lifecycle.*
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.real_estate.UpdateRealEstateUseCase
import com.suonk.oc_project9.domain.real_estate.GetLatitudeFromFullAddressUseCase
import com.suonk.oc_project9.domain.real_estate.GetLongitudeFromFullAddressUseCase
import com.suonk.oc_project9.domain.real_estate.GetRealEstateFlowByIdUseCase
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import com.suonk.oc_project9.ui.real_estates.carousel.PhotoViewState
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.NavArgProducer
import com.suonk.oc_project9.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealEstateDetailsViewModel @Inject constructor(
    private val updateRealEstateUseCase: UpdateRealEstateUseCase,

    private val getRealEstateFlowByIdUseCase: GetRealEstateFlowByIdUseCase,

    private val getLatitudeFromFullAddressUseCase: GetLatitudeFromFullAddressUseCase,
    private val getLongitudeFromFullAddressUseCase: GetLongitudeFromFullAddressUseCase,

    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context,
    private val navArgProducer: NavArgProducer
) : ViewModel() {

    val isFieldEmptySingleLiveEvent = SingleLiveEvent<Boolean>()
    val toastMessageSingleLiveEvent = SingleLiveEvent<String>()

    val isListEmptySingleLiveEvent = SingleLiveEvent<Boolean>()

    private val realEstateFormMutableSharedFlow = MutableSharedFlow<RealEstateForm>(replay = 1)
    private val realEstateEntityMutableSharedFlow = MutableSharedFlow<RealEstateEntityWithPhotos>(replay = 1)

    private val photosMutableStateFlow = MutableStateFlow<List<PhotoEntity>>(emptyList())

    val realEstateDetailsViewStateLiveData: LiveData<RealEstateDetailsViewState> =
        liveData(coroutineDispatcherProvider.io) {
            combine(
                realEstateFormMutableSharedFlow,
                realEstateEntityMutableSharedFlow,
                photosMutableStateFlow,
            ) { form, entity, photoEntities ->
                emit(
                    RealEstateDetailsViewState(
                        id = form.id ?: entity.realEstateEntity.id,
                        type = form.type ?: entity.realEstateEntity.price.toString(),
                        typePosition = form.typePosition
                            ?: realEstateTypeToSpinnerPosition(entity.realEstateEntity.type),
                        price = form.price?.toString() ?: entity.realEstateEntity.price.toString(),
                        livingSpace = form.livingSpace?.toString()
                            ?: entity.realEstateEntity.livingSpace.toString(),
                        numberRooms = form.numberRooms?.toString()
                            ?: entity.realEstateEntity.numberRooms.toString(),
                        numberBedroom = form.numberBedroom?.toString()
                            ?: entity.realEstateEntity.numberBedroom.toString(),
                        numberBathroom = form.numberBathroom?.toString()
                            ?: entity.realEstateEntity.numberBathroom.toString(),
                        description = form.description ?: entity.realEstateEntity.description,
                        photos = form.photos ?: entity.photos.map {
                            PhotoViewState(false, it.photo)
                        },
                        city = form.city ?: entity.realEstateEntity.city,
                        postalCode = form.postalCode ?: entity.realEstateEntity.postalCode,
                        state = form.state ?: entity.realEstateEntity.state,
                        streetName = form.streetName ?: entity.realEstateEntity.streetName,
                        gridZone = form.gridZone ?: entity.realEstateEntity.gridZone,
                        latitude = form.latitude ?: entity.realEstateEntity.latitude,
                        longitude = form.longitude ?: entity.realEstateEntity.longitude,
                    )
                )
            }.collect()
        }

    init {
        realEstateFormMutableSharedFlow.onStart {
            val realEstateEntityWithPhotos = getRealEstateFlowByIdUseCase.invoke(
                navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).id
            ).firstOrNull()

            if (realEstateEntityWithPhotos == null) {
                // Create mode
                emit(
                    RealEstateForm(
                        0L,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null,
                        null
                    )
                )
            } else {
                // Update mode
                emit(
                    RealEstateForm(
                        id = realEstateEntityWithPhotos.realEstateEntity.id,
                        type = realEstateEntityWithPhotos.realEstateEntity.type,
                        typePosition = realEstateTypeToSpinnerPosition(realEstateEntityWithPhotos.realEstateEntity.type),
                        price = realEstateEntityWithPhotos.realEstateEntity.price,
                        livingSpace = realEstateEntityWithPhotos.realEstateEntity.livingSpace,
                        numberRooms = realEstateEntityWithPhotos.realEstateEntity.numberRooms,
                        numberBedroom = realEstateEntityWithPhotos.realEstateEntity.numberBedroom,
                        numberBathroom = realEstateEntityWithPhotos.realEstateEntity.numberBathroom,
                        description = realEstateEntityWithPhotos.realEstateEntity.description,
                        photos = realEstateEntityWithPhotos.photos.map {
                            PhotoViewState(false, it.photo)
                        },
                        city = realEstateEntityWithPhotos.realEstateEntity.city,
                        postalCode = realEstateEntityWithPhotos.realEstateEntity.postalCode,
                        state = realEstateEntityWithPhotos.realEstateEntity.state,
                        streetName = realEstateEntityWithPhotos.realEstateEntity.streetName,
                        gridZone = realEstateEntityWithPhotos.realEstateEntity.gridZone,
                        latitude = realEstateEntityWithPhotos.realEstateEntity.livingSpace,
                        longitude = realEstateEntityWithPhotos.realEstateEntity.livingSpace,
                    )
                )
            }
        }
    }

//    fun saveRealEstateDetails(estate: RealEstateDetailsViewState) {
//        if (isEmptyOrBlank(estate.city) || isEmptyOrBlank(estate.description) || isEmptyOrBlank(
//                estate.price
//            ) || isEmptyOrBlank(estate.livingSpace) || isEmptyOrBlank(estate.postalCode) || isEmptyOrBlank(
//                estate.state
//            ) || isEmptyOrBlank(estate.streetName) || isEmptyOrBlank(estate.gridZone) || isEmptyOrBlank(
//                estate.numberRooms
//            ) || isEmptyOrBlank(estate.numberBedroom) || isEmptyOrBlank(estate.numberBathroom)
//        ) {
//            toastMessageSingleLiveEvent.value = context.getString(R.string.field_empty_toast_msg)
//            isFieldEmptySingleLiveEvent.value = true
//        } else {
//            isFieldEmptySingleLiveEvent.value = false
//            CoroutineScope(coroutineDispatcherProvider.io).launch {
//                addNewRealEstateUseCase.invoke(
//                    realEstate = RealEstateEntity(
//                        id = estate.id,
//                        type = estate.type,
//                        price = estate.price.toDouble(),
//                        livingSpace = estate.livingSpace.toDouble(),
//                        numberRooms = estate.numberRooms.toInt(),
//                        numberBedroom = estate.numberBedroom.toInt(),
//                        numberBathroom = estate.numberBathroom.toInt(),
//                        description = estate.description,
//                        fullAddress = context.getString(
//                            R.string.full_address,
//                            estate.gridZone,
//                            estate.streetName,
//                            estate.city,
//                            estate.state,
//                            estate.postalCode,
//                        ),
//                        postalCode = estate.postalCode,
//                        state = estate.state,
//                        city = estate.city,
//                        streetName = estate.streetName,
//                        gridZone = estate.gridZone,
//                        "",
//                        "Available",
//                        System.currentTimeMillis(),
//                        null,
//                        latitude = getLatitudeFromFullAddressUseCase.invoke(
//                            context.getString(
//                                R.string.full_address,
//                                estate.gridZone,
//                                estate.streetName,
//                                estate.city,
//                                estate.state,
//                                estate.postalCode,
//                            ), context
//                        ),
//                        longitude = getLongitudeFromFullAddressUseCase.invoke(
//                            context.getString(
//                                R.string.full_address,
//                                estate.gridZone,
//                                estate.streetName,
//                                estate.city,
//                                estate.state,
//                                estate.postalCode,
//                            ), context
//                        ),
//                        1L,
//                    ), photos = estate.photos
//                )
//            }
//        }
//    }

    private fun isEmptyOrBlank(value: String): Boolean {
        return value.isEmpty() || value.isBlank() || value == "" || value == " "
    }

    fun setPhotosLiveData(photoUrl: String, photos: ArrayList<PhotoViewState>, isUri: Boolean) {
        val currentList = arrayListOf<PhotoViewState>()
        currentList.addAll(photos)

        isFieldEmptySingleLiveEvent.value = isEmptyOrBlank(photoUrl)
        if (isEmptyOrBlank(photoUrl)) {
            isFieldEmptySingleLiveEvent.value = true
            toastMessageSingleLiveEvent.value = context.getString(R.string.field_empty_toast_msg)
        } else {
            if (!currentList.contains(PhotoViewState(isUri, photoUrl))) {
                currentList.add(PhotoViewState(isUri, photoUrl))
            }

            photosLiveData.value = currentList
        }

        isListEmptySingleLiveEvent.value = currentList.isEmpty()
    }

    fun onAddRealButtonClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val form = realEstateFormMutableSharedFlow.replayCache.first()
            updateRealEstateUseCase.invoke(
                RealEstateEntity(
                    form.
                ),
                form.photos ?: arrayListOf()
            )
        }
    }

    private fun realEstateTypeToSpinnerPosition(type: String): Int {
        val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
        return types.indexOf(type)
    }

    private fun spinnerPositionToType(position: Int): String {
        val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
        return types[position]
    }

    //region ===============================================  ===============================================

    private fun updateForm(block: (RealEstateForm) -> RealEstateForm) {
        realEstateFormMutableSharedFlow.tryEmit(block(realEstateFormMutableSharedFlow.replayCache.first()))
    }

    fun onTypeChanged(typePosition: Int?) {
        val casted = typePosition ?: return

        updateForm {
            it.copy(type = spinnerPositionToType(casted), typePosition = casted)
        }
    }

    fun onPriceChanged(price: String?) {
        val casted = price?.toDoubleOrNull() ?: return

        updateForm {
            it.copy(price = casted)
        }
    }

    fun onLivingSpaceChanged(livingSpace: String?) {
        val casted = livingSpace?.toDoubleOrNull() ?: return

        updateForm {
            it.copy(livingSpace = casted)
        }
    }

    fun onNumberRoomsChanged(nbRooms: String?) {
        val casted = nbRooms?.toIntOrNull() ?: return

        updateForm {
            it.copy(numberRooms = casted)
        }
    }

    fun onNumberBedroomsChanged(nbBedrooms: String?) {
        val casted = nbBedrooms?.toIntOrNull() ?: return

        updateForm {
            it.copy(numberBedroom = casted)
        }
    }

    fun onNumberBathroomsChanged(nbBathrooms: String?) {
        val casted = nbBathrooms?.toIntOrNull() ?: return

        updateForm {
            it.copy(numberBathroom = casted)
        }
    }

    fun onDescriptionChanged(description: String?) {
        val casted = description ?: return

        updateForm {
            it.copy(description = casted)
        }
    }

    fun onCityChanged(city: String?) {
        val casted = city ?: return

        updateForm {
            it.copy(city = casted)
        }
    }

    fun onPostalCodeChanged(postalCode: String?) {
        val casted = postalCode ?: return

        updateForm {
            it.copy(postalCode = casted)
        }
    }

    fun onStateChanged(state: String?) {
        val casted = state ?: return

        updateForm {
            it.copy(state = casted)
        }
    }

    fun onStreetNameChanged(streetName: String?) {
        val casted = streetName ?: return

        updateForm {
            it.copy(streetName = casted)
        }
    }

    fun onGridZoneChanged(gridZone: String?) {
        val casted = gridZone ?: return

        updateForm {
            it.copy(gridZone = casted)
        }
    }

    //endregion
}