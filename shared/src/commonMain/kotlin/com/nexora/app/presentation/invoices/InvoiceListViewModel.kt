package com.nexora.app.presentation.invoices

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexora.app.data.model.order.InvoiceResponse
import com.nexora.app.data.model.order.Order
import com.nexora.app.data.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class InvoiceListUiState(
    val status: String = "PAID",
    val query: String = "",
    val invoices: List<Order> = emptyList(),
    val filteredInvoices: List<Order> = emptyList(),
    val selectedInvoice: InvoiceResponse? = null,
    val isLoading: Boolean = false,
    val isLoadingDetails: Boolean = false,
    val errorMessage: String? = null
) {
    val totalCount: Int
        get() = invoices.size

    val showingCount: Int
        get() = filteredInvoices.size

    val totalAmount: Double
        get() = invoices.sumOf { it.grandAmt }

}

class InvoiceListViewModel : ViewModel() {

    private val repository = OrderRepository()

    private val _uiState = MutableStateFlow(InvoiceListUiState())
    val uiState: StateFlow<InvoiceListUiState> = _uiState.asStateFlow()

    fun loadInvoices(status: String = _uiState.value.status) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                status = status,
                isLoading = true,
                errorMessage = null,
                selectedInvoice = null
            )

            try {
                val invoices = repository.getInvoicesByStatus(status)
                _uiState.value = _uiState.value.copy(
                    invoices = invoices,
                    filteredInvoices = filterInvoices(invoices, _uiState.value.query),
                    isLoading = false
                )
            } catch (error: Exception) {
                error.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    invoices = emptyList(),
                    filteredInvoices = emptyList(),
                    isLoading = false,
                    errorMessage = error.message ?: "Failed to load invoices"
                )
            }
        }
    }

    fun updateStatus(status: String) {
        loadInvoices(status)
    }

    fun updateSearch(query: String) {
        val invoices = _uiState.value.invoices
        _uiState.value = _uiState.value.copy(
            query = query,
            filteredInvoices = filterInvoices(invoices, query)
        )
    }

    fun viewInvoice(orderId: Long) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingDetails = true,
                errorMessage = null
            )

            try {
                val invoice = repository.getInvoiceLineItems(orderId)
                _uiState.value = _uiState.value.copy(
                    selectedInvoice = invoice,
                    isLoadingDetails = false
                )
            } catch (error: Exception) {
                error.printStackTrace()
                _uiState.value = _uiState.value.copy(
                    isLoadingDetails = false,
                    errorMessage = error.message ?: "Failed to load invoice details"
                )
            }
        }
    }

    fun closeInvoice() {
        _uiState.value = _uiState.value.copy(selectedInvoice = null)
    }

    private fun filterInvoices(
        invoices: List<Order>,
        query: String
    ): List<Order> {
        val q = query.trim().lowercase()
        if (q.isEmpty()) return invoices

        return invoices.filter { invoice ->
            listOf(
                invoice.orderId,
                invoice.userName,
                invoice.invStatus,
                invoice.createdDate,
                invoice.description,
                invoice.grandAmt,
                invoice.subTotalAmt,
                invoice.totalOrderLiDiscAmt

            )
                .joinToString(" ")
                .lowercase()
                .contains(q)
        }
    }
}
