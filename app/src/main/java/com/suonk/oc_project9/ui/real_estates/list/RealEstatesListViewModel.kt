package com.suonk.oc_project9.ui.real_estates.list

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.domain.real_estate.get.GetAllRealEstatesUseCase
import com.suonk.oc_project9.domain.real_estate.filter.SearchRealEstateUseCase
import com.suonk.oc_project9.domain.real_estate.filter.FilterRealEstateUseCase
import com.suonk.oc_project9.domain.real_estate.filter.GetRealEstateSearchUseCase
import com.suonk.oc_project9.domain.real_estate.filter.SortReaEstateUseCase
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
    private val getRealEstateSearchUseCase: GetRealEstateSearchUseCase,
    private val searchRepository: SearchRepository,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val searchRealEstateUseCase: SearchRealEstateUseCase,
    private val filterRealEstateUseCase: FilterRealEstateUseCase,
    private val sortReaEstateUseCase: SortReaEstateUseCase,
    private val application: Application,
) : ViewModel() {

    private val sortingMutableStateFlow = MutableStateFlow(Sorting.DATE_ASC)
    private val filteringMutableStateFlow = MutableStateFlow(R.id.remove_filter)

    val toastMessageSingleLiveEvent = SingleLiveEvent<String>()

    val realEstatesViewAction = SingleLiveEvent<RealEstatesViewAction>()

    sealed class RealEstatesViewAction {
        sealed class Navigate : RealEstatesViewAction() {
            data class Detail(val realEstateId: Long) : Navigate()
        }
    }

    val realEstateLiveData: LiveData<List<RealEstatesListViewState>> = liveData(coroutineDispatcherProvider.io) {
        combine(
            getAllRealEstatesUseCase.invoke(),
            sortingMutableStateFlow,
            filteringMutableStateFlow,
            getRealEstateSearchUseCase.invoke(),
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

    private fun checkIfTwoFieldsAreFilled(firstField: String, secondField: String): Boolean {
        return !(firstField.isNotBlank() && firstField.isNotEmpty() && secondField.isEmpty() || secondField.isBlank() || secondField.isNotBlank() && secondField.isNotEmpty() && firstField.isEmpty() || firstField.isBlank())
    }

    private fun transformEntityToViewState(entity: RealEstateEntityWithPhotos): RealEstatesListViewState {
        return RealEstatesListViewState(id = entity.realEstateEntity.id,
            type = entity.realEstateEntity.type,
            price = application.getString(
                R.string.real_estate_price, entity.realEstateEntity.price
            ),
            priceValue = entity.realEstateEntity.price,
            numberRooms = application.getString(
                R.string.number_rooms,
                entity.realEstateEntity.numberRooms,
                entity.realEstateEntity.numberBedroom,
                entity.realEstateEntity.numberBathroom
            ),
            numberRoomsValue = entity.realEstateEntity.numberRooms,
            livingSpace = application.getString(R.string.square_meter, entity.realEstateEntity.livingSpace),
            livingSpaceValue = entity.realEstateEntity.livingSpace,
            description = entity.realEstateEntity.description,
            photos = entity.photos.map { ListPhotoViewState(it.photo) }.distinct(),
            address = application.getString(
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
        searchRealEstateUseCase.invoke(search)
    }

    fun onSortedOrFilterClicked(itemId: Int) {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val filterFlow = filterRealEstateUseCase.invoke(itemId).firstOrNull() ?: R.id.remove_filter // TODO Kenzy use unidirectionnal flows
            val sortFlow = sortReaEstateUseCase.invoke(itemId).firstOrNull() ?: Sorting.DATE_ASC
            filteringMutableStateFlow.tryEmit(filterFlow)
            sortingMutableStateFlow.tryEmit(sortFlow)
        }
    }
}