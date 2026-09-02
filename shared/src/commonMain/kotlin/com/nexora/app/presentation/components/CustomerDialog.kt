package com.nexora.app.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nexora.app.data.model.user.WalkInCustomerDto
import com.nexora.app.data.repository.UserRepository
import com.nexora.app.presentation.cart.CustomerDetails
import kotlinx.coroutines.launch

@Composable
fun CustomerDialog(
    open: Boolean,
    onClose: () -> Unit,
    onSave: (CustomerDetails) -> Unit
) {
    if (!open) return

    val repository = remember { UserRepository() }
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var mobile by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var userId by remember { mutableStateOf<Long?>(null) }
    var message by remember { mutableStateOf<String?>(null) }
    var isSearching by remember { mutableStateOf(false) }
    var isSaving by remember { mutableStateOf(false) }

    fun clearFoundCustomer() {
        userId = null
        email = ""
        address = ""
        message = null
    }

    AlertDialog(
        onDismissRequest = onClose,
        title = {
            Text(
                text = "Customer Details",
                fontWeight = FontWeight.Black
            )
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = mobile,
                    onValueChange = { value ->
                        mobile = value.filter { it.isDigit() }.take(10)
                        name = ""
                        clearFoundCustomer()
                    },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Mobile Number") }
                )

                OutlinedButton(
                    onClick = {
                        if (mobile.length != 10) {
                            message = "Enter a valid 10 digit mobile number"
                            return@OutlinedButton
                        }

                        scope.launch {
                            isSearching = true
                            message = null

                            try {
                                val customer = repository.searchCustomer(mobile)
                                userId = customer.userId
                                name = customer.userName.orEmpty()
                                email = customer.email.orEmpty()
                                address = customer.displayAddress
                                if (name.isBlank()) {
                                    message = "Customer found, add missing name"
                                } else {
                                    message = "Existing customer found"
                                }
                            } catch (_: Exception) {
                                userId = null
                                message = "New customer"
                            } finally {
                                isSearching = false
                            }
                        }
                    },
                    enabled = !isSearching,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(if (isSearching) "Searching..." else "Search Customer")
                }

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Customer Name") }
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    label = { Text("Email") }
                )

                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 2,
                    label = { Text("Address") }
                )

                message?.let {
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    when {
                        isSaving -> {
                            return@Button
                        }

                        mobile.length != 10 -> {
                            message = "Enter a valid 10 digit mobile number"
                        }

                        name.isBlank() -> {
                            message = "Enter customer name"
                        }

                        else -> {
                            scope.launch {
                                isSaving = true
                                message = null

                                try {
                                    val customer =
                                        repository
                                            .createOrUpdateWalkInCustomer(
                                                WalkInCustomerDto(
                                                    userName = name,
                                                    mobile = mobile
                                                )
                                            )

                                    onSave(
                                        CustomerDetails(
                                            userId =
                                                customer.userId
                                                    ?: userId,
                                            name =
                                                customer.userName
                                                    ?: name,
                                            mobile =
                                                customer.mobile
                                                    ?: mobile,
                                            email = email,
                                            address = address
                                        )
                                    )
                                } catch (error: Exception) {
                                    error.printStackTrace()
                                    message =
                                        error.message
                                            ?: "Failed to save customer"
                                } finally {
                                    isSaving = false
                                }
                            }
                        }
                    }
                }
            ) {
                Text(
                    if (isSaving) {
                        "Saving..."
                    } else {
                        "Save & Continue"
                    }
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onClose) {
                Text("Cancel")
            }
        }
    )
}
