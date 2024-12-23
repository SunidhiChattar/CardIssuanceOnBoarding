package com.isu.prepaidcard.di

import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import com.isu.prepaidcard.domain.usecase.addoncard_usecase.AddOnCardApiImpl
import com.isu.prepaidcard.domain.usecase.addoncard_usecase.AddOnCardApiUseCase
import com.isu.prepaidcard.domain.usecase.balance_api_usecase.ViewCardBalanceUseCase
import com.isu.prepaidcard.domain.usecase.balance_api_usecase.ViewCardBalanceUseCaseImpl
import com.isu.prepaidcard.domain.usecase.changecardstatus_api_usecase.ChangeCardStatusImpl
import com.isu.prepaidcard.domain.usecase.changecardstatus_api_usecase.ChangeCardStatusUseCase
import com.isu.prepaidcard.domain.usecase.viewcvv_api_usecase.ViewCardCvvImpl
import com.isu.prepaidcard.domain.usecase.viewcvv_api_usecase.ViewCardCvvUseCase
import com.isu.prepaidcard.domain.usecase.carddata_api_usecase.ViewCardDataByMobNoApiUseCase
import com.isu.prepaidcard.domain.usecase.carddata_api_usecase.ViewCardDataByMobNoApiUseCaseImpl
import com.isu.prepaidcard.domain.usecase.carddata_api_usecase.ViewCardDataByRefIdImpl
import com.isu.prepaidcard.domain.usecase.carddata_api_usecase.ViewCardDataByRefIdUsecase
import com.isu.prepaidcard.domain.usecase.loadcard_api_usecase.LoadCardApiImpl
import com.isu.prepaidcard.domain.usecase.loadcard_api_usecase.LoadCardApiUseCase
import com.isu.prepaidcard.domain.usecase.mccstatus_apiusecase.MccStatusApiImpl
import com.isu.prepaidcard.domain.usecase.mccstatus_apiusecase.MccStatusApiUseCase
import com.isu.prepaidcard.domain.usecase.modifypin_usecase.ModifyPinApiImpl
import com.isu.prepaidcard.domain.usecase.modifypin_usecase.ModifyPinApiUseCase
import com.isu.prepaidcard.domain.usecase.orderphysical_api_usecase.OrderPhysicalApiImpl
import com.isu.prepaidcard.domain.usecase.orderphysical_api_usecase.OrderPhysicalApiUseCase
import com.isu.prepaidcard.domain.usecase.statement_usecases.DetailedStatementApiImpl
import com.isu.prepaidcard.domain.usecase.statement_usecases.DetailedStatementApiUseCase
import com.isu.prepaidcard.domain.usecase.statement_usecases.MiniStatementApiImpl
import com.isu.prepaidcard.domain.usecase.statement_usecases.MiniStatementApiUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * This module provides bindings for use cases and other domain-level components.
 *
 * @Module indicates that this class is a Dagger module.
 * @InstallIn(SingletonComponent::class) specifies that the bindings provided by this module will be installed into the singleton component.
 */
@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    /**
     * Provides an instance of `DemoApiUseCaseImpl` as a singleton.
     *
     * @param repository The `PrepaidCardRepository` instance.
     * @return A `DemoApiUseCase` instance.
     */
    @Singleton
    @Provides
    fun provideCardDataByRefIdUseCase(repository: PrepaidCardRepository): ViewCardDataByRefIdUsecase {
        return ViewCardDataByRefIdImpl(repository)
    }
    /**
     * Provides an instance of `ViewCardDataByMobNoApiUseCaseImpl` as a singleton.
     *
     * @param repository The `PrepaidCardRepository` instance.
     * @return A `ViewCardDataByMobNoApiUseCase` instance.
     */
    @Singleton
    @Provides
    fun provideCardDataByMobileNumberUseCase(repository: PrepaidCardRepository): ViewCardDataByMobNoApiUseCase {
        return ViewCardDataByMobNoApiUseCaseImpl(repository)
    }
    /**
     * Provides an instance of `ViewCardBalanceUseCaseImpl` as a singleton.
     *
     * @param repository The `PrepaidCardRepository` instance.
     * @return A `ViewCardBalanceUseCase` instance.
     */
    @Singleton
    @Provides
    fun provideCardBalance(repository: PrepaidCardRepository): ViewCardBalanceUseCase{
        return ViewCardBalanceUseCaseImpl(repository)
    }

    @Singleton
    @Provides
    fun provideLoadCard(repository: PrepaidCardRepository): LoadCardApiUseCase{
        return LoadCardApiImpl(repository)
    }

    @Singleton
    @Provides
    fun provideOrderPhysical(repository: PrepaidCardRepository): OrderPhysicalApiUseCase{
        return OrderPhysicalApiImpl(repository)
    }

    @Singleton
    @Provides
    fun provideChangeCardStatus(repository: PrepaidCardRepository): ChangeCardStatusUseCase {
        return ChangeCardStatusImpl(repository)
    }

    @Singleton
    @Provides
    fun provideViewCardCvv(repository: PrepaidCardRepository): ViewCardCvvUseCase {
        return ViewCardCvvImpl(repository)
    }
    @Singleton
    @Provides
    fun provideMccStatus(repository: PrepaidCardRepository): MccStatusApiUseCase{
        return MccStatusApiImpl(repository)
    }
    @Singleton
    @Provides
    fun provideModifyPin(
        repository: PrepaidCardRepository
    ): ModifyPinApiUseCase{
        return ModifyPinApiImpl(repository)
    }

    @Singleton
    @Provides
    fun provideAddOnCard(
        repository: PrepaidCardRepository
    ): AddOnCardApiUseCase{
        return AddOnCardApiImpl(repository)
    }

    @Singleton
    @Provides
    fun provideMiniStatement(
        repository: PrepaidCardRepository
    ): MiniStatementApiUseCase{
        return MiniStatementApiImpl(repository)
    }

    @Singleton
    @Provides
    fun provideDetailedStatement(
        repository: PrepaidCardRepository
    ): DetailedStatementApiUseCase{
        return DetailedStatementApiImpl(repository)
    }
}