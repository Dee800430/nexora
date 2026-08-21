package com.nexora.app.data.model.item

import kotlinx.serialization.Serializable

@Serializable
data class ItemLine(
    val itemLineId: Long? = null,
    val itemID: Long? = null,
    val saleRate: Double? = null,
    val itemQty: Double? = null,
    val purchaseRate: Double? = null,
    val saleMaxQty: Double? = null,
    val basicRate: Double? = null,
    val uomID: Long? = null,
    val UOM: String? = null
)