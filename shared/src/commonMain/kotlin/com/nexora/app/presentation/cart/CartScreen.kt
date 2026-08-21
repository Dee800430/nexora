package com.nexora.app.presentation.cart

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexora.app.presentation.components.CartItemRow
import com.nexora.app.presentation.components.CustomerDialog
import com.nexora.app.util.formatPrice
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    onBack: () -> Unit,
    onOrderComplete: () -> Unit
) {
    var showCustomerDialog by remember { mutableStateOf(false) }
    var customerDetails by remember {
        mutableStateOf<CustomerDetails?>(null)
    }
    val cart = CartStore.lines
    val backendCart = CartStore.backendCart
    val backendItems = backendCart?.items.orEmpty()
    val scope = rememberCoroutineScope()

    LaunchedEffect(CartStore.activeBuyerId) {
        CartStore.refreshBackendCart()
    }

    CustomerDialog(
        open = showCustomerDialog,
        onClose = { showCustomerDialog = false },
        onSave = { customer ->
            customerDetails = customer
            scope.launch {
                val placed = CartStore.completeBackendOrder(customer)
                if (placed != null) {
                    showCustomerDialog = false
                    onOrderComplete()
                }
            }
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Cart",
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        if (cart.isEmpty() && backendItems.isEmpty()) {
            EmptyCart(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            )
            return@Scaffold
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF4F6F8)),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CartStore.message?.let { message ->
                item {
                    Text(
                        text = message,
                        modifier = Modifier.padding(horizontal = 16.dp),
                        color = if (message.contains("success", ignoreCase = true) ||
                            message.contains("added", ignoreCase = true)
                        ) {
                            MaterialTheme.colorScheme.primary
                        } else {
                            MaterialTheme.colorScheme.error
                        },
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            item {
                CustomerSummaryCard(
                    customer = customerDetails,
                    onChange = { showCustomerDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                )
            }

            if (backendItems.isNotEmpty()) {
                items(
                    items = backendItems,
                    key = { item -> item.orderLineItemId }
                ) { item ->
                    CartItemRow(
                        item = item,
                        onTempQtyChange = { _, _ -> },
                        onApplyQty = { id, qty ->
                            CartStore.updateBackendQuantity(
                                orderLineItemId = id.toLong(),
                                quantity = qty.toDouble()
                            )
                        },
                        onRemove = {
                            CartStore.removeBackendItem(item)
                        },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            } else {
                items(
                    items = cart,
                    key = { line -> line.id }
                ) { line ->
                    CartLineCard(
                        line = line,
                        onAdd = { CartStore.add(line.product) },
                        onRemoveOne = { CartStore.decrease(line.id) },
                        onDelete = { CartStore.remove(line.id) },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            item {
                CheckoutSummary(
                    subTotal = if (backendCart != null) CartStore.backendSubTotal else CartStore.subTotal,
                    discount = if (backendCart != null) CartStore.backendDiscount else 0.0,
                    tax = if (backendCart != null) CartStore.backendTax else CartStore.tax,
                    grandTotal = if (backendCart != null) CartStore.backendGrandTotal else CartStore.grandTotal,
                    onClear = {
                        scope.launch {
                            CartStore.clearBackendCart()
                        }
                    },
                    onComplete = {
                        showCustomerDialog = true
                    },
                    modifier = Modifier.padding(12.dp)
                )
            }
        }
    }
}

@Composable
private fun CustomerSummaryCard(
    customer: CustomerDetails?,
    onChange: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = customer?.name ?: "Customer not selected",
                    fontWeight = FontWeight.Black
                )
                Text(
                    text = customer?.mobile ?: "Mobile, address and email will be added at checkout",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                customer?.address
                    ?.takeIf { it.isNotBlank() }
                    ?.let {
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
            }

            OutlinedButton(onClick = onChange) {
                Text(if (customer == null) "Add" else "Change")
            }
        }
    }
}

@Composable
private fun EmptyCart(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Color(0xFFF4F6F8)),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = null,
                modifier = Modifier.size(44.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Your cart is empty",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
            Text(
                text = "Add items from home to continue",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun CartLineCard(
    line: CartLine,
    onAdd: () -> Unit,
    onRemoveOne: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = line.product.itemName,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${line.product.itemCode ?: "Item"} | ${formatPrice(line.unitPrice, "Rs. ")}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }

                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Remove item",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFF0F3F6)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 4.dp)
                    ) {
                        IconButton(onClick = onRemoveOne) {
                            Icon(
                                imageVector = Icons.Default.Remove,
                                contentDescription = "Decrease quantity"
                            )
                        }
                        Text(
                            text = line.quantity.toString(),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.width(28.dp)
                        )
                        IconButton(
                            onClick = onAdd,
                            enabled = line.quantity < line.product.balanceQty.toInt()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Increase quantity"
                            )
                        }
                    }
                }

                Text(
                    text = formatPrice(line.total, "Rs. "),
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp
                )
            }
        }
    }
}

@Composable
private fun CheckoutSummary(
    subTotal: Double,
    discount: Double,
    tax: Double,
    grandTotal: Double,
    onClear: () -> Unit,
    onComplete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            SummaryRow("Total Product Price", subTotal)
            SummaryRow("Discount", -discount)
            SummaryRow("Product Tax", tax)
            Divider()
            SummaryRow("Grand Total", grandTotal, true)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onClear,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Clear")
                }
                Button(
                    onClick = onComplete,
                    modifier = Modifier.weight(1.4f)
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null
                    )
                    Spacer(Modifier.width(6.dp))
                    Text("Proceed to Complete")
                }
            }
        }
    }
}

@Composable
private fun SummaryRow(
    label: String,
    amount: Double,
    important: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontWeight = if (important) FontWeight.ExtraBold else FontWeight.Medium
        )
        Text(
            text = formatPrice(amount, "Rs. "),
            fontWeight = if (important) FontWeight.ExtraBold else FontWeight.Bold,
            color = if (important) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
    }
}
