package com.nexora.app.data.model.item

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
// com.nexora.app.data.model.item.ItemLineDocument.kt


@Serializable
data class ItemLineDocument(
    @SerialName("docId")
    val docId: Int? = null,

    @SerialName("docName")
    val docName: String? = null,

    @SerialName("docDesc")
    val docDesc: String? = null,

    @SerialName("fileNameOriginal")
    val fileNameOriginal: String? = null,

    @SerialName("fileNameNew")
    val fileNameNew: String? = null,

    @SerialName("itemLineId")
    val itemLineId: Int? = null,

    @SerialName("entityItemLineId")
    val entityItemLineId: Int? = null,

    @SerialName("documentUrl")
    val documentUrl: String? = null,  // 👈 This is what we need

    @SerialName("docFileExt")
    val docFileExt: String? = null,   // 👈 This tells us it's an image

    @SerialName("isActive")
    val isActive: Boolean? = null,

    @SerialName("isDelete")
    val isDelete: Boolean? = null,

    @SerialName("createdDate")
    val createdDate: String? = null,

    @SerialName("modifiedDate")
    val modifiedDate: String? = null
)