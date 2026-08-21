package com.nexora.app.data.model.item

import kotlinx.serialization.Serializable

@Serializable
data class StockRequest(
    val entityItemLineStockReqID: Long,
    val entityItemLineStockParentID: Long,
    val workFlowID: Long,
    val orderID: Long,
    val orderLineItemID: Long,
    val entityItemLineID: Long,
    val inventoryTypeID: Long,
    val orgEntityStockLocID: Long,
    val entityItemStockBatchID: Long,
    val itemBatchNumber: String,
    val orgSerialNumber: String,
    val mftrSerialNumber: String,
    val purchaseQty: Double,
    val expiryDate: String,
    val stockAction: String,
    val basicRate: Double,
    val purchaseRate: Double,
    val saleRate: Double,
    val modifiedBy: Long
)