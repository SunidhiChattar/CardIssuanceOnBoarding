package com.isu.common.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.isu.common.utils.encryptdecrypt.EncryptDecryptCBC
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

inline fun <reified T> mapFunCBC(
        encryptedResponseModel: String,
    ): Flow<T?> = flow {
        val decryptResponseString =
            EncryptDecryptCBC.decryptResponse(encryptedData = encryptedResponseModel)
    Log.d("DECR", "dec:${encryptedResponseModel} ")
    Log.d("DECR", "decString:${decryptResponseString} ")
        val type = object : TypeToken<T>() {}.type
        val decryptedResponseModel =
            try {
                Gson()
                    .fromJson<T>(decryptResponseString?.decodeToString(), type)
            } catch (e: Exception) {
                Log.d("DECR", "mapFun:${e.message} ")
                e.printStackTrace()
                null
            }
        Log.d("DECR", "mapFun:${decryptedResponseModel} ")
        emit(decryptedResponseModel)
    }