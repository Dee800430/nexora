package com.nexora.app.util

import com.nexora.app.core.network.ApiConfig
import com.nexora.app.data.model.item.ItemLineDocument
// com.nexora.app.util.ImageUtils.kt

fun imageUrlsFromDocuments(
    documents: List<ItemLineDocument>
): List<String> {
    return documents
        .filter { doc ->
            // Only include documents that are images
            doc.docFileExt?.startsWith("image/") == true
        }
        .mapNotNull { doc ->
            // Return the full URL directly from API
            doc.documentUrl
        }
        .distinct()
}

// com.nexora.app.util.ImageUtils.kt

fun normalizeImageUrl(
    rawUrl: String?
): String {
    val url = rawUrl?.trim() ?: return ""
    if (url.isBlank()) return ""

    // If it's already a full URL with https://, return it as-is
    if (url.startsWith("https://", ignoreCase = true) ||
        url.startsWith("http://", ignoreCase = true)) {
        return url
    }

    // Handle Cloudinary URLs without protocol
    if (url.contains("res.cloudinary.com", ignoreCase = true)) {
        return if (url.startsWith("https://")) url else "https://$url"
    }

    // Handle fileName from API - remove duplicate item-lines/
    var cleanedUrl = url
    // If there's a double item-lines, fix it
    while (cleanedUrl.contains("item-lines/item-lines/")) {
        cleanedUrl = cleanedUrl.replace("item-lines/item-lines/", "item-lines/")
    }

    // If it starts with item-lines/, construct the full Cloudinary URL
    if (cleanedUrl.startsWith("item-lines/")) {
        // Remove leading slash if present
        val path = cleanedUrl.trimStart('/')
        return "https://res.cloudinary.com/dic3vj6tg/image/upload/v1786057593/$path"
    }

    // Starts with slash - add base URL
    if (cleanedUrl.startsWith("/")) {
        return "${ApiConfig.ITEM_SERVICE_URL}$cleanedUrl"
    }

    // Contains path but not item-lines
    if (cleanedUrl.contains("/")) {
        return cleanedUrl
    }

    // Just a filename - fallback
    return "${ApiConfig.ITEM_SERVICE_URL}/api/docs/image-by-item/$cleanedUrl"
}

private fun String.looksLikeImageUrl(): Boolean {
    val lower = lowercase()
    return lower.endsWith(".jpg") ||
        lower.endsWith(".jpeg") ||
        lower.endsWith(".png") ||
        lower.endsWith(".webp") ||
        lower.endsWith(".gif") ||
        lower.endsWith(".bmp")
}
