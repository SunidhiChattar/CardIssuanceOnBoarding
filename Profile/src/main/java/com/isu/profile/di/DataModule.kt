package com.isu.profile.di

import com.isu.profile.data.remote.src.ProfileApiService
import com.isu.profile.data.repositoryImplementation.ProfileRepositoryImplementation
import com.isu.profile.domain.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {


    @Singleton
    @Provides
    fun providePrepaidCardApiService(retrofit: Retrofit): ProfileApiService = retrofit
            .create(ProfileApiService::class.java)



    @Singleton
    @Provides
    fun provideRepository(
        profileApiService: ProfileApiService
    ): ProfileRepository = ProfileRepositoryImplementation(profileApiService)

}