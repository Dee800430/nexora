package com.nexora.app.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LookupResponse(
    @SerialName("itemCategory1")
    val itemCategory1: List<Lookup> = emptyList(),

    @SerialName("itemCategory2")
    val itemCategory2: List<Lookup> = emptyList(),

    @SerialName("ItemCategory1")
    val itemCategory1Upper: List<Lookup> = emptyList(),

    @SerialName("ItemCategory2")
    val itemCategory2Upper: List<Lookup> = emptyList()
) {
    val category1: List<Lookup>
        get() = itemCategory1.ifEmpty { itemCategory1Upper }

    val category2: List<Lookup>
        get() = itemCategory2.ifEmpty { itemCategory2Upper }
}
