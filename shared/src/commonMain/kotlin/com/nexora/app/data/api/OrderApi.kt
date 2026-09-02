package com.nexora.app.data.api

import com.nexora.app.core.network.ApiService
import com.nexora.app.data.model.order.ApplyDiscountRequest
import com.nexora.app.data.model.order.CreateInvoiceRequest
import com.nexora.app.data.model.order.DiscountResult
import com.nexora.app.data.model.order.InvoiceResponse
import com.nexora.app.data.model.order.Order
import com.nexora.app.data.model.order.OrderRequest
import com.nexora.app.data.model.order.PlaceOrderRequest
import com.nexora.app.data.model.order.PurchaseOrderItem
import com.nexora.app.data.model.order.UpdatePurchaseItemRequest
import io.ktor.http.encodeURLParameter

/**
 * API layer for Order / Invoice Service.
 *
 * Contains only network operations.
 *
 * Business logic belongs in repositories and ViewModels.
 */
object OrderApi {

    /*
     * =========================================================
     * SALES INVOICE / SALES ORDER
     * =========================================================
     */

    /**
     * Allocate stock and add an item to the sales cart.
     */
    suspend fun allocateStock(
        order: OrderRequest
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/add",
            method = "POST",
            body = order
        )
    }

    /**
     * Get the current sales cart for a buyer.
     */
    suspend fun viewCart(

    ): Order {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/cart",
            method = "GET"
        )
    }

    /**
     * Remove an item from the sales cart.
     */
    suspend fun removeFromCart(
        orderRequest: OrderRequest
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/cart/remove",
            method = "POST",
            body = orderRequest
        )
    }

    /**
     * Place the sales invoice/order.
     */
    suspend fun placeInvoiceOrder(
        request: PlaceOrderRequest
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/place-order",
            method = "POST",
            body = request
        )
    }

    /**
     * Update quantity of an item in the sales cart.
     */
    suspend fun updateQuantity(
        orderLineItemId: Long,
        quantity: Double
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/update-qty/$orderLineItemId" +
                        "?quantity=$quantity",
            method = "POST"
        )
    }

    suspend fun  reduceQty(
        orderLineItemId: Long,
        quantity: Double
    ): Order {
        return ApiService.request(
            service = "order",
            endpoint = "/inv/reduce-qty/$orderLineItemId" +
                     "?quantity=$quantity ",
            method = "POST"
        )
    }

    /**
     * Get sales invoices filtered by status.
     */
    suspend fun getInvoicesByStatus(
        status: String
    ): List<Order> {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/invoices/list?status=$status",
            method = "GET"
        )
    }

    /*
     * =========================================================
     * PURCHASE ORDER
     * =========================================================
     */

    /**
     * Add an item to the purchase-order cart.
     */
    suspend fun addToPurchaseOrder(
        request: OrderRequest
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/order/add-to-cart",
            method = "POST",
            body = request
        )
    }

    /**
     * Get the purchase-order cart for a seller.
     */
    suspend fun getPurchaseCart(
        sellerId: Long
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/order/cart/$sellerId",
            method = "GET"
        )
    }

    /**
     * Load an existing order into the purchase cart.
     */
    suspend fun getFromInvoicesToCart(
        orderId: Long
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/order/cart/order/$orderId",
            method = "GET"
        )
    }

    /**
     * Load an invoice into the purchase cart for editing.
     */
    suspend fun getFromInvoiceToUpdate(
        orderId: Long
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/invoice/cart/$orderId",
            method = "GET"
        )
    }

    /**
     * Update quantity of a purchase-order line.
     */
    suspend fun updatePurchaseQty(
        orderLineItemId: Long,
        quantity: Double
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/order/update-qty/$orderLineItemId" +
                        "?quantity=$quantity",
            method = "POST"
        )
    }

    /**
     * Remove a purchase-order item by line-item ID.
     */
    suspend fun removePurchaseItem(
        id: Long
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/order/remove/$id",
            method = "DELETE"
        )
    }

    /**
     * Remove an item from the purchase cart.
     */
    suspend fun removePurchaseFromCart(
        request: OrderRequest
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/order/cart/remove",
            method = "POST",
            body = request
        )
    }

    /**
     * Submit the purchase order.
     */
    suspend fun submitPurchaseOrder(
        orderId: Long,
        sellerId: Long
    ): Order {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/order/submit" +
                        "?orderId=$orderId" +
                        "&sellerId=$sellerId",
            method = "POST"
        )
    }

    /**
     * Get purchase invoices for a buyer.
     */
    suspend fun getPurchaseInvoicesByStatus(
        buyerId: Long,
        status: String
    ): List<Order> {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/order/invoices/$buyerId" +
                        "?status=$status",
            method = "GET"
        )
    }

    /**
     * Get purchase invoices for the seller side.
     */
    suspend fun getPurchaseInvoicesByStatusForSeller(
        status: String
    ): List<Order> {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/order/invoices/order/" +
                        "?status=$status",
            method = "GET"
        )
    }

    /**
     * Get purchase history.
     */
    suspend fun getPurchaseHistory(
        buyerId: Long
    ): List<Order> {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/order/history",
            method = "GET"
        )
    }

    /**
     * Get individual items belonging to a purchase order.
     */
    suspend fun getPurchaseOrderItems(
        orderId: Long
    ): List<PurchaseOrderItem> {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/order/items/$orderId",
            method = "GET"
        )
    }

    /**
     * Update a purchase-order item.
     */
    suspend fun updatePurchaseItem(
        orderLineItemId: Long,
        request: UpdatePurchaseItemRequest
    ): PurchaseOrderItem {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/order/update-item/$orderLineItemId",
            method = "PUT",
            body = request
        )
    }

    /*
     * =========================================================
     * INVOICE
     * =========================================================
     */

    /**
     * Change the buyer/customer of an invoice.
     */
    suspend fun changeBuyer(
        orderId: Long,
        buyerId: Long,
        userName: String
    ): Order {

        val encodedUserName =
            userName.encodeURLParameter()

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/invoice/$orderId/buyer" +
                        "?buyerId=$buyerId" +
                        "&userName=$encodedUserName",
            method = "PUT"
        )
    }

    /**
     * Get invoice line items / printable invoice.
     */
    suspend fun getInvoiceLineItems(
        orderId: Long
    ): InvoiceResponse {

        return ApiService.request(
            service = "order",
            endpoint =
                "/inv/print?orderId=$orderId",
            method = "GET"
        )
    }

    /**
     * Create an invoice from a purchase order.
     */
    suspend fun createInvoiceFromPurchaseOrder(
        request: CreateInvoiceRequest
    ): InvoiceResponse {

        return ApiService.request(
            service = "order",
            endpoint = "/inv/order/create",
            method = "POST",
            body = request
        )
    }
    suspend fun applyItemDiscount(
        request: ApplyDiscountRequest
    ): DiscountResult {
        return ApiService.request(
            service = "order",
            endpoint = "/api/discount/item",
            method = "POST",
            body = request
        )
    }

    /**
     * Remove discount from an item
     */
    suspend fun removeItemDiscount(
        orderLineItemId: Long
    ): DiscountResult {
        return ApiService.request(
            service = "order",
            endpoint = "/api/discount/item/$orderLineItemId",
            method = "DELETE"
        )
    }

    /**
     * Apply discount to an order
     */
    suspend fun applyOrderDiscount(
        request: ApplyDiscountRequest
    ): DiscountResult {
        return ApiService.request(
            service = "order",
            endpoint = "/api/discount/order",
            method = "POST",
            body = request
        )
    }

    /**
     * Remove all discounts from an order
     */
    suspend fun removeAllOrderDiscounts(
        orderId: Long
    ): DiscountResult {
        return ApiService.request(
            service = "order",
            endpoint = "/api/discount/order/$orderId",
            method = "DELETE"
        )
    }
}
