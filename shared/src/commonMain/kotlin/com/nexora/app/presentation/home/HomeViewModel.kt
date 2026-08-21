package com.nexora.app.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nexora.app.data.api.ItemApiService
import com.nexora.app.data.model.item.ItemDto
import com.nexora.app.data.model.item.ItemLookup
import com.nexora.app.data.model.item.Lookup
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


data class HomeUiState(

    // Categories
    val categories1: List<Lookup> = emptyList(),

    // All products returned by API
    val products: List<ItemDto> = emptyList(),

    // Products after search
    val filteredProducts: List<ItemDto> = emptyList(),

    // Search
    val searchQuery: String = "",

    // Selected category
    val selectedCategoryId: Long? = null,

    // Loading
    val isLoadingCategories: Boolean = false,
    val isLoadingProducts: Boolean = false,

    // Error
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState =
        MutableStateFlow(HomeUiState())

    val uiState: StateFlow<HomeUiState> =
        _uiState.asStateFlow()


    /*
     * =====================================================
     * LOAD HOME
     * =====================================================
     */

    fun loadHome() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(
                isLoadingCategories = true,
                errorMessage = null
            )

            try {

                val response =
                    ItemApiService.getLookups(
                        "ItemCategory1"
                    )

                _uiState.value =
                    _uiState.value.copy(
                        categories1 =
                            response.category1,
                        isLoadingCategories = false
                    )

                // Load all products initially
                loadProducts(null)

            } catch (error: Exception) {

                error.printStackTrace()

                _uiState.value =
                    _uiState.value.copy(
                        isLoadingCategories = false,
                        errorMessage =
                            error.message
                                ?: "Failed to load home"
                    )
            }
        }
    }


    /*
     * =====================================================
     * LOAD PRODUCTS
     * =====================================================
     */

    fun loadProducts(
        categoryId: Long? = null
    ) {

        viewModelScope.launch {

            _uiState.value =
                _uiState.value.copy(
                    isLoadingProducts = true,
                    errorMessage = null,
                    selectedCategoryId = categoryId
                )

            try {

                val products =
                    ItemApiService.getAllItemLines(
                        itemCategory1ID = categoryId
                    )

                val filtered =
                    filterProducts(
                        products,
                        _uiState.value.searchQuery
                    )

                _uiState.value =
                    _uiState.value.copy(
                        products = products,
                        filteredProducts = filtered,
                        isLoadingProducts = false
                    )

            } catch (error: Exception) {

                error.printStackTrace()

                _uiState.value =
                    _uiState.value.copy(
                        products = emptyList(),
                        filteredProducts = emptyList(),
                        isLoadingProducts = false,
                        errorMessage =
                            error.message
                                ?: "Failed to load products"
                    )
            }
        }
    }


    /*
     * =====================================================
     * CATEGORY
     * =====================================================
     */

    fun selectCategory(
        categoryId: Long?
    ) {

        loadProducts(categoryId)
    }


    /*
     * =====================================================
     * SEARCH
     * =====================================================
     */

    fun updateSearch(
        query: String
    ) {

        val products =
            _uiState.value.products

        val filtered =
            filterProducts(
                products,
                query
            )

        _uiState.value =
            _uiState.value.copy(
                searchQuery = query,
                filteredProducts = filtered
            )
    }


    /*
     * =====================================================
     * FILTER PRODUCTS
     * =====================================================
     */

    private fun filterProducts(
        products: List<ItemDto>,
        query: String
    ): List<ItemDto> {

        val q =
            query.trim().lowercase()

        if (q.isEmpty()) {
            return products
        }

        return products.filter { product ->

            val text = listOf(
                product.itemName,
                product.itemCode,
                product.itemNumber,
                product.itemId.toString(),
                product.uom
            )
                .joinToString(" ")
                .lowercase()

            text.contains(q)
        }
    }
}
