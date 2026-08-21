package com.nexora.app.data.repository

import com.nexora.app.data.api.OrderApi
import com.nexora.app.data.model.order.CreateInvoiceRequest
import com.nexora.app.data.model.order.InvoiceResponse
import com.nexora.app.data.model.order.Order
import com.nexora.app.data.model.order.OrderRequest
import com.nexora.app.data.model.order.PlaceOrderRequest
import com.nexora.app.data.model.order.PurchaseOrderItem
import com.nexora.app.data.model.order.UpdatePurchaseItemRequest

class OrderRepository {

    /*
     * =========================================================
     * INVOICE / SALES ORDER
     * =========================================================
     */

    suspend fun allocateStock(
        order: OrderRequest
    ): Order {
        return OrderApi.allocateStock(order)
    }

    suspend fun viewCart(
        buyerId: Long
    ): Order {
        return OrderApi.viewCart(buyerId)
    }

    suspend fun removeFromCart(
        orderRequest: OrderRequest
    ): Order {
        return OrderApi.removeFromCart(orderRequest)
    }

    suspend fun placeInvoiceOrder(
        place: PlaceOrderRequest
    ): Order {
        return OrderApi.placeInvoiceOrder(place)
    }

    suspend fun updateQuantity(
        orderLineItemId: Long,
        quantity: Double
    ): Order {
        return OrderApi.updateQuantity(
            orderLineItemId = orderLineItemId,
            quantity = quantity
        )
    }

    suspend fun getInvoicesByStatus(
        status: String
    ): List<Order> {
        return OrderApi.getInvoicesByStatus(status)
    }

    /*
     * =========================================================
     * PURCHASE ORDER
     * =========================================================
     */

    suspend fun addToPurchaseOrder(
        data: OrderRequest
    ): Order {
        return OrderApi.addToPurchaseOrder(data)
    }

    suspend fun getPurchaseCart(
        sellerId: Long
    ): Order {
        return OrderApi.getPurchaseCart(sellerId)
    }

    suspend fun getFromInvoicesToCart(
        orderId: Long
    ): Order {
        return OrderApi.getFromInvoicesToCart(orderId)
    }

    suspend fun getFromInvoiceToUpdate(
        orderId: Long
    ): Order {
        return OrderApi.getFromInvoiceToUpdate(orderId)
    }

    suspend fun updatePurchaseQty(
        orderLineItemId: Long,
        quantity: Double
    ): Order {
        return OrderApi.updatePurchaseQty(
            orderLineItemId = orderLineItemId,
            quantity = quantity
        )
    }

    suspend fun removePurchaseItem(
        id: Long
    ): Order {
        return OrderApi.removePurchaseItem(id)
    }

    suspend fun removePurchaseFromCart(
        orderRequest: OrderRequest
    ): Order {
        return OrderApi.removePurchaseFromCart(orderRequest)
    }

    suspend fun submitPurchaseOrder(
        orderId: Long,
        sellerId: Long
    ): Order {
        return OrderApi.submitPurchaseOrder(
            orderId = orderId,
            sellerId = sellerId
        )
    }

    suspend fun getPurchaseInvoicesByStatus(
        buyerId: Long,
        status: String
    ): List<Order> {
        return OrderApi.getPurchaseInvoicesByStatus(
            buyerId = buyerId,
            status = status
        )
    }

    suspend fun getPurchaseInvoicesByStatusForSeller(
        status: String
    ): List<Order> {
        return OrderApi.getPurchaseInvoicesByStatusForSeller(status)
    }

    suspend fun getPurchaseHistory(
        buyerId: Long
    ): List<Order> {
        return OrderApi.getPurchaseHistory(buyerId)
    }

    suspend fun getPurchaseOrderItems(
        orderId: Long
    ): List<PurchaseOrderItem> {
        return OrderApi.getPurchaseOrderItems(orderId)
    }

    suspend fun updatePurchaseItem(
        orderLineItemId: Long,
        data: UpdatePurchaseItemRequest
    ): PurchaseOrderItem {
        return OrderApi.updatePurchaseItem(
            orderLineItemId = orderLineItemId,
            data
        )
    }

    /*
     * =========================================================
     * INVOICE
     * =========================================================
     */

    suspend fun changeBuyer(
        orderId: Long,
        buyerId: Long,
        userName: String
    ): Order {
        return OrderApi.changeBuyer(
            orderId = orderId,
            buyerId = buyerId,
            userName = userName
        )
    }

    suspend fun getInvoiceLineItems(
        orderId: Long
    ): InvoiceResponse {
        return OrderApi.getInvoiceLineItems(orderId)
    }

    suspend fun createInvoiceFromPurchaseOrder(
        request: CreateInvoiceRequest
    ): InvoiceResponse {
        return OrderApi.createInvoiceFromPurchaseOrder(request)
    }
}