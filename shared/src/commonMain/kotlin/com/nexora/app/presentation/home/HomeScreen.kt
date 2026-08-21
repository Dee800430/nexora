package com.nexora.app.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ListAlt
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.nexora.app.presentation.item.CategoryChips
import com.nexora.app.presentation.components.ProductCard
import com.nexora.app.presentation.cart.CartStore
import kotlinx.coroutines.launch
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import com.nexora.app.core.storage.TokenStorage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCartClick: () -> Unit,
    onInvoicesClick: () -> Unit,
    onLogout: () -> Unit
) {

    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    var showMenu by remember { mutableStateOf(false) }

    // Debug state to show/hide test
    var showDebugTest by remember { mutableStateOf(true) }
    var test1Error by remember { mutableStateOf(false) }
    var test2Error by remember { mutableStateOf(false) }

    /*
     * =========================================
     * LOAD HOME
     * =========================================
     */

    LaunchedEffect(Unit) {
        viewModel.loadHome()
    }

    Scaffold(

        topBar = {

            Column {

                TopAppBar(

                    title = {
                        Text(
                            text = "Nexora",
                            fontWeight = FontWeight.Bold
                        )
                    },

                    navigationIcon = {
                        Box {
                            IconButton(
                                onClick = {
                                    showMenu = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Menu,
                                    contentDescription = "Menu"
                                )
                            }

                            DropdownMenu(
                                expanded = showMenu,
                                onDismissRequest = {
                                    showMenu = false
                                }
                            ) {

                                DropdownMenuItem(
                                    text = {
                                        Text("Logout")
                                    },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Default.Logout,
                                            contentDescription = "Logout"
                                        )
                                    },
                                    onClick = {

                                        showMenu = false

                                        // Clear JWT
                                        TokenStorage.clearToken()

                                        // Go to login
                                        onLogout()
                                    }
                                )
                            }
                        }
                    },
                    actions = {
                        IconButton(onClick = onCartClick) {
                            Box {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = "Cart"
                                )

                                if (CartStore.itemCount > 0) {
                                    Text(
                                        text = CartStore.itemCount.toString(),
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .background(Color(0xFFE53935))
                                            .padding(horizontal = 4.dp),
                                        color = Color.White,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        IconButton(onClick = onInvoicesClick) {
                            Icon(
                                imageVector = Icons.Default.ListAlt,
                                contentDescription = "Invoices"
                            )
                        }
                    }
                )

                /*
                 * =================================
                 * SEARCH
                 * =================================
                 */

                OutlinedTextField(
                    value = uiState.searchQuery,
                    onValueChange = {
                        viewModel.updateSearch(it)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    placeholder = {
                        Text("Search products...")
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    },
                    singleLine = true
                )
            }
        },

        /*
         * =========================================
         * BOTTOM NAVIGATION
         * =========================================
         */

        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.Home, contentDescription = "Home")
                    },
                    label = {
                        Text("Home")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onCartClick,
                    icon = {
                        Icon(Icons.Default.Category, contentDescription = "Categories")
                    },
                    label = {
                        Text("Categories")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onCartClick,
                    icon = {
                        Icon(Icons.Default.ShoppingCart, contentDescription = "Cart")
                    },
                    label = {
                        Text("Cart")
                    }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = onInvoicesClick,
                    icon = {
                        Icon(Icons.Default.ListAlt, contentDescription = "Invoices")
                    },
                    label = {
                        Text("Invoices")
                    }
                )
            }
        }

    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
        ) {



            CategoryChips(
                categories = uiState.categories1,
                selectedId = uiState.selectedCategoryId,
                onSelect = { categoryId ->
                    viewModel.selectCategory(categoryId)
                },
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            /*
             * =====================================
             * BANNER
             * =====================================
             */



            Spacer(modifier = Modifier.height(20.dp))

            /*
             * =====================================
             * PRODUCTS TITLE
             * =====================================
             */

            Text(
                text = "Products",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            /*
             * =====================================
             * PRODUCTS
             * =====================================
             */

            if (uiState.isLoadingProducts) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "Loading products...")
                }
            } else if (uiState.filteredProducts.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "No products found", fontWeight = FontWeight.Bold)
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentPadding = PaddingValues(
                        horizontal = 12.dp,
                        vertical = 8.dp
                    ),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(
                        items = uiState.filteredProducts,
                        key = { product ->
                            product.entityItemLineId
                                ?: product.itemId
                                ?: product.hashCode().toLong()
                        }
                    ) { product ->
                        ProductCard(
                            product = product,
                            onAdd = { item ->
                                scope.launch {

                                    CartStore.addToBackendCart(item)
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}