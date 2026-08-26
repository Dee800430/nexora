package com.nexora.app.presentation.cart

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.nexora.app.data.model.item.ItemDto
import com.nexora.app.data.model.order.Order
import com.nexora.app.data.model.order.OrderRequest
import com.nexora.app.data.model.order.PlaceOrderRequest
import com.nexora.app.data.repository.DiscountRepository
import com.nexora.app.data.repository.OrderRepository
import com.nexora.app.data.repository.UserRepository
import kotlin.math.round

data class CartLine(
    val product: ItemDto,
    val quantity: Int
) {
    val id: Long
        get() = product.entityItemLineId ?: product.itemId ?: product.hashCode().toLong()

    val unitPrice: Double
        get() = product.saleRate

    val total: Double
        get() = unitPrice * quantity
}

data class ShopInvoice(
    val invoiceNo: String,
    val customerName: String,
    val customerMobile: String = "",
    val customerEmail: String = "",
    val customerAddress: String = "",
    val lines: List<CartLine>,
    val subTotal: Double,
    val tax: Double,
    val grandTotal: Double
)

data class CustomerDetails(
    val userId: Long? = null,
    val name: String,
    val mobile: String,
    val email: String = "",
    val address: String = ""
)

object CartStore {
    private val orderRepository = OrderRepository()
    private val userRepository = UserRepository()
    private val cartLines = mutableStateListOf<CartLine>()
    private val invoiceLines = mutableStateListOf<ShopInvoice>()
    private val discountRepository = DiscountRepository()
    private var invoiceCounter = 1001


    // Discount state
    var isDiscountLoading by mutableStateOf(false)
        private set

    var discountMessage by mutableStateOf<String?>(null)

    var activeBuyerId by mutableStateOf<Long?>(null)
        private set

    var backendCart by mutableStateOf<Order?>(null)
        private set

    var isSyncing by mutableStateOf(false)
        private set

    var message by mutableStateOf<String?>(null)
        private set
    var editMode by mutableStateOf(false)
        private set

    var isNewUserSession by mutableStateOf(true)
        private set

    val lines: List<CartLine>
        get() = cartLines

    val invoices: List<ShopInvoice>
        get() = invoiceLines

    val itemCount: Int
        get() = backendCart?.items?.sumOf { it.itemQty.toInt() }
            ?: cartLines.sumOf { it.quantity }

    val subTotal: Double
        get() = roundMoney(cartLines.sumOf { it.total })

    val tax: Double
        get() = roundMoney(subTotal * 0.05)

    val grandTotal: Double
        get() = roundMoney(subTotal + tax)

    val backendSubTotal: Double
        get() = backendCart?.let { cart ->
            cart.totalAmt
                ?: cart.subTotalAmt
                ?: cart.items.sumOf { item ->
                    if (item.subTotalAmt > 0.0) {
                        item.subTotalAmt
                    } else {
                        item.itemQty * item.salePrice
                    }
                }
        } ?: 0.0

    val backendDiscount: Double
        get() = backendCart?.let { cart ->
            cart.totalOrderLiDiscAmt ?: cart.discountAmt ?: cart.discount ?: 0.0
        } ?: 0.0

    val backendTax: Double
        get() = backendCart?.let { cart ->
            cart.totalTaxAmt ?: cart.taxAmt ?: cart.tax ?: 0.0
        } ?: 0.0

    val backendGrandTotal: Double
        get() = backendCart?.let { cart ->
            if (cart.grandAmt > 0.0) {
                cart.grandAmt
            } else {
                (backendSubTotal - backendDiscount + backendTax)
                    .coerceAtLeast(0.0)
            }
        } ?: 0.0
    fun startNewSale() {
        activeBuyerId = null
        backendCart = null

        editMode = false
        isNewUserSession = true

        message = null
    }
    fun startEditOrder(order: Order) {
        backendCart = order
        activeBuyerId = order.buyerId

        editMode = true
        isNewUserSession = false

        message = null
    }
    suspend fun addToBackendCart(product: ItemDto): Boolean {
        isSyncing = true
        message = null

        try {
            val buyerId = ensureBuyerId()

            val requestOrderId =
                if (editMode && !isNewUserSession) {
                    backendCart?.orderId
                } else {
                    null
                }

            val request = OrderRequest(
                orderId = requestOrderId,

                buyerId = buyerId,
                sellerId = product.userId ?: 0,
                itemName = product.itemName,
                uom = product.uom,
                itemId = product.itemId,
                itemCode = product.itemCode,
                quantity = 1.0,
                price = product.saleRate,
                entityItemLineId = product.entityItemLineId ?: 0,
                entityItemLineStockId = product.entityItemLineStockId ?: 0,
                userId = product.userId ?: buyerId,
                siteId = 0
            )

            orderRepository.allocateStock(request)

            backendCart = orderRepository.viewCart(buyerId)
            refreshBackendCart()

            message = "Added to cart"
            return true

        } catch (error: Exception) {
            error.printStackTrace()
            message = error.message ?: "Failed to add item"
            return false
        } finally {
            isSyncing = false
        }
    }

