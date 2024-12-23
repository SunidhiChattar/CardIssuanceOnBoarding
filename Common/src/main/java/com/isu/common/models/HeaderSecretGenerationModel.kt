package com.isu.common.models


import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable


@Keep
@Serializable
data class HeaderSecretGenerationModel(
    @SerializedName("apiusername")
    val apiusername: String="PROG9345247257", // PROG9345247257
    @SerializedName("client_secret")
    val clientSecret: String="Gy5AVJ3qUuGPFVEAhTPK9hjWL93KKjmi5UUR1lvEfVz88j4NSE85UKVQBezx7W69", // Gy5AVJ3qUuGPFVEAhTPK9hjWL93KKjmi5UUR1lvEfVz88j4NSE85UKVQBezx7W69
    @SerializedName("epoch")
    val epoch: String? // 1734540480
)