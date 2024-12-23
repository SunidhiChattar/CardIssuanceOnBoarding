package com.isu.profile.data.remote.src

import com.isu.common.BuildConfig
import com.isu.common.utils.encryptdecrypt.EncryptedResponse
import com.isu.profile.data.remote.model.request.AddCommentsRequest
import com.isu.profile.data.remote.model.request.ChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.request.FetchPinCodeDataRequest
import com.isu.profile.data.remote.model.request.GenerateOTPChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.request.GetFormRequest
import com.isu.profile.data.remote.model.request.RaiseAtTicketRequest
import com.isu.profile.data.remote.model.request.ShowTicketCommentRequest
import com.isu.profile.data.remote.model.request.TokenProperty
import com.isu.profile.data.remote.model.request.UpdateTicketStatusRequest
import com.isu.profile.data.remote.model.response.AddCommentsResponse
import com.isu.profile.data.remote.model.response.ChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.FetchPinCodeDataResponse
import com.isu.profile.data.remote.model.response.FetchRaisedTicketsResponse
import com.isu.profile.data.remote.model.response.GenerateOTPChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.RaiseATicketResponse
import com.isu.profile.data.remote.model.response.ShowTicketCommentResponse
import com.isu.profile.data.remote.model.response.ShowTicketFormResponse
import com.isu.profile.data.remote.model.response.UpdateTicketStatusResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

/**
 * @author-karthik
 * API service interface for profile-related operations.
 */
interface ProfileApiService {

    /**
     * Fetches profile data for the user.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @return A [retrofit2.Response] containing the encrypted profile data.
     */
    @GET(BuildConfig.DASHBOARD_USER_PROFILE)
    suspend fun fetchProfileData(

    ): retrofit2.Response<EncryptedResponse>

    /**
     * Fetches raised tickets for the user.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @return A [retrofit2.Response] containing the fetched raised tickets response.
     */
    @POST(BuildConfig.GET_TICKETS)
    suspend fun fetchRaisedTickets(

    ): retrofit2.Response<FetchRaisedTicketsResponse>

    /**
     * Raises a new ticket.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the details of the ticket to be raised.
     * @return A [retrofit2.Response] containing the raised ticket response.
     */
    @POST(BuildConfig.RAISE_TICKET)
    suspend fun raiseATicket(

        @Body request: RaiseAtTicketRequest
    ): retrofit2.Response<RaiseATicketResponse>

    /**
     * Shows comments for a specific ticket.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the ticket ID for which comments are to be fetched.
     * @return A [retrofit2.Response] containing the ticket comments response.
     */
    @POST(BuildConfig.SHOW_TICKET_COMMENTS)
    suspend fun showTicketComments(

        @Body request: ShowTicketCommentRequest
    ): retrofit2.Response<ShowTicketCommentResponse>

    /**
     * Updates the status of a ticket.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the ticket ID and the new status.
     * @return A [retrofit2.Response] containing the update ticket status response.
     */
    @POST(BuildConfig.UPDATE_TICKET_STATUS)
    suspend fun updateTicketStatus(
        @Body request: UpdateTicketStatusRequest
    ): retrofit2.Response<UpdateTicketStatusResponse>

    /**
     * Adds comments to a specific ticket.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the comment details.
     * @return A [retrofit2.Response] containing the add comments response.
     */
    @POST(BuildConfig.ADD_COMMENTS)
    suspend fun addCommentsToTicket(
        @Body request: AddCommentsRequest
    ): retrofit2.Response<AddCommentsResponse>


    @POST("https://apidev.iserveu.online/zendesk/prepaidCard/show_ticket_form")
    suspend fun getFormData(
        @Body request: GetFormRequest,

        ): retrofit2.Response<ShowTicketFormResponse>

    /**
     * Generates an OTP for changing the password using the old password.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the details for generating OTP.
     * @return A [retrofit2.Response] containing the generate OTP response.
     */
    @POST(BuildConfig.OTP_CHANGE_PASSWORD_USING_OLD_PASSWORD)
    suspend fun generateOTPchangePasswordUsingOldPassword(
        @Header("authorization") authorization: String,
        @Header("token_properties") tokenProperties: TokenProperty,
        @Body request: GenerateOTPChangePasswordUsingOldPasswordRequest
    ): retrofit2.Response<GenerateOTPChangePasswordUsingOldPasswordResponse>

    /**
     * Changes the password using the old password.
     *
     * @param authorization The authorization token.
     * @param tokenProperties The token properties.
     * @param request The request model containing the old and new password details.
     * @return A [retrofit2.Response] containing the change password response.
     */
    @POST(BuildConfig.CHANGE_PASSWORD_USING_OLD_PASSWORD)
    suspend fun changePasswordUsingOldPassword(
        @Header("authorization") authorization: String,
        @Header("token_properties") tokenProperties: TokenProperty,
        @Body request: ChangePasswordUsingOldPasswordRequest,
    ): retrofit2.Response<ChangePasswordUsingOldPasswordResponse>


    @POST(BuildConfig.FETCH_PIN_CODE)
    suspend fun fetchPinCodeData(
        @Body request: FetchPinCodeDataRequest,
    ): retrofit2.Response<FetchPinCodeDataResponse>

}
