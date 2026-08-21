package com.nexora.app.data.model.item

import kotlinx.serialization.Serializable

@Serializable
data class ItemCategory(
    val categoryId: Long,
    val pkId: Long? = null,
    val siteId: Long,
    val category: String,
    val categoryDesc: String? = null,
    val codePrefix: String? = null,
    val createdDate: String,
    val modifiedDate: String,
    val modifiedBy: Long,
    val isActive: Boolean,
    val isDelete: Boolean
)