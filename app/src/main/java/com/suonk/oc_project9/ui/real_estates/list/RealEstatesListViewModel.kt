package com.suonk.oc_project9.ui.real_estates.list

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.domain.real_estate.get.GetAllRealEstatesUseCase
import com.suonk.oc_project9.domain.real_estate.filter.SearchRealEstateUseCase
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntityWithPhotos
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.EquatableCallback
import com.suonk.oc_project9.utils.SingleLiveEvent
import com.suonk.oc_project9.utils.sort.Sorting
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class RealEstatesListViewModel @Inject constructor(
    private val getAllRealEstatesUseCase: GetAllRealEstatesUseCase,
    private val searchRepository: SearchRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val searchRealEstateUseCase: SearchRealEstateUseCase,
    @ApplicationContext private val context: Context
) : ViewModel() {

    private val sortingMutableStateFlow = MutableStateFlow(Sorting.DATE_ASC)
    private val filteringMutableStateFlow = MutableStateFlow(R.id.remove_filter)
    private var searchMutableStateFlow = MutableStateFlow("")

    val toastMessageSingleLiveEvent = SingleLiveEvent<String>()

    val realEstatesViewAction = SingleLiveEvent<RealEstatesViewAction>()

    sealed class RealEstatesViewAction {
        sealed class Navigate : RealEstatesViewAction() {
            data class Detail(val realEstateId: Long) : Navigate()
        }
    }

    init {
        viewModelScope.launch(coroutineDispatcherProvider.io) {

        }
    }

    val realEstateLiveData: LiveData<List<RealEstatesListViewState>> = liveData(coroutineDispatcherProvider.io) {
        combine(
            getAllRealEstatesUseCase.invoke(),
            sortingMutableStateFlow,
            filteringMutableStateFlow,
            searchMutableStateFlow,
            searchRepository.getCurrentFilterParametersFlow()
        ) { entities: List<RealEstateEntityWithPhotos>, sorting, filterId, search, filters ->
            val list = entities.asSequence().filter { realEstate ->
                filters.all { it.isMatching(realEstate) }
            }.sortedWith(sorting.comparator).map {
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
            }.filter {
                if (search != "" || search != " ") {
                    it.description.lowercase().contains(search.lowercase()) || it.address.lowercase()
                        .contains(search.lowercase()) || it.price.lowercase().contains(search.lowercase()) || it.type.lowercase()
                        .contains(search.lowercase())
                } else {
                    true
                }
            }.toList()
            emit(list)
        }.collect()
    }

    fun onSortedOrFilterClicked(itemId: Int) {
        when (itemId) {
            R.id.sort_by_date_asc -> sortingMutableStateFlow.value = Sorting.DATE_ASC
            R.id.sort_by_price_asc -> sortingMutableStateFlow.value = Sorting.PRICE_ASC
            R.id.sort_by_living_space_asc -> sortingMutableStateFlow.value = Sorting.LIVING_SPACE_ASC
            R.id.sort_by_rooms_number_asc -> sortingMutableStateFlow.value = Sorting.ROOMS_NUMBER_ASC
            R.id.sort_by_date_desc -> sortingMutableStateFlow.value = Sorting.DATE_DESC
            R.id.sort_by_price_desc -> sortingMutableStateFlow.value = Sorting.PRICE_DESC
            R.id.sort_by_living_space_desc -> sortingMutableStateFlow.value = Sorting.LIVING_SPACE_DESC
            R.id.sort_by_rooms_number_desc -> sortingMutableStateFlow.value = Sorting.ROOMS_NUMBER_DESC
            R.id.remove_filter -> {
                searchRepository.reset()
                filteringMutableStateFlow.value = R.id.remove_filter
            }
            R.id.house_filter -> filteringMutableStateFlow.value = R.id.house_filter
            R.id.penthouse_filter -> filteringMutableStateFlow.value = R.id.penthouse_filter
            R.id.duplex_filter -> filteringMutableStateFlow.value = R.id.duplex_filter
            R.id.flat_filter -> filteringMutableStateFlow.value = R.id.flat_filter
            R.id.loft_filter -> filteringMutableStateFlow.value = R.id.loft_filter
            else -> Unit
        }
    }

    private fun checkIfTwoFieldsAreFilled(firstField: String, secondField: String): Boolean {
        return !(firstField.isNotBlank() && firstField.isNotEmpty() && secondField.isEmpty() || secondField.isBlank() || secondField.isNotBlank() && secondField.isNotEmpty() && firstField.isEmpty() || firstField.isBlank())
    }

    private fun transformEntityToViewState(entity: RealEstateEntityWithPhotos): RealEstatesListViewState {
        return RealEstatesListViewState(id = entity.realEstateEntity.id,
            type = entity.realEstateEntity.type,
            price = context.getString(
                R.string.real_estate_price, entity.realEstateEntity.price
            ),
            priceValue = entity.realEstateEntity.price,
            numberRooms = context.getString(
                R.string.number_rooms,
                entity.realEstateEntity.numberRooms,
                entity.realEstateEntity.numberBedroom,
                entity.realEstateEntity.numberBathroom
            ),
            numberRoomsValue = entity.realEstateEntity.numberRooms,
            livingSpace = context.getString(R.string.square_meter, entity.realEstateEntity.livingSpace),
            livingSpaceValue = entity.realEstateEntity.livingSpace,
            description = entity.realEstateEntity.description,
            photos = entity.photos.map { ListPhotoViewState(it.photo) }.distinct(),
            address = context.getString(
                R.string.full_address,
                entity.realEstateEntity.gridZone,
                entity.realEstateEntity.streetName,
                entity.realEstateEntity.city,
                entity.realEstateEntity.state,
                entity.realEstateEntity.postalCode
            ),
            entryDate = entity.realEstateEntity.entryDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            saleDate = entity.realEstateEntity.saleDate?.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")).orEmpty(),
            isSold = entity.realEstateEntity.saleDate != null,
            onClickedCallback = EquatableCallback {
                realEstatesViewAction.value = RealEstatesViewAction.Navigate.Detail(entity.realEstateEntity.id)
            })
    }

    fun onSearchQueryChanged(search: String) {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val searchFlow = searchRealEstateUseCase.invoke(search).firstOrNull() ?: ""
            searchMutableStateFlow.tryEmit(searchFlow)

            
        }
    }

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
}