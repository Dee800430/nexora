package com.nexora.app.data.model.item

import kotlinx.serialization.Serializable
@Serializable
data class ItemDto(
    val fileName: String? = null,
    val entityItemLineStockId: Long? = null,
    val entityItemLineId: Long? = null,

    val balanceQty: Double = 0.0,
    val saleRate: Double = 0.0,
    val itemQty: Double = 0.0,
    val basicRate: Double = 0.0,
    val saleMaxQty: Double = 0.0,

    val itemLineId: Long? = null,
    val purchaseRate: Double = 0.0,

    val lookupText: String? = null,

    val itemCategory1ID: Long? = null,
    val itemCategory2ID: Long? = null,

    val orgEntityId: Long? = null,
    val userId: Long? = null,

    val itemName: String = "",
    val itemId: Long? = null,
    val uom: String? = null,

    val itemCode: String? = null,
    val itemNumber: String? = null
)