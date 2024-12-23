package com.isu.prepaidcard.di

import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.prepaidcard.data.mappers.demo_response_mapper.DemoResponseMapper
import com.isu.prepaidcard.data.mappers.demo_response_mapper.DemoResponseMapperImpl
import com.isu.prepaidcard.data.remote.PrepaidCardApiService
import com.isu.prepaidcard.data.repository.PrepaidCardRepositoryImpl
import com.isu.prepaidcard.domain.repository.PrepaidCardRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Singleton
    @Provides
    fun providePrepaidCardApiService(client: OkHttpClient): PrepaidCardApiService =
        Retrofit.Builder()
            .baseUrl("https://prepaidcard-gateway.iserveu.online")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PrepaidCardApiService::class.java)

    @Singleton
    @Provides
    fun provideDemoResponseMapper(): DemoResponseMapper = DemoResponseMapperImpl()

    @Singleton
    @Provides
    fun provideRepository(

        prepaidCardApiService: PrepaidCardApiService,
        demoResponseMapper: DemoResponseMapper
    ): PrepaidCardRepository = PrepaidCardRepositoryImpl(prepaidCardApiService = prepaidCardApiService, encryptDecrypt = EncryptDecrypt, demoResponseMapper = demoResponseMapper)
}


class MyConverterFactory : Converter.Factory() {
    override fun requestBodyConverter(
        type: Type,
        parameterAnnotations: Array<out Annotation>,
        methodAnnotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<*, RequestBody>? {
        return super.requestBodyConverter(type, parameterAnnotations, methodAnnotations, retrofit)
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit,
    ): Converter<ResponseBody, *>? {
        return super.responseBodyConverter(type, annotations, retrofit)
    }

}