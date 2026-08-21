package com.nexora.app.presentation.invoices

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nexora.app.data.model.order.InvoiceResponse
import com.nexora.app.data.model.order.Order
import com.nexora.app.util.formatPrice

private val invoiceStatuses = listOf(
    "NEW",
    "OPEN",
    "APPROVED",
    "DELIVERED",
    "CLOSED",
    "CANCELLED",
    "PAID",
    "PARTIAL_PAID",
    "INV_PARTIAL"
)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun InvoiceListScreen(
    onBack: () -> Unit,
    viewModel: InvoiceListViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadInvoices()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Invoices",
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = "Status: ${uiState.status}",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { viewModel.loadInvoices() },
                        enabled = !uiState.isLoading
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh invoices"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF3F8FC))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AssistChip(
                                    onClick = {},
                                    label = { Text("Total: ${uiState.totalCount}") }
                                )
                                AssistChip(
                                    onClick = {},
                                    label = {
                                        Text("Amount: ${formatPrice(uiState.totalAmount)}")
                                    }
                                )
                                AssistChip(
                                    onClick = {},
                                    label = { Text("Showing: ${uiState.showingCount}") }
                                )
                            }

                            OutlinedTextField(
                                value = uiState.query,
                                onValueChange = viewModel::updateSearch,
                                modifier = Modifier.fillMaxWidth(),
                                singleLine = true,
                                placeholder = { Text("Search invoices") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Search,
                                        contentDescription = "Search"
                                    )
                                }
                            )

                            FlowRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                invoiceStatuses.forEach { status ->
                                    FilterChip(
                                        selected = uiState.status == status,
                                        onClick = { viewModel.updateStatus(status) },
                                        label = { Text(status) }
                                    )
                                }
                            }
                        }
                    }
                }

                if (uiState.isLoading) {
                    item {
                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp)
                        )
                    }
                }

                uiState.errorMessage?.let { message ->
                    item {
                        Text(
                            text = message,
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (!uiState.isLoading && uiState.filteredInvoices.isEmpty()) {
                    item {
                        EmptyInvoices(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp)
                        )
                    }
                }

                items(
                    items = uiState.filteredInvoices,
                    key = { invoice -> invoice.orderId }
                ) { invoice ->
                    InvoiceCard(
                        invoice = invoice,
                        onView = { viewModel.viewInvoice(invoice.orderId) },
                        onPrint = { viewModel.viewInvoice(invoice.orderId) },
                        modifier = Modifier.padding(horizontal = 12.dp)
                    )
                }
            }

            uiState.selectedInvoice?.let { invoice ->
                InvoiceDetailPanel(
                    invoice = invoice,
                    isLoading = uiState.isLoadingDetails,
                    onClose = viewModel::closeInvoice,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Composable
private fun EmptyInvoices(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Icon(
            imageVector = Icons.Default.ReceiptLong,
            contentDescription = null,
            modifier = Modifier.padding(top = 12.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "No invoices found",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "Try another search or status.",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun InvoiceCard(
    invoice: Order,
    onView: () -> Unit,
    onPrint: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Order #${invoice.orderId}",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        text = invoice.userName?.takeIf { it.isNotBlank() } ?: "-",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = invoice.createdDate ?: "-",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = statusColor(invoice.invStatus)
                    ) {
                        Text(
                            text = invoice.invStatus ?: "PAID",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = Color(0xFF08763D),
                            fontWeight = FontWeight.Black,
                            fontSize = 12.sp
                        )
                    }
                    Text(
                        text = formatPrice(invoice.grandAmt),
                        modifier = Modifier.padding(top = 6.dp),
                        fontWeight = FontWeight.Black,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = invoice.description ?: "-",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onView,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Visibility,
                        contentDescription = null
                    )
                    Text(
                        text = "View",
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
                OutlinedButton(
                    onClick = onPrint,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        imageVector = Icons.Default.Print,
                        contentDescription = null
                    )
                    Text(
                        text = "Print",
                        modifier = Modifier.padding(start = 6.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun InvoiceDetailPanel(
    invoice: InvoiceResponse,
    isLoading: Boolean,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.padding(12.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = invoice.invoiceNo ?: "Invoice #${invoice.orderId}",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp
                    )
                    Text(
                        text = "${invoice.customer.customerName} | ${invoice.status}",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
                OutlinedButton(onClick = onClose) {
                    Text("Close")
                }
            }

            if (isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }

            HorizontalDivider()

            Text(
                text = "Customer",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp
            )
            Text(
                text = invoice.customer.customerName.ifBlank { "-" },
                fontWeight = FontWeight.Bold
            )
            InvoiceInfoLine("Mobile", invoice.customer.mobile)
            InvoiceInfoLine("Email", invoice.customer.email)
            InvoiceInfoLine("Address", invoice.customer.address)

            Text(
                text = "Company",
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Black,
                fontSize = 12.sp
            )
            Text(
                text = invoice.company.companyName.ifBlank { "-" },
                fontWeight = FontWeight.Bold
            )
            InvoiceInfoLine("Mobile", invoice.company.mobile)
            InvoiceInfoLine("Email", invoice.company.email)
            InvoiceInfoLine("Address", invoice.company.address)
            InvoiceInfoLine("GST", invoice.company.gstNo)

            HorizontalDivider()

            invoice.items.take(5).forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${item.itemName} x ${item.itemQty}",
                        modifier = Modifier.weight(1f),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = formatPrice(item.grandAmt),
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (invoice.items.size > 5) {
                Text(
                    text = "+${invoice.items.size - 5} more items",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontSize = 12.sp
                )
            }

            HorizontalDivider()
            AmountRow("Subtotal", invoice.subTotal)
            AmountRow("Discount", -invoice.discount)
            AmountRow("Tax", invoice.tax)
            AmountRow("Grand Total", invoice.grandTotal, important = true)
        }
    }
}

@Composable
private fun InvoiceInfoLine(
    label: String,
    value: String?
) {
    if (value.isNullOrBlank()) return

    Text(
        text = "$label: $value",
        color = MaterialTheme.colorScheme.onSurfaceVariant,
        fontSize = 12.sp,
        maxLines = 2,
        overflow = TextOverflow.Ellipsis
    )
}

@Composable
private fun AmountRow(
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
            fontWeight = if (important) FontWeight.Black else FontWeight.Medium
        )
        Text(
            text = formatPrice(amount),
            fontWeight = if (important) FontWeight.Black else FontWeight.Bold,
            color = if (important) MaterialTheme.colorScheme.primary else Color.Unspecified
        )
    }
}

private fun statusColor(status: String?): Color {
    return when (status) {
        "CANCELLED" -> Color(0xFFFFEBEE)
        "PARTIAL_PAID", "INV_PARTIAL" -> Color(0xFFFFF8E1)
        "PAID", "DELIVERED", "APPROVED" -> Color(0xFFE7F7EE)
        else -> Color(0xFFEAF3FF)
    }
}
