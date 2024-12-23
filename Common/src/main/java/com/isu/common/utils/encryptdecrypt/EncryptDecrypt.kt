package com.isu.common.utils.encryptdecrypt

import android.util.Base64
import android.util.Base64.NO_WRAP
import android.util.Log
import com.fasterxml.jackson.databind.ObjectMapper
import com.isu.common.BuildConfig


import org.json.JSONObject
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.text.ParseException
import java.util.Arrays
import javax.crypto.Cipher
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * @author karthik
 * Encrypt decrypt
 *
 * @constructor Create empty Encrypt decrypt
 * class to handle encryption decryption of request
 * and responses
 */
object EncryptDecryptCBC{
    private  fun getEncryptionKey():String="a6T8tOCYiSzDTrcqPvCbJfy0wSQOVcfaevH0gtwCtoU="

    fun encryptRequest(responseBody: ByteArray, key: String= getEncryptionKey()): String? {
        val AES_KEY_SIZE: Int = 256
        val AES_BLOCK_SIZE: Int = 16

        try {
            val iv = ByteArray(AES_BLOCK_SIZE)
            val random = SecureRandom()
            random.nextBytes(iv)

            val decodedKey: ByteArray = Base64.decode(key, NO_WRAP)
            val secretKeySpec = SecretKeySpec(decodedKey, "AES")

            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, IvParameterSpec(iv))

            val paddedData: ByteArray = pkcs7Padding(responseBody, AES_BLOCK_SIZE)
            val encrypted = cipher.doFinal(responseBody)

            val result = ByteArray(iv.size + encrypted.size)
            System.arraycopy(iv, 0, result, 0, iv.size)
            System.arraycopy(encrypted, 0, result, iv.size, encrypted.size)
            return Base64.encodeToString(result, NO_WRAP)
        } catch (e: Exception) {
            return null
        }
    }

    fun decryptResponse(encryptedData: String, key: String = getEncryptionKey()): ByteArray? {
        val AES_BLOCK_SIZE = 16

        try {
            // Decode the Base64 encoded input
            val decodedData: ByteArray = Base64.decode(encryptedData, Base64.NO_WRAP)

            // Extract IV from the beginning of the data
            val iv = ByteArray(AES_BLOCK_SIZE)
            System.arraycopy(decodedData, 0, iv, 0, AES_BLOCK_SIZE)

            // Extract the encrypted payload (after the IV)
            val encryptedPayload = decodedData.copyOfRange(AES_BLOCK_SIZE, decodedData.size)

            // Decode the key
            val decodedKey: ByteArray = Base64.decode(key, Base64.NO_WRAP)
            val secretKeySpec = SecretKeySpec(decodedKey, "AES")

            // Initialize the cipher in DECRYPT_MODE
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, IvParameterSpec(iv))

            // Decrypt the payload
            val decryptedData = cipher.doFinal(encryptedPayload)

            return decryptedData
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }


    private fun pkcs7Padding(data: ByteArray, blockSize: Int): ByteArray {
        val padding = blockSize - (data.size % blockSize)
        val padText = ByteArray(padding)
        Arrays.fill(padText, padding.toByte())

        val paddedData = ByteArray(data.size + padding)
        System.arraycopy(data, 0, paddedData, 0, data.size)
        System.arraycopy(padText, 0, paddedData, data.size, padding)

        return paddedData
    }

}

object EncryptDecrypt {
    //Load keys from native NDK
//    init{
//        System.loadLibrary("keys")
//    }
    private  fun getEncryptionKey():String="XHgxl8qs5D6sejZncSsYg7OqPIeV0uQe5I9Zh+uHLcc="

    private fun concatenateByteArrays(a: ByteArray, b: ByteArray): ByteArray {
        return ByteBuffer.allocate(a.size + b.size).put(a).put(b).array()
    }

    private fun base64Decoding(input: String?): ByteArray {
        return Base64.decode(input, Base64.DEFAULT)
    }

    /**
     * Generate random nonce
     * function to generate a random no-once bytearray for GCM generation
     * @return
     */

