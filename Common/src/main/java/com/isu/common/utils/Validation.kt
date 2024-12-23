package com.isu.common.utils

import android.util.Patterns
import androidx.core.text.isDigitsOnly
import com.isu.common.R
import java.util.Calendar

object Validation {
    fun alphaNumericCheck(text: String): Boolean {
        return text.matches("^[a-zA-Z0-9]*$".toRegex())
    }
    fun addressCheck(text: String): Boolean {
        return text.matches("^[a-zA-Z0-9 ]+\$".toRegex())
    }
    fun isValidBankAccountNumber(accountNumber: String): Boolean {
        // Check if the account number is not empty and has a valid length
        if ( accountNumber.length < 8 || accountNumber.length > 20) {
            return false
        }

        // Check if the account number contains only digits
        if (!accountNumber.all { it.isDigit() }) {
            return false
        }

        // Additional validation logic can be added here based on the specific requirements of the bank

        // If none of the above conditions are met, consider the account number valid
        return true
    }

    fun isValidIFSC(ifscCode: String): Boolean {
        // Check if the IFSC code is not empty and has a valid length
        if (ifscCode.isEmpty() || ifscCode.length != 11) {
            return false
        }

        // Check if the first 4 characters are alphabets
        val firstFourCharacters = ifscCode.substring(0, 4)
        if (!firstFourCharacters.all { it.isLetter() }) {
            return false
        }

        // Check if the 5th character is 0 (zero)
        if (ifscCode[4] != '0') {
            return false
        }

        // Check if the remaining characters are alphanumeric
        val remainingCharacters = ifscCode.substring(5)
        return remainingCharacters.all { it.isLetterOrDigit() }

        // Additional validation logic can be added here based on the specific format or requirements of the IFSC code

        // If none of the above conditions are met, consider the IFSC code valid
    }

    fun matchValidation(value: String, fieldState: String): Boolean {
        return fieldState.contains(value.toRegex())
    }

    fun containsOnlyAlphabetic(text:String):Boolean{
        return text.matches("^[a-zA-Z]*$".toRegex())
    }
    fun nomineeUserCheck(text: String): Boolean{
        return text.matches("^[a-zA-Z ]*$".toRegex())
    }

    fun isValidMobileNumer(value: String): Boolean {
        val regex = Patterns.PHONE.toRegex()
        return regex.matches(value)
    }
fun pinCodeCheck(value: String):Boolean{
    return value.matches("^[1-9][0-9]{5}\$".toRegex())
}
    fun isValidEmail(value: String): Boolean {

        val regex = Patterns.EMAIL_ADDRESS.toRegex()
        return regex.matches(value)
    }

    fun isPANValid(panNumber: String): Boolean {
        // Regular expression to match the PAN card format
        val pattern = Regex("[A-Z]{5}[0-9]{4}[A-Z]{1}")

        // Check if the provided PAN number matches the pattern
        return pattern.matches(panNumber)
    }

    fun isAadhaarValid(
        aadhaarNumber: String,
        isValid: () -> Unit,
        isNotValid: (errMsg: String) -> Unit,
    ) {
        // Regular expression to match the Aadhaar number format
        val pattern = Regex("^[0-9]{12}\$")

        // Check if the provided Aadhaar number matches the pattern
        if (pattern.matches(aadhaarNumber)) {
            isValid()
        } else {
            isNotValid("Please enter a valid aadhaar number")
        }
    }

    fun isPassportValid(
        passportNumber: String,
        isValid: () -> Unit,
        isNotValid: (errMsg: String) -> Unit,
    ): Boolean {
        // Regular expression to match a generic passport number
        val pattern = Regex("^[A-Z0-9A-Z]{6,15}\$")

        // Check if the provided passport number matches the pattern
        if (pattern.matches(passportNumber)) {
            isValid()
        } else {
            isNotValid("Please enter a valid passport number")
        }
        return pattern.matches(passportNumber)
    }

    fun isVoterIDValid(
        voterID: String,
        isValid: () -> Unit,
        isNotValid: (errMsg: String) -> Unit,
    ): Boolean {
        // Regular expression to match a generic voter ID format (8 to 15 alphanumeric characters)
        val pattern = Regex("^[A-Za-z0-9A-Za-z]{8,15}\$")

        // Check if the provided voter ID matches the pattern
        if (pattern.matches(voterID)) {
            isValid()
        } else {
            isNotValid("Please enter a valid voter ID")

        }
        return pattern.matches(voterID)
    }

    fun isDriverLicenseValid(
        driverLicense: String,
        isValid: () -> Unit,
        isNotValid: (errMsg: String) -> Unit,
    ): Boolean {
        // Replace the regular expression with the specific format and rules for your jurisdiction
        val pattern = Regex("^[A-Z0-9]{8,15}\$")

        // Check if the provided driver's license matches the pattern
        if (pattern.matches(driverLicense)) {
            isValid()
        } else {
            isNotValid("Please enter a valid driver's license number")
        }
        return pattern.matches(driverLicense)
    }

    fun validateVPA(vpa: String): Boolean {
        val pattern = Regex("^[\\w.-]+@[a-zA-Z]+\$")
        return pattern.matches(vpa)
    }

    fun validDob(year: Int, month: Int, date: Int): Boolean {
        var isValid=true
        val calendar = Calendar.getInstance()
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val currentYear=calendar.get(Calendar.YEAR)
        if(year>currentYear){
            isValid=false

        }
        else{
            if(year==currentYear){
                if( month>currentMonth ){
                    isValid=false
                }else
                {
                    if(month==currentMonth){
                        isValid = date <= currentDay
                    }else{
                        isValid=true
                    }

                }
            }else{
                isValid=true
            }


        }
        return isValid
    }

    fun isValidPhoneNumber(text: String, onError: (UiText) -> Unit, onValid: () -> Unit) {
        if (text.isDigitsOnly() && text.length <= 10) {

            if (text.isEmpty()) {
                onError(UiText.StringResource(R.string.empty_field))

            } else {
                if (text[0].toString().toInt() < 6) {

                    onError(UiText.StringResource(R.string.mobile_number_should_start_with_6_or_7_or_8_or_9))

                } else {
                    if (text.length == 10) {
                        onValid()
                    } else {
                        onError(UiText.StringResource(R.string.mobile_number_should_be_10_digits))

                    }

                }
            }


        }
    }

}