    suspend fun clearBackendCart() {
        val items = backendCart?.items.orEmpty()
        if (items.isEmpty()) {
            clear()
            return
        }

        isSyncing = true
        try {
            items.forEach { item ->
                removeBackendItem(item)
            }
            backendCart = null
        } finally {
            isSyncing = false
        }
    }

    suspend fun refreshBackendCart() {
        val buyerId = activeBuyerId ?: return
        backendCart = orderRepository.viewCart(buyerId)
    }

    suspend fun increaseCartQty(orderLineItemId: Long) {
        if (isSyncing) return

        isSyncing = true

        try {
            backendCart =
                orderRepository.updateQuantity(
                    orderLineItemId,
                    1.0
                )
        } finally {
            isSyncing = false
        }
    }
    suspend fun decreaseCartQty(orderLineItemId: Long) {
        if (isSyncing) return

        isSyncing = true

        try {
            backendCart =
                orderRepository.reduceQty(
                    orderLineItemId,
                    1.0
                )
        } finally {
            isSyncing = false
        }
    }
    suspend fun removeBackendItem(item: com.nexora.app.data.model.order.OrderItem) {
        val cart = backendCart ?: return
        val request = OrderRequest(
            buyerId = cart.buyerId,
            sellerId = cart.sellerId,
            itemName = item.itemName.orEmpty(),
            quantity = item.itemQty,
            price = item.salePrice,
            entityItemLineId = item.entityItemLineId,
            entityItemLineStockId = 0,
            userId = cart.buyerId,
            siteId = 0,
            orderLineItemId = item.orderLineItemId
        )

        isSyncing = true
        try {
            backendCart = orderRepository.removeFromCart(request)
            refreshBackendCart()
        } finally {
            isSyncing = false
        }
    }

    suspend fun completeBackendOrder(customer: CustomerDetails): Order? {
        val cart = backendCart ?: return null
        val buyerId = activeBuyerId ?: cart.buyerId

        isSyncing = true
        message = null

        try {
            val finalBuyerId: Long
            val finalOrderId: Long
            val finalUserName: String

            if (customer.userId != null && customer.userId != buyerId) {
                val changedInvoice = orderRepository.changeBuyer(
                    orderId = cart.orderId,
                    buyerId = customer.userId,
                    userName = customer.name
                )

                finalBuyerId = changedInvoice.buyerId ?: customer.userId
                finalOrderId = changedInvoice.orderId
                finalUserName = changedInvoice.userName ?: customer.name
            } else {
                userRepository.updateTempCustomer(
                    userId = buyerId,
                    body = com.nexora.app.data.model.user.TempCustomerRequest(
                        userName = customer.name,
                        mobile = customer.mobile
                    )
                )
                finalBuyerId = buyerId
                finalOrderId = cart.orderId
                finalUserName = customer.name
            }

            val placed = orderRepository.placeInvoiceOrder(
                PlaceOrderRequest(
                    buyerId = finalBuyerId,
                    sourceOrderId = finalOrderId,
                    userName = finalUserName,
                    comments = customer.address,
                    workflowName = "",
                    isComment = false,
                    modifiedBy = customer.userId ?: finalBuyerId,
                    userId = customer.userId ?: finalBuyerId
                )
            )

            completeOrder(customer)
            backendCart = null
            activeBuyerId = null
            message = "Order placed successfully"
            return placed
        } catch (error: Exception) {
            error.printStackTrace()
            message = error.message ?: "Failed to place order"
            return null
        } finally {
            isSyncing = false
        }
    }

    private suspend fun ensureBuyerId(): Long {
        activeBuyerId?.let { return it }

        val customer = userRepository.createTempCustomer()
        val buyerId = customer.userId ?: error("Temp customer did not return userId")
        activeBuyerId = buyerId
        return buyerId
    }

    fun add(product: ItemDto) {
        val id = product.entityItemLineId ?: product.itemId ?: product.hashCode().toLong()
        val index = cartLines.indexOfFirst { it.id == id }
        val stock = product.balanceQty.toInt().coerceAtLeast(0)

        if (index == -1) {
            if (stock > 0) {
                cartLines.add(CartLine(product, 1))
            }
            return
        }

        val existing = cartLines[index]
        if (existing.quantity < stock) {
            cartLines[index] = existing.copy(quantity = existing.quantity + 1)
        }
    }