    private fun generateRandomNonce(): ByteArray {
        val secureRandom = SecureRandom()
        val nonce = ByteArray(12)
        secureRandom.nextBytes(nonce)
        return nonce
    }

    /**
     * Base64encoding
     *  function to encode to Base64
     * @param input
     * @return
     */
    private fun base64Encoding(input: ByteArray): String {
        return Base64.encodeToString(input,Base64.DEFAULT)
    }

    /**
     * Android print exception
     * function to log exceptions
     * @param e
     */
    fun androidPrintException(e: Exception) {
        if(BuildConfig.BUILD_TYPE=="debug"){
            Log.d(
                "ENCDEC_Exception",
                e.message.toString() + "\n" + e.localizedMessage!!.toString() + "\n" + e.toString()
            )
        }

    }

    /**
     * Android print
     * function to make specificlogs
     * @param encrypted text
     */
    fun androidPrint(encryptedtext: String) {
        if(BuildConfig.BUILD_TYPE=="debug"){
            Log.d("ENCDEC", "encrypRequest: $encryptedtext")
        }

    }



    /**
     * Aes gcm encrypt to base64
     *
     * @param key
     * @param data
     * @return
     *  function to encrypt the request data
     *  using GCM encryption iv ,auth encryption
     *  and convert to encrypted data class
     *
     */
    fun aesGcmEncryptToEncryptedDataClass(
        key: String = getEncryptionKey(),
        data: String,
    ): EncryptedData {
        val random = SecureRandom()
        val salt = ByteArray(8)
        random.nextBytes(salt)
        val iv = ByteArray(12)
        random.nextBytes(iv)
        androidPrint("Ecrypted(before):"+data.toString())


        val nonce = generateRandomNonce()
        val secretKeySpec = SecretKeySpec(base64Decoding(key), "AES")
        val gcmParameterSpec = GCMParameterSpec(16 * 8, nonce)
        var encryptedDto: EncryptedData? = null

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec)
        val dataString = data
        val ciphertextWithTag = cipher.doFinal(dataString.toByteArray(StandardCharsets.UTF_8))
        val ciphertext = ciphertextWithTag.copyOfRange(0, ciphertextWithTag.size - 16)
        val gcmTag = ciphertextWithTag.copyOfRange(ciphertextWithTag.size - 16, ciphertextWithTag.size)
        val nonceBase64 = base64Encoding(nonce)
        val ciphertextBase64 = base64Encoding(ciphertext)
        val gcmTagBase64 = base64Encoding(gcmTag)

        encryptedDto = EncryptedData()
        encryptedDto.iv = nonceBase64.trim()

        encryptedDto.encryptedMessage = ciphertextBase64.trim()

        encryptedDto.authTag = gcmTagBase64.trim()
        androidPrint("Ecrypted(after):" + encryptedDto.toString())


        return encryptedDto
    }

    /**
     * Aes gcm encrypt to base64
     *
     * @param key
     * @param data
     * @return
     *  function to encrypt the request data
     *  using GCM encryption iv ,auth encryption
     *  and convert to encrypted data class
     *
     */
    fun aesGcmEncryptToEncryptedDataClassWithIvAuth(
        key: String = getEncryptionKey(),
        ivc: String = "M4njcIJrckuh",
        auth: String = "gbajJPAKA+EyLCGedyTG2g==",
        data: String,
    ): String {
        val random = SecureRandom()
        val salt = ByteArray(8)
        random.nextBytes(salt)
        val iv = ivc.toByteArray()
        random.nextBytes(iv)
        androidPrint("Ecrypted(before):" + data.toString())


        val nonce = generateFixedNonce(ivc)
        val secretKeySpec = SecretKeySpec(base64Decoding(key), "AES")
        val gcmParameterSpec = GCMParameterSpec(16 * 8, nonce)
        var encryptedDto: EncryptedData? = null

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec)
        val dataString = data
        val ciphertextWithTag = cipher.doFinal(dataString.toByteArray(StandardCharsets.UTF_8))
        val ciphertext = ciphertextWithTag.copyOfRange(0, ciphertextWithTag.size - 16)

        val gcmTag = base64Decoding(auth)
        val ciphertextBase64 = base64Encoding(ciphertext)
        val decodeAuthTag = base64Decoding(auth)
        val gcmTagBase64 = base64Encoding(decodeAuthTag)

        val ivBase64 = base64Encoding(nonce)
        val encryptedCardNumber = base64Encoding(ciphertext)
        val authTagBase64 = base64Encoding(gcmTag)


        encryptedDto = EncryptedData()
