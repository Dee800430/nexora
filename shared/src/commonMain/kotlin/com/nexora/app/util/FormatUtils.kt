
package com.nexora.app.util

import kotlin.math.abs
import kotlin.math.round

/**
 * Formats a price in a Kotlin Multiplatform-safe way.
 *
 * Examples:
 * 2999.0  -> "₹2999.00"
 * 4350.0  -> "₹4350.00"
 * 499.0   -> "₹499.00"
 * null    -> "₹0.00"
 */
fun formatPrice(
    price: Double?,
    currencySymbol: String = "₹"
): String {

    val value = price ?: 0.0

    // Round to 2 decimal places
    val rounded = round(value * 100.0) / 100.0

    val integerPart = rounded.toLong()

    val decimalPart =
        (abs(rounded - integerPart) * 100)
            .toLong()

    val decimalText =
        decimalPart
            .toString()
            .padStart(2, '0')

    return "$currencySymbol$integerPart.$decimalText"
}

