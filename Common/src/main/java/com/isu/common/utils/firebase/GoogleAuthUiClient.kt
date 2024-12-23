//package com.isu.common.utils.firebase
//
//
//import android.content.Context
//import android.content.Intent
//import android.content.IntentSender
//import com.google.android.gms.auth.api.identity.BeginSignInRequest
//import com.google.android.gms.auth.api.identity.SignInClient
//import com.google.firebase.auth.GoogleAuthProvider
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.ktx.Firebase
//import com.isu.common.R
//import kotlinx.coroutines.CancellationException
//import kotlinx.coroutines.tasks.await
//
//
//class GoogleAuthUiClient(
//    private val context: Context,
//    private val oneTapClient: SignInClient,
//) {
//    private val auth = Firebase.auth
//
//    suspend fun signIn(): IntentSender? {
//        val result = try {
//            oneTapClient.beginSignIn(
//                buildSignInRequest()
//            ).await()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            if (e is CancellationException) throw e
//            null
//        }
//        return result?.pendingIntent?.intentSender
//    }
//
//
//    suspend fun signInWithIntent(intent: Intent): SignInResult {
//        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
//        val googleIdToken = credential.googleIdToken
//        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
//        return try {
//            val user = auth.signInWithCredential(googleCredentials).await().user
//            SignInResult(
//                data = user?.run {
//                    UserData(
//                        userId = uid,
//                        username = displayName,
//                        profilePictureUrl = photoUrl?.toString()
//                    )
//                },
//                errorMessage = null
//            )
//        } catch (e: Exception) {
//            e.printStackTrace()
//            if (e is CancellationException) throw e
//            SignInResult(
//                data = null,
//                errorMessage = e.message
//            )
//        }
//    }
//
//
////    suspend fun signInWithCredentialManager(): SignInResult {
////        // Create an instance of CredentialManager
////        val credentialManager = CredentialManager.create()
////
////        // Create a request for credentials
////        val request = GetCredentialRequest.Builder()
////            .addSignInCredentialOption() // Requests a federated sign-in option (Google Sign-In)
////            .build()
////
////        return try {
////            // Get the credentials from Credential Manager
////            val credentialResponse = credentialManager.getCredential(request)
////            val signInCredential = credentialResponse.credential as SignInCredential
////
////            // Extract the Google ID token from the credential
////            val googleIdToken = signInCredential.googleIdToken
////            if (googleIdToken != null) {
////                // Use the Google ID token to create Firebase credentials
////                val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
////
////                // Sign in with Firebase using the credential and await the result
////                val user = auth.signInWithCredential(googleCredentials).await().user
////                SignInResult(
////                    data = user?.run {
////                        UserData(
////                            userId = uid,
////                            username = displayName,
////                            profilePictureUrl = photoUrl?.toString()
////                        )
////                    },
////                    errorMessage = null
////                )
////            } else {
////                // Handle case when the Google ID token is null
////                SignInResult(
////                    data = null,
////                    errorMessage = "Google ID Token is null"
////                )
////            }
////        } catch (e: Exception) {
////            e.printStackTrace()
////            if (e is CancellationException) throw e
////            SignInResult(
////                data = null,
////                errorMessage = e.message
////            )
////        }
////    }
//
//
//    suspend fun signOut() {
//        try {
//            oneTapClient.signOut().await()
//            auth.signOut()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            if (e is CancellationException) throw e
//        }
//    }
//
//    fun getSignedInUser(): UserData? = auth.currentUser?.run {
//        UserData(
//            userId = uid,
//            username = displayName,
//            profilePictureUrl = photoUrl?.toString()
//        )
//    }
//
//    private fun buildSignInRequest(): BeginSignInRequest {
//        return BeginSignInRequest.Builder()
//            .setGoogleIdTokenRequestOptions(
//                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
//                    .setSupported(true)
//                    .setFilterByAuthorizedAccounts(false)
//                    .setServerClientId(context.getString(R.string.web_client_id))
//                    .build()
//            )
//            .setAutoSelectEnabled(true)
//            .build()
//    }
//}
//
//data class UserData(
//    val userId: String,
//    val username: String?,
//    val profilePictureUrl: String?,
//)
//
//data class SignInResult(val data: UserData?, val errorMessage: String?)