//        encryptedDto.iv = nonceBase64.trim()

        encryptedDto.encryptedMessage = ciphertextBase64.trim()

        encryptedDto.authTag = gcmTagBase64.trim()
        androidPrint("Ecrypted(after):" + encryptedDto.toString())


        return ivBase64 + "|" + encryptedCardNumber + "|" + authTagBase64
    }

    private fun generateFixedNonce(iv: String): ByteArray {
        return iv.toByteArray(StandardCharsets.UTF_8)
    }

    fun aesGcmEncryptToString(key: String = getEncryptionKey(), data: String): String {
        val random = SecureRandom()
        val salt = ByteArray(8)
        random.nextBytes(salt)
        val iv = ByteArray(12)
        random.nextBytes(iv)
        androidPrint("Ecrypted(before):" + data.toString())


        val nonce = generateRandomNonce()
        val secretKeySpec = SecretKeySpec(base64Decoding(key), "AES")
        val gcmParameterSpec = GCMParameterSpec(16 * 8, nonce)
        var encryptedDto: EncryptedData? = null

        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec)
        val dataString = data
        val ciphertextWithTag = cipher.doFinal(dataString.toByteArray(StandardCharsets.UTF_8))
        val ciphertext = ciphertextWithTag.copyOfRange(0, ciphertextWithTag.size - 16)
        val gcmTag =
            ciphertextWithTag.copyOfRange(ciphertextWithTag.size - 16, ciphertextWithTag.size)
        val nonceBase64 = base64Encoding(nonce)
        val ciphertextBase64 = base64Encoding(ciphertext)
        val gcmTagBase64 = base64Encoding(gcmTag)

        encryptedDto = EncryptedData()
        encryptedDto.iv = nonceBase64.trim()

        encryptedDto.encryptedMessage = ciphertextBase64.trim()

        encryptedDto.authTag = gcmTagBase64.trim()
        androidPrint("Ecrypted(after):" + encryptedDto.toString())


        return encryptedDto.iv + "|" + encryptedDto.encryptedMessage + "|" +encryptedDto.authTag
    }

    /**
     * Aes gcm decrypt from base64
     *
     * @param key
     * @param encrypted
     * @return
     * function to decrypt the response
     * using GCM iv,auth tag
     * to map the required response
     */
    fun aesGcmDecryptFromBase64(key: String= getEncryptionKey(), encrypted: EncryptedData): String ?{
        val nonce = base64Decoding(encrypted.iv)
        val ciphertext = base64Decoding(encrypted.encryptedMessage)
        val gcmTag = base64Decoding(encrypted.authTag)
        val encryptedData = concatenateByteArrays(ciphertext, gcmTag)
        var decryptedData: String? = null
        try {
            androidPrint("Decrypted(before):$encrypted")
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val secretKeySpec = SecretKeySpec(base64Decoding(key), "AES")
            val gcmParameterSpec = GCMParameterSpec(16 * 8, nonce)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec)
            val decryptedString = String(cipher.doFinal(encryptedData))

            decryptedData = decryptedString
            androidPrint("Decrypted(after):$decryptedData")

        } catch (pe: ParseException) {
            androidPrintException(pe)
            System.err.println(pe.message)
            Log.d("CVVEXCEPTION", "aesGcmDecryptFromBase64:${pe.message} ")
        } catch (e: Exception) {
            androidPrintException(e)
            System.err.println(e.message)
            Log.d("CVVEXCEPTION", "aesGcmDecryptFromBase64:${e.message} ")
        }
        return decryptedData
    }

    fun aesGcmDecryptFromBase64FromJsonObject(key: String= getEncryptionKey(), encrypted: JSONObject): String ?{

        val iv = encrypted.getString("iv")
        val encryptedMessage = encrypted.getString("encryptedMessage")
        val authTag = encrypted.getString("authTag")
        val nonce = base64Decoding(iv)
        val ciphertext = base64Decoding(encryptedMessage)
        val gcmTag = base64Decoding(authTag)
        val encryptedData = concatenateByteArrays(ciphertext, gcmTag)
        var decryptedData: String? = null
        try {
            androidPrint("Decrypted(before):$encrypted")
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val secretKeySpec = SecretKeySpec(base64Decoding(key), "AES")
            val gcmParameterSpec = GCMParameterSpec(16 * 8, nonce)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec)
            val decryptedString = String(cipher.doFinal(encryptedData))

            decryptedData = decryptedString
            androidPrint("Decrypted(after):$decryptedData")

        } catch (pe: ParseException) {
            androidPrintException(pe)
            System.err.println(pe.message)
        } catch (e: Exception) {
            androidPrintException(e)
            System.err.println(e.message)
        }
        return decryptedData
    }


}


