package  di

import com.isu.authentication.domain.usecases.statuscheck_api_usecase.StatusCheckImpl
import com.isu.authentication.domain.usecases.statuscheck_api_usecase.StatusCheckUseCase
import  com.isu.authentication.domain.repo.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
  /*  @Singleton
    @Provides
    fun statusCheckUseCase(repository: AuthRepository): StatusCheckUseCase {
        return StatusCheckImpl(repository)
    }*/
}