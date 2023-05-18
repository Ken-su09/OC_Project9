package com.suonk.oc_project9.di

import com.suonk.oc_project9.domain.CurrentPositionRepository
import com.suonk.oc_project9.domain.PhotoRepository
import com.suonk.oc_project9.domain.PlacesRepository
import com.suonk.oc_project9.domain.RealEstateRepository
import com.suonk.oc_project9.domain.SearchRepository
import com.suonk.oc_project9.model.database.data.CurrentPositionRepositoryImpl
import com.suonk.oc_project9.model.database.data.PhotoRepositoryImpl
import com.suonk.oc_project9.model.database.data.PlacesRepositoryImpl
import com.suonk.oc_project9.model.database.data.RealEstateRepositoryImpl
import com.suonk.oc_project9.model.database.data.SearchRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
abstract class DataBindingsModule {

    @Binds
    @Singleton
    abstract fun bindRealEstateRepository(impl: RealEstateRepositoryImpl): RealEstateRepository

    @Binds
    @Singleton
    abstract fun bindPhotoRepositoryImpl(impl: PhotoRepositoryImpl): PhotoRepository

    @Binds
    @Singleton
    abstract fun bindPlacesRepositoryImpl(impl: PlacesRepositoryImpl): PlacesRepository

    @Binds
    @Singleton
    abstract fun bindSearchRepositoryImpl(impl: SearchRepositoryImpl): SearchRepository

    @Binds
    @Singleton
    abstract fun bindCurrentPositionRepositoryImpl(impl: CurrentPositionRepositoryImpl): CurrentPositionRepository
}