object AesGcmEncryption {

    // Utility to decode base64 string to byte array
    private fun base64Decoding(input: String): ByteArray {
        return Base64.decode(input, Base64.DEFAULT)
    }

    // Utility to encode byte array to base64 string
    private fun base64Encoding(input: ByteArray): String {
        return Base64.encodeToString(input, Base64.DEFAULT)
    }

    // Encrypt method that accepts a key, IV, auth tag, and data to encrypt
    @Throws(Exception::class)
    fun encryptWithGivenIvAndAuth(
        key: String = "XHgxl8qs5D6sejZncSsYg7OqPIeV0uQe5I9Zh+uHLcc=",
        ivc: String = "M4njcIJrckuh",
        auth: String = "gbajJPAKA+EyLCGedyTG2g==",
        data: String,
    ): String {
        // Create a SecureRandom instance
//        val random = SecureRandom()

        // Use provided IV or generate fixed nonce
        val iv = ivc.toByteArray(StandardCharsets.UTF_8)

        // Decode the encryption key (Base64 encoded key)
        val secretKeySpec = SecretKeySpec(base64Decoding(key), "AES")

        // Initialize GCMParameterSpec with the IV and 128-bit auth tag length
        val gcmParameterSpec = GCMParameterSpec(16 * 8, iv)

        // Initialize Cipher in AES/GCM/NoPadding mode
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec)

        val dataString = ObjectMapper().writeValueAsString(data)
        val ciphertextWithTag = cipher.doFinal(dataString.toByteArray(StandardCharsets.UTF_8))
        // Convert data string to byte array
//        val plaintextBytes = data.toByteArray(StandardCharsets.UTF_8)
//
//        // Encrypt data and get ciphertext + auth tag
//        val ciphertextWithTag = cipher.doFinal(plaintextBytes)

        // Extract ciphertext (without the auth tag)
        val ciphertext = ciphertextWithTag.copyOfRange(0, ciphertextWithTag.size - 16)

        // Decode the predefined auth tag from Base64
        val gcmTag = base64Decoding(auth)

        System.arraycopy(ciphertextWithTag, 0, ciphertext, 0, ciphertextWithTag.size - 16)
        System.arraycopy(ciphertextWithTag, ciphertextWithTag.size - 16, gcmTag, 0, 16)

        // Base64 encode IV, ciphertext, and the auth tag
        val ivBase64 = base64Encoding(iv).trim()
        val ciphertextBase64 = base64Encoding(ciphertext).trim()
        val authTagBase64 = base64Encoding(gcmTag).trim()

        // Combine the encrypted components into a single string
        val separator = "|"
        return "$ivBase64$separator$ciphertextBase64$separator$authTagBase64"
    }
}

fun main() {
    // Example usage
    val key = "your_base64_encoded_key"  // Example: Base64 encoded key
    val iv = "M4njcIJrckuh"              // Example IV
    val auth = "gbajJPAKA+EyLCGedyTG2g==" // Example auth tag
    val data = "Sensitive data to encrypt"

    val encryptedData = AesGcmEncryption.encryptWithGivenIvAndAuth(key, iv, auth, data)
    println("Encrypted Data: $encryptedData")
}

