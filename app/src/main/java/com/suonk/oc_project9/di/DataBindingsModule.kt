package com.suonk.oc_project9.di

import com.suonk.oc_project9.domain.*
import com.suonk.oc_project9.model.database.data.*
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

    @Binds
    @Singleton
    abstract fun bindCurrentRealEstateIdRepositoryImpl(impl: CurrentRealEstateIdRepositoryImpl): CurrentRealEstateIdRepository

    @Binds
    @Singleton
    abstract fun bindLoanSimulatorRepositoryImpl(impl: LoanSimulatorRepositoryImpl): LoanSimulatorRepository
}