package com.nexora.app.data.model.item

data class UploadFile(
    val fileName: String,
    val bytes: ByteArray,
    val contentType: String = "application/octet-stream"
)