/*
object switchEncryption{



    lateinit var iv: String


    lateinit var authTag: String

     object {
        private fun base64Decoding(input: String): ByteArray = Base64.decode(input)

        fun concatenateByteArrays(a: ByteArray, b: ByteArray): ByteArray {
            return ByteBuffer.allocate(a.size + b.size).put(a).put(b).array()
        }

        private fun base64Encoding(input: ByteArray): String = Base64.encodeToString(input)
    }

    // Encryption Service
     fun encrypt(cardNumber: String): Map<Any, Any> {
        val response = mutableMapOf<Any, Any>()

        try {
            val nonce = generateFixedNonce()

            // Fetch and decode the secret key
            val secretKeySpec = SecretKeySpec(base64Decoding(fetchSecretValues.cardSecretValue), "AES")

            // GCM parameter with 128-bit tag length
            val gcmParameterSpec = GCMParameterSpec(16 * 8, nonce)

            // Initialize cipher for AES/GCM encryption
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, gcmParameterSpec)

            // Convert card number to JSON string and encrypt
            val dataString = ObjectMapper().writeValueAsString(cardNumber)
            val ciphertextWithTag = cipher.doFinal(dataString.toByteArray(StandardCharsets.UTF_8))

            // Split ciphertext and authentication tag
            val ciphertext = ByteArray(ciphertextWithTag.size - 16)
            val gcmTag = base64Decoding(authTag)
            System.arraycopy(ciphertextWithTag, 0, ciphertext, 0, ciphertextWithTag.size - 16)
            System.arraycopy(ciphertextWithTag, ciphertextWithTag.size - 16, gcmTag, 0, 16)

            // Base64 encode components
            val ivBase64 = base64Encoding(nonce)
            val encryptedCardNumber = base64Encoding(ciphertext)
            val authTagBase64 = base64Encoding(gcmTag)

            // Combine results into one string
            val separator = "|"
            val combinedString = "$ivBase64$separator$encryptedCardNumber$separator$authTagBase64"

            // Put encrypted data in response map
            response["ENCRYPTEDCARDNUMBER"] = combinedString

        } catch (e: Exception) {
            response["FAILED"] = e.message.toString()
            logger.error("Unable to encrypt the card number :: reason {}", e.message)
        }

        return response
    }

    // Decryption Service
     fun decrypt2(data: String): Map<String, String> {
        val separator = "|"
        val firstSeparatorIndex = data.indexOf(separator)
        val secondSeparatorIndex = data.indexOf(separator, firstSeparatorIndex + 1)

        // Extract individual parts of the data string
        val iv = data.substring(0, firstSeparatorIndex)
        val encryptedCardNumber = data.substring(firstSeparatorIndex + 1, secondSeparatorIndex)
        val authTag = data.substring(secondSeparatorIndex + 1)

        // Decode Base64 values
        val nonce = base64Decoding(iv)
        val gcmTag = base64Decoding(authTag)
        val ciphertext = base64Decoding(encryptedCardNumber)
        val encryptedData = concatenateByteArrays(ciphertext, gcmTag)

        val decryptionResponse = mutableMapOf<String, String>()
        var decrydata: Any

        try {
            // Decrypt using AES/GCM/NoPadding
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            val secretKeySpec = SecretKeySpec(base64Decoding(fetchSecretValues.cardSecretValue), "AES")
            val gcmParameterSpec = GCMParameterSpec(16 * 8, nonce)
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, gcmParameterSpec)

            // Decrypt and parse JSON
            val decryptedString = String(cipher.doFinal(encryptedData))
            decrydata = JSONParser(decryptedString).parse()

        } catch (e: Exception) {
            decryptionResponse["FAILED"] = e.message.toString()
            return decryptionResponse
        }

        decryptionResponse["SUCCESSFUL"] = decrydata.toString()
        return decryptionResponse
    }

    private fun generateFixedNonce(): ByteArray {
        return iv.toByteArray(StandardCharsets.UTF_8)
    }
}*/



