/*
package com.isu.common.utils.firebase

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.isu.common.utils.NetworkResource
import kotlinx.coroutines.flow.flow

class FirebaseStorageManager(val context: Context) {
    init {


    }

    suspend fun uploadFileToFirebaseStorage(fileUri: Uri) = flow<NetworkResource<String>> {
        var downloadUrl = ""
        try {
            emit(NetworkResource.Loading(true))
            // Get a reference to Firebase Storage
            if (Firebase.storage.app != null) {


                val storageReference =
                    FirebaseStorage.getInstance("gs://card-issuance-4b54d.appspot.com").reference
                val fileReference = storageReference.child("uploads/${System.currentTimeMillis()}")

                // Upload file to Firebase Storage
                fileReference.putFile(fileUri)
                    .addOnSuccessListener {
                        fileReference.downloadUrl.addOnFailureListener {
                            Log.d("KFB", "uploadFileToFirebaseStorage:$it ")
                        }.addOnSuccessListener {
                            if (it != null) {
                                downloadUrl = it.toString()
                            } else {
                                Log.d("KFB", "uploadFileToFirebaseStorage:$it ")
                            }

                        }
                    }.addOnFailureListener {
                        Log.d("KFB", "uploadFileToFirebaseStorage:$it ")
                    }
                // Get download URL after upload


                // Trigger success callback with download URL

                emit(NetworkResource.Success(downloadUrl))
            } else {
                emit(NetworkResource.Error("Encountered issue while uploading file"))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            // Trigger failure callback
            emit(NetworkResource.Error(e.message ?: "Encountered issue while uploading file"))
        }
        emit(NetworkResource.Loading(false))
    }


}*/
