package com.suonk.oc_project9.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.suonk.oc_project9.model.database.AppDatabase
import com.suonk.oc_project9.model.database.dao.PhotoDao
import com.suonk.oc_project9.model.database.dao.RealEstateDao
import com.suonk.oc_project9.model.database.data.entities.PhotoEntity
import com.suonk.oc_project9.model.database.data.entities.RealEstateEntity
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Provider
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        providerDao: Provider<RealEstateDao>,
        photoDao: Provider<PhotoDao>
    ) =
        Room.databaseBuilder(context, AppDatabase::class.java, "app_database")
            .allowMainThreadQueries()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    prepopulateDatabase(providerDao.get(), photoDao.get())
                }

                private fun prepopulateDatabase(estateDao: RealEstateDao, photoDao: PhotoDao) {
                    CoroutineScope(Dispatchers.IO).launch {
                        estateDao.insertRealEstate(
                            RealEstateEntity(
                                id = 1L,
                                type = "Penthouse",
                                price = 29872000.0,
                                livingSpace = 8072.900,
                                numberRooms = 8,
                                numberBedroom = 4,
                                numberBathroom = 2,
                                description = "Anchored by a vast marble gallery with sweeping staircase, ",
                                "55 West 25th Street, New York, NY, 10010",
                                "10010",
                                "NY",
                                "New York",
                                "25th Street",
                                "55 West",
                                "",
                                "Available",
                                System.currentTimeMillis(),
                                null,
                                1L,
                            )
                        )
                        estateDao.insertRealEstate(
                            RealEstateEntity(
                                2L,
                                "Apartment",
                                10995000.0,
                                12916.640,
                                9,
                                3,
                                3,
                                "",
                                "23 W 24th Street, New York, NY 10010, United States",
                                "10010",
                                "NY",
                                "New York",
                                "24th Street",
                                "23 W",
                                "",
                                "Available",
                                System.currentTimeMillis(),
                                null,
                                2L
                            )
                        )
                        estateDao.insertRealEstate(
                            RealEstateEntity(
                                3L,
                                "Duplex",
                                15995000.0,
                                11756.9652,
                                11,
                                3,
                                3,
                                "",
                                "156-0-156-34 25th Ave, Whitestone, NY 11357",
                                "11357",
                                "NY",
                                "Whitestone",
                                "25th Ave",
                                "156-0-156-34",
                                "",
                                "Available",
                                System.currentTimeMillis(),
                                null,
                                2L
                            )
                        )

                        photoDao.insertPhoto(
                            PhotoEntity(
                                0,
                                1L,
                                "https://photos.zillowstatic.com/fp/390793abc077faf2df87690ad3f9940c-se_extra_large_1500_800.webp"
                            )
                        )
                        photoDao.insertPhoto(
                            PhotoEntity(
                                0,
                                1L,
                                "https://photos.zillowstatic.com/fp/344beadccb742f876c027673bfccccf2-se_extra_large_1500_800.webp"
                            )
                        )
                        photoDao.insertPhoto(
                            PhotoEntity(
                                0,
                                1L,
                                "https://photos.zillowstatic.com/fp/9d28f752e5f90e54d151a41114db6040-se_extra_large_1500_800.webp"
                            )
                        )
                        photoDao.insertPhoto(
                            PhotoEntity(
                                0,
                                2L,
                                "https://photos.zillowstatic.com/fp/cfef7cd3d01074fe7ac38e6fdfd0c657-se_extra_large_1500_800.webp"
                            )
                        )
                        photoDao.insertPhoto(
                            PhotoEntity(
                                0,
                                2L,
                                "https://photos.zillowstatic.com/fp/f6def35d74b83ffa18e93d5bd56c6390-se_extra_large_1500_800.webp"
                            )
                        )
                        photoDao.insertPhoto(
                            PhotoEntity(
                                0,
                                3L,
                                "https://photonet.hotpads.com/search/listingPhoto/Postlets/1efkb7qsyr3d4/0002_1812003620_medium.jpg"
                            )
                        )
                        photoDao.insertPhoto(
                            PhotoEntity(
                                0,
                                3L,
                                "https://www.trulia.com/pictures/thumbs_6/zillowstatic/fp/77ff7feecc004404d198666792ae7add-full.jpg"
                            )
                        )
                        photoDao.insertPhoto(
                            PhotoEntity(
                                0,
                                3L,
                                "https://www.trulia.com/pictures/thumbs_5/zillowstatic/fp/c5839d5d7fee68c638c64cf34b6f033d-full.jpg"
                            )
                        )
                    }
                }
            })
            .addMigrations()
            .build()

    @Provides
    @Singleton
    fun provideRealEstateDao(database: AppDatabase) = database.realEstateDao()

    @Provides
    @Singleton
    fun providePhotoDao(database: AppDatabase) = database.photoDao()
}