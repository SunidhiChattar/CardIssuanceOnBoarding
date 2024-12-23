package com.isu.profile.data.remote.model.response

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName

/**
 * Response class representing the data returned after fetching pin code information.
 *
 * @property data The main response data containing status and pincode details.
 * @property statusCode The status code indicating the success or failure of the request.
 */
@Keep
data class FetchPinCodeDataResponse(
    @SerializedName("data")
    val data: Data?, // The main response data object
    @SerializedName("statusCode")
    val statusCode: Int?, // Status code, 0 indicates success
) {

    /**
     * Data class representing the status and description of the response.
     *
     * @property data The detailed pincode data including city, district, state, and pincode.
     * @property status The status of the response (e.g., "Success").
     * @property statusDesc A description of the status (e.g., "Pincode Data Fetched!").
     */
    @Keep
    data class Data(
        @SerializedName("data")
        val data: PincodeData?, // Detailed pincode data
        @SerializedName("status")
        val status: String?, // Status of the response, e.g., "Success"
        @SerializedName("statusDesc")
        val statusDesc: String?, // Description of the status, e.g., "Pincode Data Fetched!"
    ) {

        /**
         * Data class representing the pincode details.
         *
         * @property city The name of the city associated with the pincode.
         * @property district The district where the pincode is located.
         * @property pincode The actual pincode value.
         * @property state The state associated with the pincode.
         */
        @Keep
        data class PincodeData(
            @SerializedName("city")
            val city: String?, // Name of the city, e.g., "Cuttack North"
            @SerializedName("district")
            val district: String?, // Name of the district, e.g., "JAJAPUR"
            @SerializedName("pincode")
            val pincode: Int?, // The pincode, e.g., 755050
            @SerializedName("state")
            val state: String?, // The state code, e.g., "OR"
        )
    }
}
