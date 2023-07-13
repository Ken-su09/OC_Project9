package com.suonk.oc_project9.di

import com.suonk.oc_project9.domain.LoanSimulatorRepository
import com.suonk.oc_project9.model.database.data.repositories.LoanSimulatorRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@InstallIn(ViewModelComponent::class)
@Module
abstract class DataViewModelBindingsModule {

    @Binds
    @ViewModelScoped
    abstract fun bindLoanSimulatorRepositoryImpl(impl: LoanSimulatorRepositoryImpl): LoanSimulatorRepository
}