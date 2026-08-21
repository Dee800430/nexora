package com.nexora.app.presentation.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nexora.app.data.api.OrderApi
import com.nexora.app.data.model.order.PlaceOrderRequest
import kotlinx.coroutines.launch


@Composable
fun CartItem(
    modifier: Modifier = Modifier
) {

    val scope =
        rememberCoroutineScope()

    var isPlacingOrder by remember {
        mutableStateOf(false)
    }

    var message by remember {
        mutableStateOf<String?>(null)
    }

    /*
     * ==================================
     * ORDER REQUEST
     * ==================================
     */

    val placeOrder =
        remember {

            PlaceOrderRequest(
                buyerId = 0 ,
                userName = "",
                modifiedBy = 0,
                sourceOrderId = 0,
                comments = "",
                userId = 0,
                isComment = false,
                workflowName = ""
            )
        }

    /*
     * ==================================
     * PLACE ORDER
     * ==================================
     */

    fun handlePlaceOrder() {

        if (isPlacingOrder) {
            return
        }

        scope.launch {

            isPlacingOrder = true
            message = null

            try {

                val response =
                    OrderApi.placeInvoiceOrder(
                        placeOrder
                    )

                println(
                    "Order Placed: $response"
                )

                message =
                    "Order placed successfully!"

            } catch (
                e: Exception
            ) {

                e.printStackTrace()

                message =
                    "Failed to place order"

            } finally {

                isPlacingOrder = false
            }
        }
    }

    /*
     * ==================================
     * UI
     * ==================================
     */

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Text(
            text = "Cart",

            style =
                MaterialTheme.typography
                    .titleLarge
        )

        Spacer(
            modifier =
                Modifier.height(8.dp)
        )

        Divider()

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        /*
         * SUCCESS / ERROR MESSAGE
         */

        message?.let {

            Text(
                text = it,

                modifier =
                    Modifier.padding(
                        bottom = 12.dp
                    ),

                color =
                    if (
                        it.contains(
                            "success",
                            ignoreCase = true
                        )
                    ) {
                        MaterialTheme.colorScheme
                            .primary
                    } else {
                        MaterialTheme.colorScheme
                            .error
                    }
            )
        }

        /*
         * COMPLETE PAYMENT
         */

        Button(
            onClick =
                ::handlePlaceOrder,

            enabled =
                !isPlacingOrder,

            modifier =
                Modifier.fillMaxWidth()
        ) {

            if (isPlacingOrder) {

                CircularProgressIndicator(
                    modifier =
                        Modifier.padding(
                            end = 8.dp
                        ),
                    strokeWidth = 2.dp
                )

                Text(
                    text = "Processing..."
                )

            } else {

                Text(
                    text =
                        "Complete Payment"
                )
            }
        }
    }
}