package com.suonk.oc_project9.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.suonk.oc_project9.api.PlacesApiHolder
import com.suonk.oc_project9.api.PlacesApiService
import com.suonk.oc_project9.model.database.AppDatabase
import com.suonk.oc_project9.model.database.dao.PhotoDao
import com.suonk.oc_project9.model.database.dao.RealEstateDao
import com.suonk.oc_project9.model.database.data.entities.real_estate.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.real_estate.RealEstateEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context, providerDao: Provider<RealEstateDao>, photoDao: Provider<PhotoDao>
    ) = Room.databaseBuilder(context, AppDatabase::class.java, "app_database").allowMainThreadQueries()
        .addCallback(object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)

                prepopulateDatabase(providerDao.get(), photoDao.get())
            }

            private fun prepopulateDatabase(estateDao: RealEstateDao, photoDao: PhotoDao) {
                CoroutineScope(Dispatchers.IO).launch {
                    estateDao.upsertRealEstate(
                        RealEstateEntity(
                            id = 1L,
                            type = "Penthouse",
                            price = BigDecimal(29872000.0),
                            livingSpace = 8072.900,
                            numberRooms = 8,
                            numberBedroom = 4,
                            numberBathroom = 2,
                            description = "Anchored by a vast marble gallery with sweeping staircase, ",
                            postalCode = "10010",
                            state = "NY",
                            city = "New York",
                            streetName = "25th Street",
                            gridZone = "55 West",
                            status = "Available",
                            entryDate = LocalDateTime.now(),
                            saleDate = null,
                            latitude = 40.744080,
                            longitude = -73.991302,
                            agentInChargeId = 1L,
                        )
                    )

                    photoDao.insertPhoto(PhotoEntity(0, 1L, "content://media/external/images/media/68287"))
                    photoDao.insertPhoto(PhotoEntity(0, 1L, "content://media/external/images/media/68288"))
                    photoDao.insertPhoto(PhotoEntity(0, 1L, "content://media/external/images/media/68289"))


                    estateDao.upsertRealEstate(
                        RealEstateEntity(
                            id = 2L,
                            type = "Apartment",
                            price = BigDecimal(10495.0),
                            livingSpace = 1410.0,
                            numberRooms = 4,
                            numberBedroom = 2,
                            numberBathroom = 1,
                            description = "Welcome to this two bedroom Park Avenue residence on the luxurious Upper East Side. The apartment features arched windows, stainless steel appliances and custom white hardwood floors",
                            postalCode = "10028",
                            state = "NY",
                            city = "New York",
                            streetName = "920 Park Avenue",
                            gridZone = "920 Park",
                            status = "Available",
                            entryDate = LocalDateTime.now(),
                            saleDate = null,
                            latitude = 40.776670,
                            longitude = -73.960240,
                            agentInChargeId = 2L
                        )
                    )

                    photoDao.insertPhoto(PhotoEntity(0, 2L, "content://media/external/images/media/68282"))
                    photoDao.insertPhoto(PhotoEntity(0, 2L, "content://media/external/images/media/68281"))
                    photoDao.insertPhoto(PhotoEntity(0, 2L, "content://media/external/images/media/68280"))
                    photoDao.insertPhoto(PhotoEntity(0, 2L, "content://media/external/images/media/68279"))
                    photoDao.insertPhoto(PhotoEntity(0, 2L, "content://media/external/images/media/68278"))
                    photoDao.insertPhoto(PhotoEntity(0, 2L, "content://media/external/images/media/68277"))
                    photoDao.insertPhoto(PhotoEntity(0, 2L, "content://media/external/images/media/68276"))


//                    estateDao.upsertRealEstate(
//                        RealEstateEntity(
//                            id = 3L,
//                            type = "House",
//                            price = 15995000.0,
//                            livingSpace = 11756.9652,
//                            numberRooms = 11,
//                            numberBedroom = 3,
//                            numberBathroom = 3,
//                            description = "",
//                            postalCode = "11357",
//                            state = "NY",
//                            city = "Whitestone",
//                            streetName = "25th Ave",
//                            gridZone = "156-0-156-34",
//                            pointOfInterest = "",
//                            status = "Available",
//                            entryDate = System.currentTimeMillis(),
//                            saleDate = null,
//                            latitude = 40.775070,
//                            longitude = -73.806640,
//                            agentInChargeId = 2L
//                        )
//                    )
//                    photoDao.insertPhoto(PhotoEntity(0, 3L, "content://media/external/images/media/68278"))
//                    photoDao.insertPhoto(PhotoEntity(0, 3L, "content://media/external/images/media/68277"))
//                    photoDao.insertPhoto(PhotoEntity(0, 3L, "content://media/external/images/media/68276"))

                }
            }
        }).addMigrations().build()


    @Provides
    @Singleton
    fun providePlacesApiService(): PlacesApiService {
        return PlacesApiHolder.getInstance()
    }

    @Provides
    @Singleton
    fun provideClock(): Clock {
        return Clock.systemDefaultZone()
    }

//    RealEstateEntity(
//    id = ,
//    type = "",
//    price = ,
//    livingSpace = ,
//    numberRooms = ,
//    numberBedroom = ,
//    numberBathroom = ,
//    description = ,
//    postalCode = ,
//    state = ,
//    city = ,
//    streetName = ,
//    gridZone = ,
//    pointOfInterest = ,
//    status = ,
//    entryDate = ,
//    saleDate = ,
//    latitude = ,
//    longitude = ,
//    agentInChargeId = ,
//    )

    @Provides
    @Singleton
    fun provideRealEstateDao(database: AppDatabase) = database.realEstateDao()

    @Provides
    @Singleton
    fun providePhotoDao(database: AppDatabase) = database.photoDao()
}