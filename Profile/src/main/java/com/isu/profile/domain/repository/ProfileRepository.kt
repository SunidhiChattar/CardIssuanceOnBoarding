package com.isu.profile.domain.repository

import com.isu.common.utils.NetworkResource
import com.isu.profile.data.remote.model.request.AddCommentsRequest
import com.isu.profile.data.remote.model.request.ChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.request.FetchPinCodeDataRequest
import com.isu.profile.data.remote.model.request.GenerateOTPChangePasswordUsingOldPasswordRequest
import com.isu.profile.data.remote.model.request.GetFormRequest
import com.isu.profile.data.remote.model.request.RaiseAtTicketRequest
import com.isu.profile.data.remote.model.request.ShowTicketCommentRequest
import com.isu.profile.data.remote.model.request.UpdateTicketStatusRequest
import com.isu.profile.data.remote.model.response.AddCommentsResponse
import com.isu.profile.data.remote.model.response.ChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.FetchPinCodeDataResponse
import com.isu.profile.data.remote.model.response.FetchProfileData
import com.isu.profile.data.remote.model.response.FetchRaisedTicketsResponse
import com.isu.profile.data.remote.model.response.GenerateOTPChangePasswordUsingOldPasswordResponse
import com.isu.profile.data.remote.model.response.RaiseATicketResponse
import com.isu.profile.data.remote.model.response.ShowTicketCommentResponse
import com.isu.profile.data.remote.model.response.ShowTicketFormResponse
import com.isu.profile.data.remote.model.response.UpdateTicketStatusResponse
import kotlinx.coroutines.flow.Flow

/**
 * @author -karthik
 * Interface for profile-related operations in the domain layer.
 */
interface ProfileRepository {

     /**
      * Fetches profile data for the user.
      *
      * @param authorization The authorization token.
      * @param tokenProperties The token properties.
      * @return A [Flow] emitting a [NetworkResource] containing [FetchProfileData].
      */
     fun fetchProfileData(

     ): Flow<NetworkResource<FetchProfileData>>

     /**
      * Fetches raised tickets for the user.
      *
      * @param authorization The authorization token.
      * @param tokenProperties The token properties.
      * @return A [Flow] emitting a [NetworkResource] containing [FetchRaisedTicketsResponse].
      */
     fun fetchRaisedTickets(

     ): Flow<NetworkResource<FetchRaisedTicketsResponse>>

     /**
      * Raises a new ticket.
      *
      * @param authorization The authorization token.
      * @param tokenProperties The token properties.
      * @param request The request model containing the details of the ticket to be raised.
      * @return A [Flow] emitting a [NetworkResource] containing [RaiseATicketResponse].
      */
     fun raiseATicket(

         request: RaiseAtTicketRequest
     ): Flow<NetworkResource<RaiseATicketResponse>>

     /**
      * Shows comments for a specific ticket.
      *
      * @param authorization The authorization token.
      * @param tokenProperties The token properties.
      * @param request The request model containing the ticket ID for which comments are to be fetched.
      * @return A [Flow] emitting a [NetworkResource] containing [ShowTicketCommentResponse].
      */
     fun showTicketComments(

         request: ShowTicketCommentRequest
     ): Flow<NetworkResource<ShowTicketCommentResponse>>

     /**
      * Updates the status of a ticket.
      *
      * @param authorization The authorization token.
      * @param tokenProperties The token properties.
      * @param request The request model containing the ticket ID and the new status.
      * @return A [Flow] emitting a [NetworkResource] containing [UpdateTicketStatusResponse].
      */
     fun updateTicketStatus(
          authorization: String,
          tokenProperties: String,
          request: UpdateTicketStatusRequest
     ): Flow<NetworkResource<UpdateTicketStatusResponse>>

     /**
      * Adds comments to a specific ticket.
      *
      * @param authorization The authorization token.
      * @param tokenProperties The token properties.
      * @param request The request model containing the comment details.
      * @return A [Flow] emitting a [NetworkResource] containing [AddCommentsResponse].
      */
     fun addCommentsToTicket(
          authorization: String,
          tokenProperties: String,
          request: AddCommentsRequest
     ): Flow<NetworkResource<AddCommentsResponse>>

     /**
      * Generates an OTP for changing the password using the old password.
      *
      * @param authorization The authorization token.
      * @param tokenProperties The token properties.
      * @param request The request model containing the details for generating OTP.
      * @return A [Flow] emitting a [NetworkResource] containing [GenerateOTPChangePasswordUsingOldPasswordResponse].
      */
     fun generateOTPChangePasswordUsingOldPassword(
          authorization: String,
          tokenProperties: String,
          request: GenerateOTPChangePasswordUsingOldPasswordRequest
     ): Flow<NetworkResource<GenerateOTPChangePasswordUsingOldPasswordResponse>>

     /**
      * Changes the password using the old password.
      *
      * @param authorization The authorization token.
      * @param tokenProperties The token properties.
      * @param request The request model containing the old and new password details.
      * @return A [Flow] emitting a [NetworkResource] containing [GenerateOTPChangePasswordUsingOldPasswordResponse].
      */
     fun changePasswordUsingOldPassword(
         authorization: String,
         tokenProperties: String,
         request: ChangePasswordUsingOldPasswordRequest,
     ): Flow<NetworkResource<ChangePasswordUsingOldPasswordResponse>>

    fun fetchPinCodeData(
        request: FetchPinCodeDataRequest,
    ): Flow<NetworkResource<FetchPinCodeDataResponse>>

    fun showFormTicket(
         request: GetFormRequest
    ): Flow<NetworkResource<ShowTicketFormResponse>>
}
