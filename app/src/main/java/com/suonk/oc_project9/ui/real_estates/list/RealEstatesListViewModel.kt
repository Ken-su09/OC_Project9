package com.suonk.oc_project9.ui.real_estates.list

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Log
import androidx.lifecycle.*
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.real_estate.GetAllRealEstatesUseCase
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import com.suonk.oc_project9.ui.real_estates.carousel.PhotoViewState
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.SingleLiveEvent
import com.suonk.oc_project9.utils.sort.Sorting
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.*
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RealEstatesListViewModel @Inject constructor(
    private val getAllRealEstatesUseCase: GetAllRealEstatesUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    val realEstatesViewAction = SingleLiveEvent<RealEstatesViewAction>()

    private val sortingMutableStateFlow = MutableStateFlow(Sorting.DATE_ASC)
    private val filteringMutableStateFlow = MutableStateFlow(R.id.remove_filter)

    sealed class RealEstatesViewAction {
        sealed class Navigate : RealEstatesViewAction() {
            data class Detail(val realEstateId: Long) : Navigate()
        }
    }

    val realEstateLiveData = liveData(coroutineDispatcherProvider.io) {
        combine(
            getAllRealEstatesUseCase.invoke(), sortingMutableStateFlow, filteringMutableStateFlow
        ) { entities: List<RealEstateEntityWithPhotos>, sorting, filterId ->
            emit(entities.sortedWith(sorting.comparator).map {
                transformEntityToViewState(it)
            }.filter {
                when (filterId) {
                    R.id.remove_filter -> {
                        it.id != 0L
                    }
                    R.id.house_filter -> {
                        it.type.contains("House")
                    }
                    R.id.penthouse_filter -> {
                        it.type.contains("Penthouse")
                    }
                    R.id.duplex_filter -> {
                        it.type.contains("Duplex")
                    }
                    R.id.flat_filter -> {
                        it.type.contains("Flat")
                    }
                    R.id.loft_filter -> {
                        it.type.contains("Loft")
                    }
                    else -> {
                        it.id != 0L
                    }
                }
            })
        }.collect()
    }

    fun setMutableState(itemId: Int) {
        when (itemId) {
            R.id.sort_by_date_asc -> {
                sortingMutableStateFlow.value = Sorting.DATE_ASC
            }
            R.id.sort_by_price_asc -> {
                sortingMutableStateFlow.value = Sorting.PRICE_ASC
            }
            R.id.sort_by_living_space_asc -> {
                sortingMutableStateFlow.value = Sorting.LIVING_SPACE_ASC
            }
            R.id.sort_by_rooms_number_asc -> {
                sortingMutableStateFlow.value = Sorting.ROOMS_NUMBER_ASC
            }
            R.id.sort_by_date_desc -> {
                sortingMutableStateFlow.value = Sorting.DATE_DESC
            }
            R.id.sort_by_price_desc -> {
                sortingMutableStateFlow.value = Sorting.PRICE_DESC
            }
            R.id.sort_by_living_space_desc -> {
                sortingMutableStateFlow.value = Sorting.LIVING_SPACE_DESC
            }
            R.id.sort_by_rooms_number_desc -> {
                sortingMutableStateFlow.value = Sorting.ROOMS_NUMBER_DESC
            }
            R.id.remove_filter -> {
                filteringMutableStateFlow.value = R.id.remove_filter
            }
            R.id.house_filter -> {
                filteringMutableStateFlow.value = R.id.house_filter
            }
            R.id.penthouse_filter -> {
                filteringMutableStateFlow.value = R.id.penthouse_filter
            }
            R.id.duplex_filter -> {
                filteringMutableStateFlow.value = R.id.duplex_filter
            }
            R.id.flat_filter -> {
                filteringMutableStateFlow.value = R.id.flat_filter
            }
            R.id.loft_filter -> {
                filteringMutableStateFlow.value = R.id.loft_filter
            }
            else -> {
            }
        }
    }

    private fun transformEntityToViewState(entity: RealEstateEntityWithPhotos) = RealEstatesListViewState(id = entity.realEstateEntity.id,
        type = entity.realEstateEntity.type,
        price = context.getString(
            R.string.real_estate_price, java.text.NumberFormat.getIntegerInstance().format(entity.realEstateEntity.price.toInt())
        ),
        livingSpace = context.getString(
            R.string.square_foot, entity.realEstateEntity.livingSpace
        ),
        numberRooms = context.getString(
            R.string.number_rooms,
            entity.realEstateEntity.numberRooms,
            entity.realEstateEntity.numberBedroom,
            entity.realEstateEntity.numberBathroom
        ),
        description = entity.realEstateEntity.description,
        photos = entity.photos.map { PhotoViewState(Uri.parse(it.photo)) },
        address = context.getString(
            R.string.full_address,
            entity.realEstateEntity.gridZone,
            entity.realEstateEntity.streetName,
            entity.realEstateEntity.city,
            entity.realEstateEntity.state,
            entity.realEstateEntity.postalCode
        ),
        date = SimpleDateFormat("dd/MM/yyyy HH:mm").format(Date(entity.realEstateEntity.entryDate)),
        onClickedCallback = { id ->
            realEstatesViewAction.value = RealEstatesViewAction.Navigate.Detail(id)
        })

//    val realEstateLiveData = liveData(coroutineDispatcherProvider.io) {
//        sortingMutableStateFlow.flatMapLatest { sortField ->
//            getAllRealEstatesUseCase.invoke(sortField)
//        }.collect { entities ->
//            emit(
//                entities.map {
//                    transformEntityToViewState(it)
//                }
//            )
//        }
//    }

//    val realEstateLiveData = liveData(coroutineDispatcherProvider.io) {
//        sortingMutableStateFlow.collectLatest { sortField ->
//            getAllRealEstatesUseCase.invoke(sortField).collect { entities ->
//                emit(entities.map {
//                    transformEntityToViewState(it)
//                })
//            }
//        }
//    }

//    private val searchMutableStateFlow = MutableStateFlow<String?>(null)
//
//    val viewStateLiveDataVM: LiveData<List<String>> = liveData(coroutineDispatcherProvider.io) {
//        combine(
//            myRepo.getAllStuff(),
//            searchMutableStateFlow
//        ) { allStuff, search ->
//            if (search == null) {
//                emit(map(allStuff))
//            } else {
//                emit(
//                    map(
//                        allStuff.filter { it == search }
//                    )
//                )
//            }
//        }.collect()
//    }
//
//    val viewStateLiveDataSQL: LiveData<List<String>> = searchMutableStateFlow.flatMapLatest {
//        if (it == null) {
//            myRepo.getAllStuff()
//        } else {
//            myRepo.getStuffWithSomeSearch(it)
//        }
//    }.asLiveData(coroutineDispatcherProvider.IO)
//
//    private fun map(allStuff: List<String>): List<String> {
//        return allStuff
//    }
//
//    fun onSearchQueryChanged(search: String) {
//        searchMutableStateFlow.value = search
//    }
}