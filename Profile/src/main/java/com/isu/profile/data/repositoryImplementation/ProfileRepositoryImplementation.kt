package com.isu.profile.data.repositoryImplementation

import android.util.Log
import com.google.gson.Gson
import com.isu.common.utils.NetworkResource
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptedData
import com.isu.common.utils.handleFlowResponse
import com.isu.profile.data.remote.model.request.AddCommentsRequest
import com.isu.profile.data.remote.model.request.ChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.request.FetchPinCodeDataRequest
import com.isu.profile.data.remote.model.request.GenerateOTPChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.request.GetFormRequest
import com.isu.profile.data.remote.model.request.RaiseAtTicketRequest
import com.isu.profile.data.remote.model.request.ShowTicketCommentRequest
import com.isu.profile.data.remote.model.request.TokenProperty
import com.isu.profile.data.remote.model.request.UpdateTicketStatusRequest
import com.isu.profile.data.remote.model.response.ChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.FetchPinCodeDataResponse
import com.isu.profile.data.remote.model.response.FetchProfileData
import com.isu.profile.data.remote.model.response.GenerateOTPChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.RaiseATicketResponse
import com.isu.profile.data.remote.model.response.ShowTicketCommentResponse
import com.isu.profile.data.remote.model.response.ShowTicketFormResponse
import com.isu.profile.data.remote.model.response.UpdateTicketStatusResponse
import com.isu.profile.data.remote.src.ProfileApiService
import com.isu.profile.domain.repository.ProfileRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * @author-karthik
 * Implementation of the ProfileRepository interface.
 *
 * @property profileApiService Service to make network requests related to profile operations.
 */
class ProfileRepositoryImplementation @Inject constructor(
    private val profileApiService: ProfileApiService
) : ProfileRepository {

    /**
     * Decrypts the encrypted response and maps it to the specified type [T].
     *
     * @param encryptedResponseModel The encrypted response model.
     * @return A flow emitting the decrypted response model of type [T].
     */
    private inline fun <reified T> mapFun(
        encryptedResponseModel: EncryptedData
    ): Flow<T> = flow {
        val decryptResponseString = EncryptDecrypt.aesGcmDecryptFromBase64(encrypted = encryptedResponseModel)
        val decryptedResponseModel = Gson().fromJson(decryptResponseString, T::class.java)
        Log.d("KAPIMAP", "mapFun: $decryptedResponseModel")
        emit(decryptedResponseModel)
    }

    /**
     * Fetches the profile data from the server.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @return A flow emitting the network resource containing the profile data.
     */
    override fun fetchProfileData(

    ): Flow<NetworkResource<FetchProfileData>> {
        return handleFlowResponse(
            call = {
                profileApiService.fetchProfileData(
                )
            },
            mapFun = { encryptedResp ->
                var res: FetchProfileData? = null
                if (encryptedResp.statusCode == 0) {
                    mapFun<FetchProfileData.FetchProfileDecryptedData>(encryptedResp.data).collectLatest {
                        res = FetchProfileData(
                            data = it,
                            status = encryptedResp.status!!,
                            statusDesc = encryptedResp.statusDesc!!,
                            statusCode = encryptedResp.statusCode
                        )
                    }
                } else {
                    res = FetchProfileData(
                        data = null,
                        status = encryptedResp.status!!,
                        statusDesc = encryptedResp.statusDesc!!,
                        statusCode = encryptedResp.statusCode!!
                    )
                }
                res
            }
        )
    }

    /**
     * Fetches the raised tickets from the server.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @return A flow emitting the network resource containing the raised tickets.
     */
    override fun fetchRaisedTickets(

    ): Flow<NetworkResource<com.isu.profile.data.remote.model.response.FetchRaisedTicketsResponse>> {
        return handleFlowResponse(
            call = {
                profileApiService.fetchRaisedTickets(

                )
            },
            mapFun = { it }
        )
    }

    /**
     * Raises a new ticket on the server.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the details of the ticket to be raised.
     * @return A flow emitting the network resource containing the raised ticket response.
     */
    override fun raiseATicket(

        request: RaiseAtTicketRequest
    ): Flow<NetworkResource<RaiseATicketResponse>> {
        return handleFlowResponse(
            call = {
                profileApiService.raiseATicket(

                    request
                )
            },
            mapFun = { it }
        )
    }

    /**
     * Shows the comments of a ticket.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the ticket ID.
     * @return A flow emitting the network resource containing the ticket comments.
     */
    override fun showTicketComments(

        request: ShowTicketCommentRequest
    ): Flow<NetworkResource<ShowTicketCommentResponse>> {
        return handleFlowResponse(
            call = {
                profileApiService.showTicketComments(

                    request
                )
            },
            mapFun = { it }
        )
    }

    /**
     * Updates the status of a ticket.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the ticket ID and new status.
     * @return A flow emitting the network resource containing the update ticket status response.
     */
    override fun updateTicketStatus(
        authorization: String,
        tokenProperties: String,
        request: UpdateTicketStatusRequest
    ): Flow<NetworkResource<UpdateTicketStatusResponse>> {
        return handleFlowResponse(
            call = {
                profileApiService.updateTicketStatus(
                    request
                )
            },
            mapFun = { it }
        )
    }

    /**
     * Adds comments to a ticket.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the comment details.
     * @return A flow emitting the network resource containing the add comments response.
     */
    override fun addCommentsToTicket(
        authorization: String,
        tokenProperties: String,
        request: AddCommentsRequest
    ): Flow<NetworkResource<com.isu.profile.data.remote.model.response.AddCommentsResponse>> {
        return handleFlowResponse(
            call = {
                profileApiService.addCommentsToTicket(

                    request
                )
            },
            mapFun = { it }
        )
    }

    /**
     * Generates an OTP for changing the password using the old password.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the old password details.
     * @return A flow emitting the network resource containing the generate OTP response.
     */
    override fun generateOTPChangePasswordUsingOldPassword(
        authorization: String,
        tokenProperties: String,
        request: GenerateOTPChangePasswordUsingOldPasswordRequest
    ): Flow<NetworkResource<GenerateOTPChangePasswordUsingOldPasswordResponse>> {
        return handleFlowResponse(
            call = {
                profileApiService.generateOTPchangePasswordUsingOldPassword(
                    authorization,
                    TokenProperty(tokenProperties),
                    request
                )
            },
            mapFun = { it }
        )
    }

    /**
     * Changes the password using the old password.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the old password and new password details.
     * @return A flow emitting the network resource containing the change password response.
     */
    override fun changePasswordUsingOldPassword(
        authorization: String,
        tokenProperties: String,
        request: ChangePasswordUsingOldPasswordRequest,
    ): Flow<NetworkResource<ChangePasswordUsingOldPasswordResponse>> {
        return handleFlowResponse(
            call = {
                profileApiService.changePasswordUsingOldPassword(
                    authorization,
                    TokenProperty(tokenProperties),
                    request
                )
            },
            mapFun = { it }
        )
    }

    override fun fetchPinCodeData(request: FetchPinCodeDataRequest): Flow<NetworkResource<FetchPinCodeDataResponse>> {
        return handleFlowResponse(
            call = {
                profileApiService.fetchPinCodeData(request)
            }, mapFun = { it })
    }

    override fun showFormTicket(request: GetFormRequest): Flow<NetworkResource<ShowTicketFormResponse>> {
        return handleFlowResponse(
            call = {
                profileApiService.getFormData(request)
            },
            mapFun = {it}
        )
    }
}
