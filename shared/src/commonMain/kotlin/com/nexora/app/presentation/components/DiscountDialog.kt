package com.nexora.app.presentation.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.nexora.app.util.formatPrice

@Composable
fun DiscountDialog(
    open: Boolean,
    onClose: () -> Unit,
    onApply: (discountType: String, value: Double, applyToAllItems: Boolean) -> Unit,
    type: String = "PERCENTAGE", // "PERCENTAGE" or "FIXED"
    currentDiscount: Double = 0.0,
    itemName: String? = null,
    isOrderDiscount: Boolean = false,
    isLoading: Boolean = false
) {
    if (!open) return

    var discountType by remember { mutableStateOf(type) }
    var discountValue by remember { mutableStateOf("") }
    var applyToAllItems by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(
                text = if (isOrderDiscount) {
                    "Apply Discount to Order"
                } else {
                    "Apply Discount to ${itemName ?: "Item"}"
                },
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Discount Type Selection
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = discountType == "PERCENTAGE",
                        onClick = {
                            discountType = "PERCENTAGE"
                            errorMessage = null
                        },
                        label = { Text("Percentage") },
                        modifier = Modifier.weight(1f)
                    )
                    FilterChip(
                        selected = discountType == "FIXED",
                        onClick = {
                            discountType = "FIXED"
                            errorMessage = null
                        },
                        label = { Text("Fixed Amount") },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Discount Value Input
                OutlinedTextField(
                    value = discountValue,
                    onValueChange = {
                        discountValue = it
                        errorMessage = null
                    },
                    label = {
                        Text(
                            if (discountType == "PERCENTAGE") {
                                "Discount Percentage (%)"
                            } else {
                                "Discount Amount (₹)"
                            }
                        )
                    },
                    placeholder = {
                        Text(
                            if (discountType == "PERCENTAGE") {
                                "Enter percentage (0-100)"
                            } else {
                                "Enter amount"
                            }
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = errorMessage != null,
                    supportingText = {
                        if (errorMessage != null) {
                            Text(
                                text = errorMessage!!,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                // Apply to all items (only for order discount)
                if (isOrderDiscount) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Switch(
                            checked = applyToAllItems,
                            onCheckedChange = { applyToAllItems = it }
                        )
                        Text(
                            text = "Apply discount proportionally to all items",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                // Current discount display
                if (currentDiscount > 0) {
                    Text(
                        text = formatPrice( currentDiscount,"d"),

                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val value = discountValue.toDoubleOrNull()
                    when {
                        value == null || value <= 0 -> {
                            errorMessage = "Please enter a valid discount value"
                        }
                        discountType == "PERCENTAGE" && value > 100 -> {
                            errorMessage = "Percentage discount cannot exceed 100%"
                        }
                        else -> {
                            onApply(discountType, value, applyToAllItems)
                            onClose()
                        }
                    }
                },
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Applying...")
                } else {
                    Text("Apply Discount")
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onClose,
                enabled = !isLoading
            ) {
                Text("Cancel")
            }
        }
    )
}