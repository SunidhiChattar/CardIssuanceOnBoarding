package com.isu.common.di

/*
import com.isu.common.utils.firebase.FirebaseStorageManager*/
import android.content.Context
import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.google.gson.Gson
import com.isu.apitracker.presentation.ApiInterceptor
import com.isu.common.BuildConfig
import com.isu.common.events.ShowSnackBarEvent
import com.isu.common.events.SnackBarType
import com.isu.common.models.HeaderSecretGenerationModel
import com.isu.common.navigation.NavigationEvent
import com.isu.common.navigation.SplashScreens
import com.isu.common.utils.EncryptDecryptDecoder
import com.isu.common.utils.EncryptDecryptDecoderCBC
import com.isu.common.utils.GlobalVariables
import com.isu.common.utils.UiText
import com.isu.common.utils.datastore.DataStoreManager
import com.isu.common.utils.datastore.DataStoreManagerImpl
import com.isu.common.utils.datastore.PreferencesKeys
import com.isu.common.utils.encryptdecrypt.EncryptDecryptCBC
import com.isu.common.utils.encryptdecrypt.LogOutInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataModule {
    @Module
    @InstallIn(SingletonComponent::class)
    object AppModule {

        @Singleton
        @Provides
        fun provideCoroutineScope(): CoroutineScope {
            return CoroutineScope(SupervisorJob() + Dispatchers.IO)
        }
    }
    /**
     * Provides an OkHttpClient with custom configuration, including the use of a specific X.509 certificate.
     *
     * @return OkHttpClient instance.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        scope: CoroutineScope,
        dataStoreManager: DataStoreManager
    ): OkHttpClient {
        // Build and return OkHttpClient with custom configuration
        val logging = HttpLoggingInterceptor().apply {
            setLevel(HttpLoggingInterceptor.Level.BODY)
        }

        var authToken = ""
        var clientSecret=""
        var clientId=""

        scope.launch {
            authToken = dataStoreManager.getPreferenceValue(PreferencesKeys.AUTH_TOKEN).toString()
            clientSecret=dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_SECRET).toString()
            clientId=dataStoreManager.getPreferenceValue(PreferencesKeys.CLIENT_ID).toString()
        }
        Log.d("DATASTORE", "provideOkHttpClient:${clientSecret} ")

        return OkHttpClient.Builder().connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .hostnameVerifier { _, _ -> true }
            .addInterceptor(LogOutInterceptor {
                scope.launch {
                    NavigationEvent.helper.navigateTo(SplashScreens.LoadingScreen)
                    ShowSnackBarEvent.helper.emit(
                        ShowSnackBarEvent.show(
                            SnackBarType.ErrorSnackBar,
                            UiText.DynamicString("Token is expired")
                        )
                    )
                }
            })// Bypass hostname verification
            .addInterceptor { chain ->
                chain.request().newBuilder()
                    .build()
                    .let(chain::proceed)
            }.addInterceptor { chain ->
                val request = chain.request()

                val newRequestBuilder = request.newBuilder()
                val newRequest = if (
                    request.url.toString()
                        .contains("/user/card_status_check".toRegex()) ||
                    request.url.toString()
                        .contains("card/resend_otp_generation".toRegex()) ||
                    request.url.toString()
                        .contains("/upi/initiate-dynamic-transaction".toRegex()) ||
                    request.url.toString()
                        .contains("/user/self_signup".toRegex())
                    ) {

                    val headerRequest=
                        HeaderSecretGenerationModel(epoch = System.currentTimeMillis().toString(), clientSecret = clientSecret)
                    val headerRequestJson= Gson().toJson(headerRequest)
                    val headerSecret=
                        EncryptDecryptCBC.encryptRequest(headerRequestJson.toByteArray(), key = "W30gz/ZGS/BbKI5YLqSbk/SqiDhOenlOb3cpk1gRy2E=")

                    newRequestBuilder.removeHeader("Authorization").addHeader(
                        "header_secrets",
                        headerSecret.toString()
                    ).addHeader("client_id",clientId).build()
                    }
                else if (request.url.toString()
                        .contains("user-registration/user/login".toRegex())
                ) {
                    newRequestBuilder.removeHeader("Authorization").addHeader(
                        "Authorization",
                        "Basic aXN1LWZpbm8tY2xpZW50OmlzdS1maW5vLXBhc3N3b3Jk"
                    ).build()
                } else if (request.url.toString()
                        .contains("zendesk_form/_search".toRegex())
                ) {
                    newRequestBuilder.removeHeader("Authorization").addHeader(
                        "Authorization",
                        "Basic ZWxhc3RpYzpUQWhJamJ4U2RzRzRRRDY3WWVmZTZQdzg="
                    ).build()
                } else {
                    val headerRequest=
                        HeaderSecretGenerationModel(epoch = System.currentTimeMillis().toString(), clientSecret = clientSecret)
                    val headerRequestJson= Gson().toJson(headerRequest)
                    val headerSecret=
                        EncryptDecryptCBC.encryptRequest(headerRequestJson.toByteArray(), key = "W30gz/ZGS/BbKI5YLqSbk/SqiDhOenlOb3cpk1gRy2E=")

                    newRequestBuilder.addHeader(
                        "header_secrets",
                        headerSecret.toString()
                    ).addHeader("client_id",clientId).build()

                }

                chain.proceed(newRequest)
            }

            .retryOnConnectionFailure(true)
            .protocols(listOf(Protocol.HTTP_1_1))
            .addInterceptor(logging)
            .addInterceptor(HttpLoggingInterceptor())
            .addInterceptor(
                ApiInterceptor(
                    context, listOf(EncryptDecryptDecoderCBC()), listOf("")
                )
            )
            .build()

    }

    val CARD_PREFERNCE = "Card store preference"

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            corruptionHandler = ReplaceFileCorruptionHandler(
                produceNewData = { emptyPreferences() }
            ),
            migrations = listOf(SharedPreferencesMigration(appContext, CARD_PREFERNCE)),
            scope = CoroutineScope(Dispatchers.IO + SupervisorJob()),
            produceFile = { appContext.preferencesDataStoreFile(CARD_PREFERNCE) }
        )
    }

    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context = context

    @Provides
    @Singleton
    fun provideDataStoreManager(context: Context): DataStoreManager {
        return DataStoreManagerImpl(context)
    }

    /*   @Provides
       @Singleton
       fun provideFireBaseManager(context: Context): FirebaseStorageManager {

           return FirebaseStorageManager(context)
       }*/


}