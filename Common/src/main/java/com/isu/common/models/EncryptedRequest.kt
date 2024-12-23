package com.isu.common.models


import com.google.gson.annotations.SerializedName

import androidx.annotation.Keep

@Keep

data class EncryptedRequest(
    @SerializedName("RequestData")
    val requestData: String? // AYUtdsPDex8Qry7zaWD9rAnGoV6S5BAG8peeML79F4PP42fEX8opOnaDzAfdjqLZHw5Kyg+TlzXL41sSs33B9CcvofYH+SuckAQIusqZ3TO4/LQwGxyO4v80FLp0dXdbuC8UEWisWkq85GLDDUJq7CXp2BEpIwj7SeUi+mX/xP0=
)