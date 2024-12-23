package com.isu.common.utils

import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptedData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Decrypts the encrypted response and maps it to the specified type [T].
 *
 * @param encryptedResponseModel The encrypted response model.
 * @return A flow emitting the decrypted response model of type [T].
 */
inline fun <reified T> mapFun(
    encryptedResponseModel: EncryptedData,
): Flow<T?> = flow {
    val decryptResponseString =
        EncryptDecrypt.aesGcmDecryptFromBase64(encrypted = encryptedResponseModel)
    val type = object : TypeToken<T>() {}.type
    val decryptedResponseModel =
        try {
            Gson()
                .fromJson<T>(decryptResponseString, type)
        } catch (e: Exception) {

            e.printStackTrace()
            null
        }
    Log.d("DECR", "mapFun:${decryptedResponseModel} ")
    emit(decryptedResponseModel)
}