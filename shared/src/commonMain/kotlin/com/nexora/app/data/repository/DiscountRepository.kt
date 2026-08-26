package com.nexora.app.data.repository

import com.nexora.app.data.api.OrderApi
import com.nexora.app.data.model.order.ApplyDiscountRequest
import com.nexora.app.data.model.order.DiscountResult

class DiscountRepository {

    suspend fun applyItemDiscount(
        orderLineItemId: Long,
        discountType: String,
        discountValue: Double
    ): DiscountResult {
        val request = ApplyDiscountRequest(
            orderLineItemId = orderLineItemId,
            discountType = discountType,
            discountValue = discountValue
        )
        return OrderApi.applyItemDiscount(request)
    }

    suspend fun removeItemDiscount(
        orderLineItemId: Long
    ): DiscountResult {
        return OrderApi.removeItemDiscount(orderLineItemId)
    }

    suspend fun applyOrderDiscount(
        orderId: Long,
        discountType: String,
        discountValue: Double,
        applyToAllItems: Boolean = false
    ): DiscountResult {
        val request = ApplyDiscountRequest(
            orderId = orderId,
            discountType = discountType,
            discountValue = discountValue,
            applyToAllItems = applyToAllItems
        )
        return OrderApi.applyOrderDiscount(request)
    }

    suspend fun removeAllOrderDiscounts(
        orderId: Long
    ): DiscountResult {
        return OrderApi.removeAllOrderDiscounts(orderId)
    }
}