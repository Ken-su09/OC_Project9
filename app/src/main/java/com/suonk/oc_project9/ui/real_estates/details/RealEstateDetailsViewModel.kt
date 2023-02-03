package com.suonk.oc_project9.ui.real_estates.details

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.*
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.real_estate.*
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
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
    private val upsertNewRealEstateUseCase: UpsertNewRealEstateUseCase,
    private val getRealEstateFlowByIdUseCase: GetRealEstateFlowByIdUseCase,
    private val getPositionFromFullAddressUseCase: GetPositionFromFullAddressUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context,
    private val navArgProducer: NavArgProducer
) : ViewModel() {

    val finishSavingSingleLiveEvent = SingleLiveEvent<Unit>()
    val isFieldEmptySingleLiveEvent = SingleLiveEvent<Boolean>()
    val toastMessageSingleLiveEvent = SingleLiveEvent<String>()

    val isListEmptySingleLiveEvent = SingleLiveEvent<Boolean>()

    private var realEstateFormMutableSharedFlow = MutableSharedFlow<RealEstateForm>(replay = 1)
    private var _realEstateFormMutableSharedFlow = MutableSharedFlow<RealEstateForm>(replay = 1)

    private val photosMutableStateFlow = MutableStateFlow<List<AggregatedPhoto>>(emptyList())

    data class AggregatedPhoto(
        val uri: String,
    )

    val realEstateDetailsViewStateLiveData: LiveData<RealEstateDetailsViewState> = liveData(coroutineDispatcherProvider.io) {
        combine(
            realEstateFormMutableSharedFlow,
            photosMutableStateFlow,
        ) { form, aggregatedPhotos ->
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
                    noPhoto = photoEntities.isEmpty(),
                    photos = aggregatedPhotos.map {
                        DetailsPhotoViewState(
                            uri = it.uri.toString(),
                            clickedCallback = {
                                photosMutableStateFlow.update { list ->
                                    list - it
                                }
                            }
                        )
                    }
                )
            )
        }.collect()
    }

    init {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val id = navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId

            val realEstateEntityWithPhotos = getRealEstateFlowByIdUseCase.invoke(id).firstOrNull()

            if (realEstateEntityWithPhotos == null) {
                // Create mode
                _realEstateFormMutableSharedFlow.emit(
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
                _realEstateFormMutableSharedFlow.emit(
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
                        photos = realEstateEntityWithPhotos.photos.map { photoEntity ->
                            DetailsPhotoViewState(
                                uri = Uri.parse(photoEntity.photo),
                                clickedCallback = {
                                    photosMutableStateFlow.update {
                                        it - AggregatedPhoto(photoEntity.photo)
                                    }
                                }
                            )
                        },
                        city = realEstateEntityWithPhotos.realEstateEntity.city,
                        postalCode = realEstateEntityWithPhotos.realEstateEntity.postalCode,
                        state = realEstateEntityWithPhotos.realEstateEntity.state,
                        streetName = realEstateEntityWithPhotos.realEstateEntity.streetName,
                        gridZone = realEstateEntityWithPhotos.realEstateEntity.gridZone
                    )
                )
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
                        photos = realEstateEntityWithPhotos.photos.map { DetailsPhotoViewState(Uri.parse(it.photo)) },
                        city = realEstateEntityWithPhotos.realEstateEntity.city,
                        postalCode = realEstateEntityWithPhotos.realEstateEntity.postalCode,
                        state = realEstateEntityWithPhotos.realEstateEntity.state,
                        streetName = realEstateEntityWithPhotos.realEstateEntity.streetName,
                        gridZone = realEstateEntityWithPhotos.realEstateEntity.gridZone
                    )
                )

                val photos = arrayListOf<DetailsPhotoViewState>()
                photos.addAll(realEstateEntityWithPhotos.photos.map { DetailsPhotoViewState(Uri.parse(it.photo)) })
                photosMutableStateFlow.emit(photos)
            }
        }
    }

    fun onSaveRealEstateButtonClicked(

    ) {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val photos = arrayListOf<DetailsPhotoViewState>()
            photos.addAll(realEstateFormMutableSharedFlow.replayCache.first().photos)
            photosMutableStateFlow.emit(photos)

            Log.i("GetPhotosList", "photos : $photos")

            realEstateFormMutableSharedFlow = _realEstateFormMutableSharedFlow

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

//            upsertNewRealEstateUseCase.invoke(
//                realEstate = RealEstateEntity(
//                    id = form.id ?: 0L,
//                    type = form.type,
//                    price = form.price,
//                    livingSpace = form.livingSpace,
//                    numberRooms = form.numberRooms,
//                    numberBedroom = form.numberBedroom,
//                    numberBathroom = form.numberBathroom,
//                    description = form.description,
//                    postalCode = form.postalCode,
//                    state = form.state,
//                    city = form.city,
//                    streetName = form.streetName,
//                    gridZone = form.gridZone,
//                    pointOfInterest = "",
//                    status = "AVAILABLE",
//                    entryDate = System.currentTimeMillis(),
//                    saleDate = null,
//                    latitude = position.lat,
//                    longitude = position.long,
//                    agentInChargeId = 0L
//                ), photos = photos
//            )

//            withContext(coroutineDispatcherProvider.main) {
//                finishSavingSingleLiveEvent.value = Unit
//            }
        }
    }

    //region ===============================================  ===============================================

    private fun updateForm(block: (RealEstateForm) -> RealEstateForm) {
        if (_realEstateFormMutableSharedFlow.replayCache.isNotEmpty()) {
            _realEstateFormMutableSharedFlow.tryEmit(block(_realEstateFormMutableSharedFlow.replayCache.first()))
        }
    }

    private fun updateFormPhoto(block: (RealEstateForm) -> RealEstateForm) {
        realEstateFormMutableSharedFlow.tryEmit(block(realEstateFormMutableSharedFlow.replayCache.first()))
//        _realEstateFormMutableSharedFlow.tryEmit(block(_realEstateFormMutableSharedFlow.replayCache.first()))
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

    //    private fun updatePhotosForm(block: (ArrayList<PhotoViewState>) -> ArrayList<PhotoViewState>) {
//        photosMutableStateFlow.tryEmit(block(photosMutableStateFlow.replayCache.first()))
//    }

    private fun realEstateTypeToSpinnerPosition(type: String): Int {
        val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
        return types.indexOf(type)
    }

    private fun spinnerPositionToType(position: Int): String {
        val types = arrayListOf("House", "Penthouse", "Duplex", "Flat", "Loft")
        return types[position]
    }

    private fun isEmptyOrBlank(value: String): Boolean {
        return value.isEmpty() || value.isBlank() || value == "" || value == " "
    }

    //region ================================================================== PHOTO ==================================================================

    fun onNewPhotoAdded(photo: Uri) {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            photosMutableStateFlow.collect {
                if (it.contains(DetailsPhotoViewState(photo))) {
                    withContext(coroutineDispatcherProvider.main) {
                        toastMessageSingleLiveEvent.value = context.getString(R.string.image_already_in_list)
                    }
                } else {
                    it.add(DetailsPhotoViewState(photo))

                    updateFormPhoto { form ->
                        form.copy(photos = it)
                    }
                }
            }
        }
    }

    fun onPhotoDeleted(photo: String) {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            photosMutableStateFlow.collect {
                it.remove(DetailsPhotoViewState(Uri.parse(photo)))

                updateFormPhoto { form ->
                    form.copy(photos = it)
                }
            }
        }
    }

    //endregion
}