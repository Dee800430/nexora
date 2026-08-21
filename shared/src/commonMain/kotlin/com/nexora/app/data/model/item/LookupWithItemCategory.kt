package com.nexora.app.data.model.item

import kotlinx.serialization.Serializable

@Serializable
data class LookupWithItemCategory(
    val lookupID: Long? = null,
    val pkID: String? = null,
    val categoryID: Long? = null,
    val category: String? = null,
    val lookupParentID: Long? = null,
    val orgEntityID: Long? = null,
    val lookupText: String? = null,
    val lookupValue: String? = null,
    val codePrefix: String? = null,
    val lookupGroup1Value: String? = null,
    val lookupGroup2Value: String? = null,
    val createdDate: String,
    val modifiedDate: String,
    val modifiedBy: Long,
    val isActive: Boolean,
    val isDelete: Boolean,
    val isAddStockAllow: Boolean,
    val siteID: Long
)