    fun decrease(lineId: Long) {
        val index = cartLines.indexOfFirst { it.id == lineId }
        if (index == -1) return

        val existing = cartLines[index]
        if (existing.quantity <= 1) {
            cartLines.removeAt(index)
        } else {
            cartLines[index] = existing.copy(quantity = existing.quantity - 1)
        }
    }

    fun remove(lineId: Long) {
        cartLines.removeAll { it.id == lineId }
    }

    fun clear() {
        cartLines.clear()
    }

    fun completeOrder(customer: CustomerDetails): ShopInvoice? {
        if (cartLines.isEmpty()) return null

        val invoice = ShopInvoice(
            invoiceNo = "NX-${invoiceCounter++}",
            customerName = customer.name.ifBlank { "Walk-in Customer" },
            customerMobile = customer.mobile,
            customerEmail = customer.email,
            customerAddress = customer.address,
            lines = cartLines.map { it.copy() },
            subTotal = subTotal,
            tax = tax,
            grandTotal = grandTotal
        )

        invoiceLines.add(0, invoice)
        cartLines.clear()
        return invoice
    }

    /**
     * Apply discount to an order
     */
    suspend fun applyItemDiscount(
        orderLineItemId: Long,
        discountType: String,
        discountValue: Double
    ): Boolean {
        isDiscountLoading = true
        discountMessage = null

        try {
            val result = discountRepository.applyItemDiscount(
                orderLineItemId = orderLineItemId,
                discountType = discountType,
                discountValue = discountValue
            )

            if (result.success) {
                refreshBackendCart()
                discountMessage = result.message ?: "Discount applied successfully"
                return true
            } else {
                discountMessage = result.message ?: "Failed to apply discount"
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            discountMessage = e.message ?: "Error applying discount"
            return false
        } finally {
            isDiscountLoading = false
        }
    }

    /**
     * Remove discount from an item
     */
    suspend fun removeItemDiscount(orderLineItemId: Long): Boolean {
        isDiscountLoading = true
        discountMessage = null

        try {
            val result = discountRepository.removeItemDiscount(orderLineItemId)

            if (result.success) {
                refreshBackendCart()
                discountMessage = result.message ?: "Discount removed successfully"
                return true
            } else {
                discountMessage = result.message ?: "Failed to remove discount"
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            discountMessage = e.message ?: "Error removing discount"
            return false
        } finally {
            isDiscountLoading = false
        }
    }

    /**
     * Apply discount to an order
     */
    suspend fun applyOrderDiscount(
        discountType: String,
        discountValue: Double,
        applyToAllItems: Boolean = false
    ): Boolean {
        val cart = backendCart ?: return false
        isDiscountLoading = true
        discountMessage = null

        try {
            val result = discountRepository.applyOrderDiscount(
                orderId = cart.orderId,
                discountType = discountType,
                discountValue = discountValue,
                applyToAllItems = applyToAllItems
            )

            if (result.success) {
                refreshBackendCart()
                discountMessage = result.message ?: "Order discount applied successfully"
                return true
            } else {
                discountMessage = result.message ?: "Failed to apply order discount"
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            discountMessage = e.message ?: "Error applying order discount"
            return false
        } finally {
            isDiscountLoading = false
        }
    }

    /**
     * Remove all discounts from an order
     */
    suspend fun removeAllOrderDiscounts(): Boolean {
        val cart = backendCart ?: return false
        isDiscountLoading = true
        discountMessage = null

        try {
            val result = discountRepository.removeAllOrderDiscounts(cart.orderId)

            if (result.success) {
                refreshBackendCart()
                discountMessage = result.message ?: "All discounts removed successfully"
                return true
            } else {
                discountMessage = result.message ?: "Failed to remove discounts"
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            discountMessage = e.message ?: "Error removing discounts"
            return false
        } finally {
            isDiscountLoading = false
        }
    }

    /**
     * Check if order has discounts
     */
    fun hasOrderDiscount(): Boolean {
        return (backendCart?.totalOrderLiDiscAmt ?: 0.0) > 0
    }

    /**
     * Get order discount amount
     */
    fun getOrderDiscountAmount(): Double {
        return backendCart?.totalOrderLiDiscAmt ?: 0.0
    }
}


private fun roundMoney(value: Double): Double {
    return round(value * 100.0) / 100.0
}
