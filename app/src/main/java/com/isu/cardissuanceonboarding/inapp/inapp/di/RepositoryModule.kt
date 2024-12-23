package com.isu.cardissuanceonboarding.inapp.inapp.di

import com.isu.cardissuanceonboarding.inapp.inapp.domain.usecase.GetLatestVersionUseCase
import com.isu.cardissuanceonboarding.inapp.inapp.domain.usecase.SendAppDetailsToApiUseCase
import com.isu.cardissuanceonboarding.inapp.inapp.domain.usecase.UseCases
import com.isu.cardissuanceonboarding.inapp.inapp.data.ApiService
import com.isu.cardissuanceonboarding.inapp.inapp.data.repository.RepositoryImplementation
import com.isu.cardissuanceonboarding.inapp.inapp.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dagger Hilt module providing dependencies related to repositories and use cases.
 */
@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    /**
     * Provides a singleton instance of the Repository interface.
     *
     * @param apiService The ApiService instance for network operations.
     * @return Repository instance.
     */
    @Singleton
    @Provides
    fun provideRepository(apiService: ApiService): Repository {
        return RepositoryImplementation(apiService)
    }

    /**
     * Provides a singleton instance of the UseCase class, initializing it with various use cases.
     *
     * @param repository The Repository instance.
     * @return UseCase instance.
     */
    @Singleton
    @Provides
    fun provideUseCase(repository: Repository): UseCases {
        return UseCases(
          getLatestVersionUseCase = GetLatestVersionUseCase(repository),
            sendAppDetailsToApiUseCase = SendAppDetailsToApiUseCase(repository)
        )
    }
}