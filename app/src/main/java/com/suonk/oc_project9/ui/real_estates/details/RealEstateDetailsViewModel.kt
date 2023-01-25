package com.suonk.oc_project9.ui.real_estates.details

import android.content.Context
import androidx.lifecycle.*
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.real_estate.*
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import com.suonk.oc_project9.ui.real_estates.carousel.PhotoViewState
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.NavArgProducer
import com.suonk.oc_project9.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class RealEstateDetailsViewModel @Inject constructor(
    private val insertNewRealEstateUseCase: UpsertNewRealEstateUseCase,
    private val getRealEstateFlowByIdUseCase: GetRealEstateFlowByIdUseCase,
    private val getPositionFromFullAddressUseCase: GetPositionFromFullAddressUseCase,
    private val updateRealEstateUseCase: UpdateRealEstateUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context,
    private val navArgProducer: NavArgProducer
) : ViewModel() {

    val finishSavingSingleLiveEvent = SingleLiveEvent<Unit>()
    val isFieldEmptySingleLiveEvent = SingleLiveEvent<Boolean>()
    val toastMessageSingleLiveEvent = SingleLiveEvent<String>()

    val isListEmptySingleLiveEvent = SingleLiveEvent<Boolean>()

    private val realEstateFormMutableSharedFlow = MutableSharedFlow<RealEstateForm>(replay = 1)

    private val photosMutableStateFlow = MutableStateFlow<List<PhotoEntity>>(emptyList())

    val realEstateDetailsViewStateLiveData: LiveData<RealEstateDetailsViewState> = liveData(coroutineDispatcherProvider.io) {
        combine(
            realEstateFormMutableSharedFlow,
            photosMutableStateFlow,
        ) { form, photoEntities ->
            emit(
                RealEstateDetailsViewState(
                    type = form.type,
                    typePosition = form.typePosition,
                    price = form.price.toString(),
                    livingSpace = form.livingSpace.toString(),
                    numberRooms = form.numberRooms.toString(),
                    numberBedroom = form.numberBedroom.toString(),
                    numberBathroom = form.numberBathroom.toString(),
                    description = form.description,
                    photos = form.photos,
                    city = form.city,
                    postalCode = form.postalCode,
                    state = form.state,
                    streetName = form.streetName,
                    gridZone = form.gridZone,
                    noPhoto = photoEntities.isEmpty()
                )
            )
        }.collect()
    }

    init {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val realEstateEntityWithPhotos =
                getRealEstateFlowByIdUseCase.invoke(navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).id).firstOrNull()

            if (realEstateEntityWithPhotos == null) {
                // Create mode
                realEstateFormMutableSharedFlow.emit(
                    RealEstateForm(
                        id = null,
                        type = "",
                        typePosition = 0,
                        price = 0.0,
                        livingSpace = 0.0,
                        numberRooms = 0,
                        numberBedroom = 0,
                        numberBathroom = 0,
                        description = "",
                        photos = arrayListOf(),
                        city = "",
                        postalCode = "",
                        state = "",
                        streetName = "",
                        gridZone = "",
                    )
                )
            } else {
                // Update mode

                realEstateFormMutableSharedFlow.emit(
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
                        photos = realEstateEntityWithPhotos.photos.map { PhotoViewState(false, it.photo) },
                        city = realEstateEntityWithPhotos.realEstateEntity.city,
                        postalCode = realEstateEntityWithPhotos.realEstateEntity.postalCode,
                        state = realEstateEntityWithPhotos.realEstateEntity.state,
                        streetName = realEstateEntityWithPhotos.realEstateEntity.streetName,
                        gridZone = realEstateEntityWithPhotos.realEstateEntity.gridZone
                    )
                )

                photosMutableStateFlow.emit(realEstateEntityWithPhotos.photos)
            }
        }
    }

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
        }

        isListEmptySingleLiveEvent.value = currentList.isEmpty()
    }

    fun onSaveRealEstateButtonClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val form = realEstateFormMutableSharedFlow.replayCache.first()

            val position = getPositionFromFullAddressUseCase.invoke(
                context.getString(
                    R.string.full_address,
                    form.gridZone,
                    form.streetName,
                    form.city,
                    form.state,
                    form.postalCode,
                ), context
            )

//            fullAddress = context.getString(
//                R.string.full_address, form.gridZone, form.streetName, form.city, form.state, form.postalCode
//            ),

            insertNewRealEstateUseCase.invoke(
                realEstate = RealEstateEntity(
                    id = form.id ?: 0L,
                    type = form.type,
                    price = form.price,
                    livingSpace = form.livingSpace,
                    numberRooms = form.numberRooms,
                    numberBedroom = form.numberBedroom,
                    numberBathroom = form.numberBathroom,
                    description = form.description,
                    postalCode = form.postalCode,
                    state = form.state,
                    city = form.city,
                    streetName = form.streetName,
                    gridZone = form.gridZone,
                    pointOfInterest = "",
                    status = "AVAILABLE",
                    entryDate = System.currentTimeMillis(),
                    saleDate = null,
                    latitude = position.lat,
                    longitude = position.long,
                    agentInChargeId = 0L
                ), photos = form.photos
            )

            withContext(coroutineDispatcherProvider.main) {
                finishSavingSingleLiveEvent.value = Unit
            }
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

    fun onTypeChanged(position: Int) {
        updateForm {
            it.copy(type = spinnerPositionToType(position), typePosition = position)
        }
    }

    fun onPriceChanged(newTextInput: String?) {
        newTextInput?.toDoubleOrNull()?.let { casted ->
            updateForm { form ->
                form.copy(price = casted)
            }
        }
    }

    fun onLivingSpaceChanged(newTextInput: String?) {
        newTextInput?.toDoubleOrNull()?.let { casted ->
            updateForm { form ->
                form.copy(livingSpace = casted)
            }
        }
    }

    fun onNumberRoomsChanged(newTextInput: String?) {
        newTextInput?.toIntOrNull()?.let { casted ->
            updateForm { form ->
                form.copy(numberRooms = casted)
            }
        }
    }

    fun onNumberBedroomsChanged(newTextInput: String?) {
        newTextInput?.toIntOrNull()?.let { casted ->
            updateForm { form ->
                form.copy(numberBedroom = casted)
            }
        }
    }

    fun onNumberBathroomsChanged(newTextInput: String?) {
        newTextInput?.toIntOrNull()?.let { casted ->
            updateForm { form ->
                form.copy(numberBathroom = casted)
            }
        }
    }

    fun onDescriptionChanged(newTextInput: String?) {
        newTextInput?.let { casted ->
            updateForm { form ->
                form.copy(description = casted)
            }
        }
    }

    fun onCityChanged(newTextInput: String?) {
        newTextInput?.let { casted ->
            updateForm { form ->
                form.copy(city = casted)
            }
        }
    }

    fun onPostalCodeChanged(newTextInput: String?) {
        newTextInput?.let { casted ->
            updateForm { form ->
                form.copy(postalCode = casted)
            }
        }
    }

    fun onStateChanged(newTextInput: String?) {
        newTextInput?.let { casted ->
            updateForm { form ->
                form.copy(state = casted)
            }
        }
    }

    fun onStreetNameChanged(newTextInput: String?) {
        newTextInput?.let { casted ->
            updateForm { form ->
                form.copy(streetName = casted)
            }
        }
    }

    fun onGridZoneChanged(newTextInput: String?) {
        newTextInput?.let { casted ->
            updateForm { form ->
                form.copy(gridZone = casted)
            }
        }
    }

    //endregion
}