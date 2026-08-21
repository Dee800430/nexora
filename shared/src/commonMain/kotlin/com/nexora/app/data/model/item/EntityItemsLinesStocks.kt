package com.nexora.app.data.model.item

import kotlinx.serialization.Serializable

@Serializable
data class EntityItemsLinesStocks(
    val entityItemLineStockId: Long? = null,
    val pkId: String? = null,
    val entityItemLineId: Long? = null,
    val orgEntityStockLocId: Long? = null,
    val entityItemStockBatchId: Long? = null,
    val orgSerialNumber: String? = null,
    val mftrSerialNumber: String? = null,
    val purchasedQty: Double? = null,
    val soldQty: Double? = null,
    val balanceQty: Double? = null,
    val basicRate: Double? = null,
    val purchaseRate: Double? = null,
    val saleRate: Double? = null,
    val promoRate: Double? = null,
    val promoStartDate: String? = null,
    val promoEndDate: String? = null,
    val batchNumber: String? = null,
    val expiryDate: String? = null,
    val inventoryTypeId: Long? = null,
    val entityItemLineStockParentId: Long? = null,
    val workflowId: Long? = null,
    val orderId: Long? = null,
    val orderLineItemId: Long? = null,
    val createdDate: String? = null,
    val modifiedDate: String? = null,
    val modifiedBy: Long? = null,
    val siteId: Long? = null
)