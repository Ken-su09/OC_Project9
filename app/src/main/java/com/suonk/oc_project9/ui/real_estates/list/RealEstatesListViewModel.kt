package com.suonk.oc_project9.ui.real_estates.list

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import com.suonk.oc_project9.R
import com.suonk.oc_project9.domain.real_estate.GetAllRealEstatesUseCase
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntityWithPhotos
import com.suonk.oc_project9.utils.CoroutineDispatcherProvider
import com.suonk.oc_project9.utils.SingleLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class RealEstatesListViewModel @Inject constructor(
    private val getAllRealEstatesUseCase: GetAllRealEstatesUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    val realEstatesViewAction = SingleLiveEvent<RealEstatesViewAction>()

    sealed class RealEstatesViewAction {
        sealed class Navigate : RealEstatesViewAction() {
            data class Detail(
                val realEstateId: Long
            ) : Navigate()
        }
    }

    val realEstateLiveData = liveData(coroutineDispatcherProvider.io) {
        getAllRealEstatesUseCase.invoke().collect { entities ->
            emit(
                entities.map {
                    transformEntityToViewState(it)
                }
            )
        }
    }

    private fun transformEntityToViewState(entity: RealEstateEntityWithPhotos) =
        RealEstatesListViewState(
            id = entity.realEstateEntity.id,
            type = entity.realEstateEntity.type,
            price = context.getString(
                R.string.real_estate_price,
                java.text.NumberFormat.getIntegerInstance()
                    .format(entity.realEstateEntity.price.toInt())
            ),
            livingSpace = context.getString(
                R.string.square_foot,
                entity.realEstateEntity.livingSpace
            ),
            numberRooms = context.getString(
                R.string.number_rooms,
                entity.realEstateEntity.numberRooms,
                entity.realEstateEntity.numberBedroom,
                entity.realEstateEntity.numberBathroom
            ),
            description = entity.realEstateEntity.description,
            photos = entity.photos.map { it.url },
            address = entity.realEstateEntity.fullAddress,
            onClickedCallback = { id ->

            }
        )


    fun convertSquareMetreToSquareFoot(value: Double): Double {
        return value * 10.763867361111
    }

    fun convertSquareFootToSquareMetre(value: Double): Double {
        return value / 10.763867361111
    }

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