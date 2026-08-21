package com.nexora.app.data.model.item
import kotlinx.serialization.Serializable

@Serializable
data class Lookup(
    val lookupID: Long,
    val lookupText: String,
    val lookupValue: String? = null
)
