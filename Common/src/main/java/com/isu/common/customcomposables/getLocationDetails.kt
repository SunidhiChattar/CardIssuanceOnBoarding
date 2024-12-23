package com.isu.common.customcomposables

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.gson.annotations.SerializedName
import java.util.Locale

data class ShippingAddressItem(

    @field:SerializedName("country")
    val country: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("address2")
    val address2: String? = null,

    @field:SerializedName("address1")
    val address1: String? = null,

    @field:SerializedName("pinCode")
    val pinCode: String? = null,

    @field:SerializedName("id")
    val id: Long? = null,

    @field:SerializedName("state")
    val state: String? = null,
    @field:SerializedName("addressType")
    val type: String? = null,
)

fun getLocationDetails(
    context: Context,
    latitude: Double,
    longitude: Double,
): ShippingAddressItem? {
    val geocoder = Geocoder(context, Locale.getDefault())
    val addresses: List<Address>? = geocoder.getFromLocation(latitude, longitude, 1)

    return if (addresses != null && addresses.isNotEmpty()) {
        val address: Address = addresses[0]
        val streetAddress = address.getAddressLine(0) // Street address
        val city = address.locality // City
        val state = address.adminArea // State
        val country = address.countryName // Country
        val pinCode = address.postalCode
        return ShippingAddressItem(
            country = country,
            city = city,
            address2 = streetAddress,
            address1 = streetAddress,
            pinCode = pinCode,
            id = 0,
            state = state,
            type = "OTHERS"

        )


    } else {
        null
    }
}
