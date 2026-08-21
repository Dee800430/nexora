package com.nexora.app.data.model.item

import kotlinx.serialization.Serializable

@Serializable
data class Item(
    val itemID: Long,
    val orgEntityId: Long,
    val itemName: String,
    val itemTypeID: String? = null,
    val itemCategory1ID: String? = null,
    val itemDescription: String? = null,
    val itemBrandID: String? = null,
    val isActive: Boolean
)