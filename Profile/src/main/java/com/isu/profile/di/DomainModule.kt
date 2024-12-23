package  com.isu.profile.di


import com.isu.common.utils.ApiUseCase
import com.isu.profile.data.remote.model.request.AddCommentsRequest
import com.isu.profile.data.remote.model.request.AuthTokenData
import com.isu.profile.data.remote.model.request.ChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.request.FetchPinCodeDataRequest
import com.isu.profile.data.remote.model.request.GenerateOTPChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.request.GetFormRequest
import com.isu.profile.data.remote.model.request.RaiseAtTicketRequest
import com.isu.profile.data.remote.model.request.ShowTicketCommentRequest
import com.isu.profile.data.remote.model.request.UpdateTicketStatusRequest
import com.isu.profile.data.remote.model.response.ChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.FetchPinCodeDataResponse
import com.isu.profile.data.remote.model.response.GenerateOTPChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.RaiseATicketResponse
import com.isu.profile.data.remote.model.response.ShowTicketCommentResponse
import com.isu.profile.data.remote.model.response.ShowTicketFormResponse
import com.isu.profile.data.remote.model.response.UpdateTicketStatusResponse
import com.isu.profile.domain.repository.ProfileRepository
import com.isu.profile.domain.usecases.AddCommentUseCase
import com.isu.profile.domain.usecases.ChangePasswordUsingOldUseCase
import com.isu.profile.domain.usecases.FetchPinCodeUseCase
import com.isu.profile.domain.usecases.FetchProfileDataUseCase
import com.isu.profile.domain.usecases.FetchRaisedTicketsUseCase
import com.isu.profile.domain.usecases.GenerateOTPToChangePasswordUsecase
import com.isu.profile.domain.usecases.RaiseATicketUseCase
import com.isu.profile.domain.usecases.ShowTicketCommentsUseCase
import com.isu.profile.domain.usecases.TicketFormUseCase
import com.isu.profile.domain.usecases.UpdateTicketStatusUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {
    @Singleton
    @Provides
    fun provideFetchProfileApiUseCase(repository: ProfileRepository): ApiUseCase<AuthTokenData<Any>, com.isu.profile.data.remote.model.response.FetchProfileData> {
        return FetchProfileDataUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideFetchRaisedTicketsApiUseCase(repository: ProfileRepository): ApiUseCase<AuthTokenData<Any>, com.isu.profile.data.remote.model.response.FetchRaisedTicketsResponse> {
        return FetchRaisedTicketsUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideFetchTicketCommentsApiUseCase(repository: ProfileRepository): ApiUseCase<AuthTokenData<ShowTicketCommentRequest>, ShowTicketCommentResponse> {
        return ShowTicketCommentsUseCase(repository)
    }
    @Singleton
    @Provides
    fun raiseATicket(repository: ProfileRepository): ApiUseCase<AuthTokenData<RaiseAtTicketRequest>, RaiseATicketResponse> {
        return RaiseATicketUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideupdateTicketStatusApiUsecase(repository: ProfileRepository): ApiUseCase<AuthTokenData<UpdateTicketStatusRequest>, UpdateTicketStatusResponse> {
        return UpdateTicketStatusUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideTAddCommentsApiUseCase(repository: ProfileRepository): ApiUseCase<AuthTokenData<AddCommentsRequest>, com.isu.profile.data.remote.model.response.AddCommentsResponse> {
        return AddCommentUseCase(repository)
    }
    @Singleton
    @Provides
    fun provideChangePasswordUsingOldPasswordApiUseCase(repository: ProfileRepository): ApiUseCase<AuthTokenData<ChangePasswordUsingOldPasswordRequest>, ChangePasswordUsingOldPasswordResponse> {
        return ChangePasswordUsingOldUseCase(repository)
    }

    @Singleton
    @Provides
    fun provideGetOTPChangePasswordUsingOldPasswordApiUseCase(repository: ProfileRepository): ApiUseCase<AuthTokenData<GenerateOTPChangePasswordUsingOldPasswordRequest>, GenerateOTPChangePasswordUsingOldPasswordResponse> {
        return GenerateOTPToChangePasswordUsecase(repository)
    }

    @Singleton
    @Provides
    fun fetchPinCodeDataApiUseCase(repository: ProfileRepository): ApiUseCase<FetchPinCodeDataRequest, FetchPinCodeDataResponse> {
        return FetchPinCodeUseCase(repository)
    }

    @Singleton
    @Provides
    fun formDataApiUseCase(repository: ProfileRepository): ApiUseCase<GetFormRequest, ShowTicketFormResponse> {
        return TicketFormUseCase(repository)
    }

}