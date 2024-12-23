package  di
import  data.remote.networkservice.AuthApiService
import  com.isu.authentication.data.repositoryimplementation.AuthRepoImpl
import  com.isu.authentication.domain.repo.AuthRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit
            .Builder()
            .client(okHttpClient)
            .baseUrl("https://prepaidcard-gateway.iserveu.online")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    @Singleton
    @Provides
    fun providePrepaidCardApiService(retrofit: Retrofit): AuthApiService = retrofit
            .create(AuthApiService::class.java)



    @Singleton
    @Provides
    fun provideRepository(
        prepaidCardApiService: AuthApiService,
    ): AuthRepository = AuthRepoImpl(prepaidCardApiService)

}