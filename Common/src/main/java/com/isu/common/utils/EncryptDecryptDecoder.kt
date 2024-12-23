package com.isu.common.utils

import android.util.Log
import com.isu.apitracker.util.BodyDecoder
import com.isu.common.utils.encryptdecrypt.EncryptDecrypt
import com.isu.common.utils.encryptdecrypt.EncryptDecryptCBC
import com.isu.common.utils.encryptdecrypt.EncryptDecryptCBC.decryptResponse
import okhttp3.Request
import okhttp3.Response
import okio.Buffer
import org.json.JSONObject

class EncryptDecryptDecoder: BodyDecoder {

    override fun decodeRequest(request: Request): String? {
        return try {
            val bodyJsonStr = request.body?.toStringRepresentation()
            val bodyJsonObj = JSONObject(bodyJsonStr)

//            val dataObject = bodyJsonObj.getJSONObject("data")

            Log.d("DEC", "decodeRequest: $bodyJsonObj")
            EncryptDecrypt.aesGcmDecryptFromBase64FromJsonObject(encrypted = bodyJsonObj)
        } catch (e: Exception) {
            return e.message ?: "Cannot decrypt"
        }


    }

    override fun decodeResponse(response: Response): String? {
        return   try {
        val responseBodyStr= response.body.string()
        val jsonObject=JSONObject(responseBodyStr)
        Log.d("DEC", "decodeRequest: $jsonObject")




                val dataObject = jsonObject.getJSONObject("data")
                val iv = dataObject.getString("iv")
                val encryptedMessage = dataObject.getString("encryptedMessage")
                val authTag = dataObject.getString("authTag")
                println("IV: $iv")
                println("Encrypted Message: $encryptedMessage")
                println("Auth Tag: $authTag")
                EncryptDecrypt.aesGcmDecryptFromBase64FromJsonObject(encrypted = dataObject ).toString()


        }catch (e:Exception){
           Log.d("DEC", "decodeResponse: ${e.message}")
            e.printStackTrace()
            "2"
        }

    }
}
private fun okhttp3.RequestBody.toStringRepresentation(): String {
    val buffer = Buffer()
    writeTo(buffer)
    return buffer.readUtf8()
}

class EncryptDecryptDecoderCBC: BodyDecoder {

    override fun decodeRequest(request: Request): String? {
        return try {
            val bodyJsonStr = request.body?.toStringRepresentation()
            val bodyJsonObj = JSONObject(bodyJsonStr)

//            val dataObject = bodyJsonObj.getJSONObject("data")
            val encryptedData=bodyJsonObj.getString("RequestData")

            Log.d("DEC", "decodeRequest: $bodyJsonObj")
            if (bodyJsonStr != null) {
                EncryptDecryptCBC.decryptResponse(encryptedData = encryptedData)?.decodeToString()
            }else{
                ""
            }
        } catch (e: Exception) {
            return e.message ?: "Cannot decrypt"
        }


    }

    override fun decodeResponse(response: Response): String? {
        return try {
            val responseBodyStr = response.body.string()
            val jsonObject = JSONObject(responseBodyStr)
            Log.d("DEC", "decodeRequest: $jsonObject")


            val encryptedData = jsonObject.getString("ResponseData")

//                val dataObject = jsonObject.getJSONObject("data")
//                val iv = dataObject.getString("iv")
//                val encryptedMessage = dataObject.getString("encryptedMessage")
//                val authTag = dataObject.getString("authTag")
//                println("IV: $iv")
//                println("Encrypted Message: $encryptedMessage")
//                println("Auth Tag: $authTag")
            EncryptDecryptCBC.decryptResponse(encryptedData = encryptedData)?.decodeToString()


        } catch (e: Exception) {
            Log.d("DEC", "decodeResponse: ${e.message}")
            e.printStackTrace()
            "2"
        }
    }
//    {
//        return   try {
//            val responseBodyStr= response.body.string()
//            Log.d("DEC", "decodeRequest: $response")
//            Log.d("DEC", "decodeRequest: $responseBodyStr")
//            val jsonObject= JSONObject(responseBodyStr)
//            Log.d("DEC", "decodeRequest: $jsonObject")
//
//
//            val encryptedData=jsonObject.getString("ResponseData")
//
////                val dataObject = jsonObject.getJSONObject("data")
////                val iv = dataObject.getString("iv")
////                val encryptedMessage = dataObject.getString("encryptedMessage")
////                val authTag = dataObject.getString("authTag")
////                println("IV: $iv")
////                println("Encrypted Message: $encryptedMessage")
////                println("Auth Tag: $authTag")
//            EncryptDecryptCBC.decryptResponse(encryptedData = encryptedData )?.decodeToString()
//
//
//        }catch (e:Exception){
//            Log.d("DEC", "decodeResponse: ${e.message}")
//            e.printStackTrace()
//            "2"
//        }
//
//    }
}
