package com.suonk.oc_project9.ui.real_estates.details

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.places.GetNearbyPointsOfInterestUseCase
import com.suonk.oc_project9.domain.real_estate.*
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import com.suonk.oc_project9.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javax.inject.Inject

@HiltViewModel
class RealEstateDetailsViewModel @Inject constructor(
    private val upsertNewRealEstateUseCase: UpsertNewRealEstateUseCase,
    private val getRealEstateFlowByIdUseCase: GetRealEstateFlowByIdUseCase,
    private val getPositionFromFullAddressUseCase: GetPositionFromFullAddressUseCase,
    private val getNearbyPointsOfInterestUseCase: GetNearbyPointsOfInterestUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context,
    private val navArgProducer: NavArgProducer
) : ViewModel() {

    val finishSavingSingleLiveEvent = SingleLiveEvent<Unit>()
    val isFieldEmptySingleLiveEvent = SingleLiveEvent<Boolean>()
    val toastMessageSingleLiveEvent = SingleLiveEvent<String>()

    private var realEstateDetailsViewStateMutableSharedFlow = MutableSharedFlow<RealEstateDetailsViewState>(replay = 1)
    private val photosMutableStateFlow = MutableStateFlow<Set<AggregatedPhoto>>(setOf())
    private val isSoldMutableStateFlow = MutableStateFlow(false)

    fun onSoldRealEstateClick() {
        isSoldMutableStateFlow.update { !it }
    }

    data class AggregatedPhoto(
        val uri: String,
    )

    val realEstateDetailsViewStateLiveData: LiveData<RealEstateDetailsViewState> = liveData(coroutineDispatcherProvider.io) {
        combine(
            realEstateDetailsViewStateMutableSharedFlow, photosMutableStateFlow, isSoldMutableStateFlow
        ) { detailsViewState, aggregatedPhotos, isSold ->

            Log.i("PointsOfInterest", "pointsOfInterest :  ${detailsViewState.pointsOfInterest}")
            emit(
                RealEstateDetailsViewState(
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
                realEstateDetailsViewStateMutableSharedFlow.emit(
                    RealEstateDetailsViewState(
                        id = 0,
                        type = "",
                        typePosition = 0,
                        price = "0",
                        livingSpace = "0",
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
                        entryDate = System.currentTimeMillis(),
                        saleDate = null,
                        isSold = false,
                        pointsOfInterest = emptyList()
                    )
                )
                isSoldMutableStateFlow.emit(false)
            } else {
                val pointsOfInterest = getNearbyPointsOfInterestUseCase.invoke(
                    lat = realEstateEntityWithPhotos.realEstateEntity.latitude,
                    long = realEstateEntityWithPhotos.realEstateEntity.longitude
                )

                // Update mode
                realEstateDetailsViewStateMutableSharedFlow.emit(
                    RealEstateDetailsViewState(
                        id = realEstateEntityWithPhotos.realEstateEntity.id,
                        type = realEstateEntityWithPhotos.realEstateEntity.type,
                        typePosition = realEstateTypeToSpinnerPosition(realEstateEntityWithPhotos.realEstateEntity.type),
                        price = realEstateEntityWithPhotos.realEstateEntity.price.toString(),
                        livingSpace = realEstateEntityWithPhotos.realEstateEntity.livingSpace.toString(),
                        numberRooms = realEstateEntityWithPhotos.realEstateEntity.numberRooms.toString(),
                        numberBedroom = realEstateEntityWithPhotos.realEstateEntity.numberBedroom.toString(),
                        numberBathroom = realEstateEntityWithPhotos.realEstateEntity.numberBathroom.toString(),
                        description = realEstateEntityWithPhotos.realEstateEntity.description,
                        photos = realEstateEntityWithPhotos.photos.map { photoEntity ->
                            DetailsPhotoViewState(uri = photoEntity.photo, onDeleteCallback = EquatableCallback {
                                onPhotoDeleted(photoEntity.photo)
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
                        entryDate = fromLocalDateToLong(realEstateEntityWithPhotos.realEstateEntity.entryDate),
                        saleDate = fromLocalDateToLongWithNullable(realEstateEntityWithPhotos.realEstateEntity.saleDate),
                        isSold = realEstateEntityWithPhotos.realEstateEntity.saleDate != null,
                        pointsOfInterest = pointsOfInterest
                    )
                )
                isSoldMutableStateFlow.emit(realEstateEntityWithPhotos.realEstateEntity.saleDate != null)

                val photos = realEstateEntityWithPhotos.photos.map { photoEntity -> AggregatedPhoto(uri = photoEntity.photo) }.toSet()
                photosMutableStateFlow.emit(photos)
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
            val photos = arrayListOf<AggregatedPhoto>()
            photos.addAll(photosMutableStateFlow.replayCache.first().distinct())

            val entryDate = realEstateDetailsViewStateMutableSharedFlow.replayCache.first().entryDate

            val saleDate = if (isSoldMutableStateFlow.value) {
                realEstateDetailsViewStateMutableSharedFlow.replayCache.first().saleDate ?: System.currentTimeMillis()
            } else {
                null
            }

            val position = getPositionFromFullAddressUseCase.invoke(
                context.getString(
                    R.string.full_address,
                    gridZone,
                    streetName,
                    city,
                    state,
                    postalCode,
                ), context
            )

            val isFieldEmpty =
                isEmptyOrBlank(price) || isEmptyOrBlank(livingSpace) || isEmptyOrBlank(numberRooms) || isEmptyOrBlank(numberBedroom) || isEmptyOrBlank(
                    numberBathroom
                ) || isEmptyOrBlank(description) || isEmptyOrBlank(postalCode) || isEmptyOrBlank(city) || isEmptyOrBlank(streetName) || isEmptyOrBlank(
                    gridZone
                )

            if (isFieldEmpty) {
                withContext(coroutineDispatcherProvider.main) {
                    toastMessageSingleLiveEvent.value = context.getString(R.string.field_empty_toast_msg)
                }
            } else {
                upsertNewRealEstateUseCase.invoke(
                    realEstate = RealEstateEntity(
                        id = navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId,
                        type = spinnerPositionToType(type),
                        price = price.toDouble(),
                        livingSpace = livingSpace.toDouble(),
                        numberRooms = numberRooms.toInt(),
                        numberBedroom = numberBedroom.toInt(),
                        numberBathroom = numberBathroom.toInt(),
                        description = description,
                        postalCode = postalCode,
                        state = state,
                        city = city,
                        streetName = streetName,
                        gridZone = gridZone,
                        pointOfInterest = "",
                        status = "AVAILABLE",
                        entryDate = fromLongToLocalDate(entryDate),
                        saleDate = fromLongToLocalDateWithNullable(saleDate),
                        latitude = position.lat,
                        longitude = position.long,
                        agentInChargeId = 0L
                    ), photos = photos
                )

                withContext(coroutineDispatcherProvider.main) {
                    finishSavingSingleLiveEvent.value = Unit

                    if (navArgProducer.getNavArgs(RealEstateDetailsFragmentArgs::class).realEstateId == 0L) {
                        toastMessageSingleLiveEvent.value = context.getString(R.string.new_real_estate_is_added)
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

    fun onNewPhotoAdded(photo: Uri) {
        photosMutableStateFlow.update { list ->
            if (list.contains(AggregatedPhoto(photo.toString()))) {
                toastMessageSingleLiveEvent.value = context.getString(R.string.image_already_in_list)
                list
            } else {
                list + AggregatedPhoto(photo.toString())
            }
        }
    }

    private fun onPhotoDeleted(photo: String) {
        photosMutableStateFlow.update {
            it - AggregatedPhoto(photo)
        }
    }

    //endregion

    private fun fromLocalDateToLong(value: LocalDateTime): Long = value.toEpochSecond(ZoneOffset.UTC)
    private fun fromLocalDateToLongWithNullable(value: LocalDateTime?): Long? = value?.toEpochSecond(ZoneOffset.UTC)

    private fun fromLongToLocalDate(value: Long): LocalDateTime = Instant.ofEpochSecond(value).atZone(ZoneOffset.UTC).toLocalDateTime()
    private fun fromLongToLocalDateWithNullable(value: Long?): LocalDateTime? =
        value?.let { Instant.ofEpochSecond(it).atZone(ZoneOffset.UTC).toLocalDateTime() }
}