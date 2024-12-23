package com.isu.common.utils
// Calculate password strength based on various criteria
fun calculatePasswordStrength(password: String): Int {
    var strength = 0

    // Criteria for password strength
    val hasLowercase = password.any { it.isLowerCase() }
    val hasUppercase = password.any { it.isUpperCase() }
    val hasDigit = password.any { it.isDigit() }
    val hasSpecialChar = password.any { !it.isLetterOrDigit() }
    val isLongEnough = password.length >= 8

    // Increment strength based on the presence of different character types
    if (hasLowercase) strength++
    if (hasUppercase) strength++
    if (hasDigit) strength++
    if (hasSpecialChar) strength++
    if (isLongEnough) strength++

    // Ensure the strength is in the range of 0 to 4
    return strength.coerceAtMost(5)
}