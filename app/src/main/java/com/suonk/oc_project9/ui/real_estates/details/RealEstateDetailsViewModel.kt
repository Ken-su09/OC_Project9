package com.suonk.oc_project9.ui.real_estates.details

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.photo.AddNewPhotoUseCase
import com.suonk.oc_project9.domain.photo.DeletePhotoUseCase
import com.suonk.oc_project9.domain.photo.GetPhotosListByIdUseCase
import com.suonk.oc_project9.domain.places.GetNearbyPointsOfInterestUseCase
import com.suonk.oc_project9.domain.real_estate.get.GetPositionFromFullAddressUseCase
import com.suonk.oc_project9.domain.real_estate.get.GetRealEstateFlowByIdUseCase
import com.suonk.oc_project9.domain.real_estate.upsert.UpsertNewRealEstateUseCase
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.*
import javax.inject.Inject

@HiltViewModel
class RealEstateDetailsViewModel @Inject constructor(
    private val upsertNewRealEstateUseCase: UpsertNewRealEstateUseCase,
    private val getRealEstateFlowByIdUseCase: GetRealEstateFlowByIdUseCase,
    private val getPositionFromFullAddressUseCase: GetPositionFromFullAddressUseCase,
    private val getNearbyPointsOfInterestUseCase: GetNearbyPointsOfInterestUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val application: Application,
    private val navArgProducer: NavArgProducer,
    private val clock: Clock
) : ViewModel() {

    val finishSavingSingleLiveEvent = SingleLiveEvent<Unit>()
    val toastMessageSingleLiveEvent = SingleLiveEvent<NativeText>()

    private var realEstateDetailsViewStateMutableSharedFlow = MutableSharedFlow<RealEstateDetailsViewState>(replay = 1)
    private val photosMutableStateFlow = MutableStateFlow<Set<AggregatedPhoto>>(setOf())
    private val isSoldMutableStateFlow = MutableStateFlow(false)

    fun onSoldRealEstateClick() {
        isSoldMutableStateFlow.update { !it }
    }

    val realEstateDetailsViewStateLiveData: LiveData<RealEstateDetailsViewState> = liveData(coroutineDispatcherProvider.io) {
        combine(
            realEstateDetailsViewStateMutableSharedFlow, photosMutableStateFlow, isSoldMutableStateFlow
        ) { detailsViewState, aggregatedPhotos, isSold ->
            emit(
                RealEstateDetailsViewState(
                    id = detailsViewState.id,
                    type = detailsViewState.type,
                    typePosition = detailsViewState.typePosition,
                    price = detailsViewState.price,
                    livingSpace = detailsViewState.livingSpace,
                    numberRooms = detailsViewState.numberRooms,
                    numberBedroom = detailsViewState.numberBedroom,
                    numberBathroom = detailsViewState.numberBathroom,
                    description = detailsViewState.description,
                    photos = aggregatedPhotos.map {
                        DetailsPhotoViewState(it.uri, onDeleteCallback = EquatableCallback {
                            onPhotoDeleted(it.uri)
                        })
                    },
                    city = detailsViewState.city,
                    postalCode = detailsViewState.postalCode,
                    state = detailsViewState.state,
                    streetName = detailsViewState.streetName,
                    gridZone = detailsViewState.gridZone,
                    latitude = detailsViewState.latitude,
                    longitude = detailsViewState.longitude,
                    noPhoto = aggregatedPhotos.isEmpty() && !isSold,
                    entryDate = detailsViewState.entryDate,
                    saleDate = detailsViewState.saleDate,
                    isSold = isSold,
                    pointsOfInterest = detailsViewState.pointsOfInterest
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
                realEstateDetailsViewStateMutableSharedFlow.tryEmit(
                    RealEstateDetailsViewState(
                        id = 0,
                        type = spinnerPositionToType(0),
                        typePosition = 0,
                        price = "0.0",
                        livingSpace = "0.0",
                        numberRooms = "0",
                        numberBedroom = "0",
                        numberBathroom = "0",
                        description = "",
                        photos = arrayListOf(),
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
                        pointsOfInterest = emptyList()
                    )
                )
                isSoldMutableStateFlow.emit(false)
            } else {
                val pointsOfInterest = getNearbyPointsOfInterestUseCase.invoke(
                    lat = realEstateEntityWithPhotos.realEstateEntity.latitude, long = realEstateEntityWithPhotos.realEstateEntity.longitude
                )

                val price = DecimalFormat("#,###").format(realEstateEntityWithPhotos.realEstateEntity.price)

                // Update mode
                realEstateDetailsViewStateMutableSharedFlow.tryEmit(
                    RealEstateDetailsViewState(
                        id = realEstateEntityWithPhotos.realEstateEntity.id,
                        type = realEstateEntityWithPhotos.realEstateEntity.type,
                        typePosition = realEstateTypeToSpinnerPosition(realEstateEntityWithPhotos.realEstateEntity.type),
                        price = price,
                        livingSpace = realEstateEntityWithPhotos.realEstateEntity.livingSpace.toString(),
                        numberRooms = realEstateEntityWithPhotos.realEstateEntity.numberRooms.toString(),
                        numberBedroom = realEstateEntityWithPhotos.realEstateEntity.numberBedroom.toString(),
                        numberBathroom = realEstateEntityWithPhotos.realEstateEntity.numberBathroom.toString(),
                        description = realEstateEntityWithPhotos.realEstateEntity.description,
                        photos = realEstateEntityWithPhotos.photos.map { photoEntity ->
                            DetailsPhotoViewState(uri = photoEntity.photo, onDeleteCallback = EquatableCallback {
                                onPhotoDeleted(photoEntity.photo)
//                                viewModelScope.launch(coroutineDispatcherProvider.io) {
//                                    deletePhotoUseCase.invoke(
//                                        PhotoEntity(
//                                            photoEntity.id, realEstateEntityWithPhotos.realEstateEntity.id, photoEntity.photo
//                                        )
//                                    )
//                                }
                            })
                        }.distinct(),
                        city = realEstateEntityWithPhotos.realEstateEntity.city,
                        postalCode = realEstateEntityWithPhotos.realEstateEntity.postalCode,
                        state = realEstateEntityWithPhotos.realEstateEntity.state,
                        streetName = realEstateEntityWithPhotos.realEstateEntity.streetName,
                        gridZone = realEstateEntityWithPhotos.realEstateEntity.gridZone,
                        latitude = realEstateEntityWithPhotos.realEstateEntity.latitude,
                        longitude = realEstateEntityWithPhotos.realEstateEntity.longitude,
                        noPhoto = realEstateEntityWithPhotos.photos.isEmpty() && realEstateEntityWithPhotos.realEstateEntity.saleDate == null,
                        entryDate = fromLocalDateToInstant(realEstateEntityWithPhotos.realEstateEntity.entryDate),
                        saleDate = fromLocalDateToLongWithNullable(realEstateEntityWithPhotos.realEstateEntity.saleDate),
                        isSold = realEstateEntityWithPhotos.realEstateEntity.saleDate != null,
                        pointsOfInterest = pointsOfInterest
                    )
                )
                isSoldMutableStateFlow.emit(realEstateEntityWithPhotos.realEstateEntity.saleDate != null)

                val photos = realEstateEntityWithPhotos.photos.map { photoEntity -> AggregatedPhoto(uri = photoEntity.photo) }.toSet()
                photosMutableStateFlow.tryEmit(photos)
            }
        }
    }

    fun onSaveRealEstateButtonClicked(
        type: Int,
        price: String,
        livingSpace: String,
        numberRooms: String,
        numberBedroom: String,
        numberBathroom: String,
        description: String,
        postalCode: String,
        state: String,
        city: String,
        streetName: String,
        gridZone: String
    ) {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val isFieldEmpty = isEmptyOrBlank(price) || isEmptyOrBlank(livingSpace) || isEmptyOrBlank(numberRooms) || isEmptyOrBlank(
                numberBedroom
            ) || isEmptyOrBlank(
                numberBathroom
            ) || isEmptyOrBlank(description) || isEmptyOrBlank(postalCode) || isEmptyOrBlank(
                city
            ) || isEmptyOrBlank(streetName) || isEmptyOrBlank(
                gridZone
            )

            val isFieldsAreNotDigits =
                price.toDoubleOrNull() == null && livingSpace.toDoubleOrNull() == null && numberRooms.toDoubleOrNull() == null && numberBedroom.toDoubleOrNull() == null

            if (isFieldEmpty) {
                withContext(coroutineDispatcherProvider.main) {
                    toastMessageSingleLiveEvent.setValue(NativeText.Resource(R.string.field_empty_toast_msg))
                }
            } else if (isFieldsAreNotDigits) {
                withContext(coroutineDispatcherProvider.main) {
                    toastMessageSingleLiveEvent.setValue(NativeText.Resource(R.string.fields_are_not_digits))
                }
            } else {
                val photos = photosMutableStateFlow.value

                val entryDate = realEstateDetailsViewStateMutableSharedFlow.first().entryDate ?: ZonedDateTime.now(clock).toInstant()
                val saleDate = if (isSoldMutableStateFlow.value) {
                    realEstateDetailsViewStateMutableSharedFlow.first().saleDate ?: ZonedDateTime.now(clock).toInstant().toEpochMilli()
                } else {
                    null
                }

                val position = getPositionFromFullAddressUseCase.invoke(
                    application.getString(
                        R.string.full_address,
                        gridZone,
                        streetName,
                        city,
                        state,
                        postalCode,
                    ), application
                )

                val bigDecimalPrice = if (price.contains(",")) {
                    BigDecimal(price.replace(",", "").toDouble())
                } else {
                    BigDecimal(price.toDouble())
                }

                val livingSpaceToTransform = if (livingSpace.contains(",")) {
                    livingSpace.replace(",", "").toDouble()
                } else {
                    livingSpace.toDouble()
                }

                upsertNewRealEstateUseCase.invoke(
                    realEstate = RealEstateEntity(
                        id = navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId,
                        type = spinnerPositionToType(type),
                        price = bigDecimalPrice,
                        livingSpace = livingSpaceToTransform,
                        numberRooms = numberRooms.toInt(),
                        numberBedroom = numberBedroom.toInt(),
                        numberBathroom = numberBathroom.toInt(),
                        description = description,
                        postalCode = postalCode,
                        state = state,
                        city = city,
                        streetName = streetName,
                        gridZone = gridZone,
                        status = if (saleDate == null) application.getString(R.string.real_estate_status_available) else application.getString(
                            R.string.real_estate_status_not_available
                        ),
                        entryDate = fromInstantToLocalDate(entryDate),
                        saleDate = fromLongToLocalDateWithNullable(saleDate),
                        latitude = position.lat,
                        longitude = position.long,
                        agentInChargeId = 1L
                    ),
                    photos = photos.toList(),
                )

                withContext(coroutineDispatcherProvider.main) {
                    finishSavingSingleLiveEvent.setValue(Unit)

                    if (navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId == 0L) {
                        toastMessageSingleLiveEvent.setValue(NativeText.Resource(R.string.new_real_estate_is_added))
                    }
                }
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

    private fun isEmptyOrBlank(value: String): Boolean {
        return value.isEmpty() || value.isBlank() || value == "" || value == " "
    }

    //region ================================================================== PHOTO ==================================================================

    fun onNewPhotoAdded(photo: String) {
        photosMutableStateFlow.update { set ->
            if (set.contains(AggregatedPhoto(photo))) {
                toastMessageSingleLiveEvent.setValue(NativeText.Resource(R.string.image_already_in_list))
                set
            } else {
                set + AggregatedPhoto(photo)
            }
        }
    }

    fun onPhotoDeleted(photo: String) {
        photosMutableStateFlow.update {
            it - AggregatedPhoto(photo)
        }
    }

    //endregion

    private fun fromLocalDateToInstant(value: LocalDateTime): Instant = value.toInstant(ZoneOffset.UTC)

    private fun fromLocalDateToLongWithNullable(value: LocalDateTime?): Long? = value?.toEpochSecond(ZoneOffset.UTC)

    private fun fromInstantToLocalDate(instant: Instant): LocalDateTime = instant.atZone(ZoneOffset.UTC).toLocalDateTime()

    private fun fromLongToLocalDateWithNullable(value: Long?): LocalDateTime? =
        value?.let { Instant.ofEpochSecond(it).atZone(ZoneOffset.UTC).toLocalDateTime() }
}