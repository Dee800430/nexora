package com.nexora.app.data.api

import com.nexora.app.core.network.ApiService
import com.nexora.app.data.model.item.EntityItemsLinesStocks
import com.nexora.app.data.model.item.Item
import com.nexora.app.data.model.item.ItemCategory
import com.nexora.app.data.model.item.ItemDto
import com.nexora.app.data.model.item.ItemLine
import com.nexora.app.data.model.item.ItemLineDocument
import com.nexora.app.data.model.item.ItemLookup
import com.nexora.app.data.model.item.LookupResponse
import com.nexora.app.data.model.item.StockRequest

/**
 * API layer for Item Service.
 *
 * This class contains ONLY network/API operations.
 *
 * Business logic should stay inside repositories/ViewModels.
 */
object ItemApiService {

    /*
     * =========================================================
     * ITEMS
     * =========================================================
     */

    suspend fun getItems(): List<Item> {
        return ApiService.request(
            service = "item",
            endpoint = "/item/getAllItem",
            method = "GET"
        )
    }

    suspend fun getItemById(
        itemId: Long
    ): Item {
        return ApiService.request(
            service = "item",
            endpoint = "/item/getItem/$itemId",
            method = "GET"
        )
    }

    suspend fun saveItem(
        item: Item
    ): Item {
        return ApiService.request(
            service = "item",
            endpoint = "/item/saveItem",
            method = "POST",
            body = item
        )
    }

    suspend fun deleteItem(
        itemId: Long
    ) {
        ApiService.request<Unit>(
            service = "item",
            endpoint = "/item/deleteItem/$itemId",
            method = "DELETE"
        )
    }

    /*
     * =========================================================
     * ITEM LINES
     * =========================================================
     */

    suspend fun saveLine(
        itemLine: ItemLine
    ): ItemLine {
        return ApiService.request(
            service = "item",
            endpoint = "/item/saveItemLine",
            method = "POST",
            body = itemLine
        )
    }

    suspend fun getLinesByItem(
        itemId: Long
    ): List<ItemLine> {
        return ApiService.request(
            service = "item",
            endpoint = "/item/getItemLine/$itemId",
            method = "GET"
        )
    }

    suspend fun updateLine(
        itemLineId: Long,
        itemLine: ItemLine
    ): ItemLine {
        return ApiService.request(
            service = "item",
            endpoint = "/item/save/$itemLineId",
            method = "PUT",
            body = itemLine
        )
    }

    suspend fun deleteLine(
        itemLineId: Long
    ) {
        ApiService.request<Unit>(
            service = "item",
            endpoint = "/item/deleteItemLine/$itemLineId",
            method = "DELETE"
        )
    }

    /*
     * =========================================================
     * ITEM CATEGORIES
     * =========================================================
     */

    suspend fun getCategories(): List<ItemCategory> {
        return ApiService.request(
            service = "item",
            endpoint = "/item-look/all-cats",
            method = "GET"
        )
    }

    suspend fun saveCategory(
        category: ItemCategory
    ): ItemCategory {
        return ApiService.request(
            service = "item",
            endpoint = "/item-look/save-category",
            method = "POST",
            body = category
        )
    }

    /*
     * =========================================================
     * LOOKUPS
     * =========================================================
     */

    suspend fun getAllLookups(): List<ItemLookup> {
        return ApiService.request(
            service = "item",
            endpoint = "/item-look/get-all-lookups",
            method = "GET"
        )
    }

    suspend fun getLookupById(
        id: Long
    ): ItemLookup {
        return ApiService.request(
            service = "item",
            endpoint = "/item-look/get-lookup/$id",
            method = "GET"
        )
    }

    suspend fun saveLookup(
        lookup: ItemLookup
    ): ItemLookup {
        return ApiService.request(
            service = "item",
            endpoint = "/item-look/save-lookup",
            method = "POST",
            body = lookup
        )
    }

    suspend fun deleteItemLookup(
        id: Long
    ) {
        ApiService.request<Unit>(
            service = "item",
            endpoint = "/item-look/delete-lookup/$id",
            method = "DELETE"
        )
    }

    suspend fun getLookups(
        lookupType: String
    ): LookupResponse {
        return ApiService.request(
            service = "item",
            endpoint =
                "/item-look/getGroupByLookup?lookupType=$lookupType",
            method = "GET"
        )
    }

    suspend fun getUomList(): List<ItemLookup> {
        return ApiService.request(
            service = "item",
            endpoint = "/item-look/getItemUOM",
            method = "GET"
        )
    }

    suspend fun getAllTaxes(): List<ItemLookup> {
        return ApiService.request(
            service = "item",
            endpoint = "/item/item-look/get-all-taxes",
            method = "GET"
        )
    }

    /*
     * =========================================================
     * ORGANIZATION ITEM ASSIGNMENT
     * =========================================================
     */

    suspend fun getAssignableItems(
        orgEntityId: Long
    ): List<Item> {
        return ApiService.request(
            service = "item",
            endpoint =
                "/item/entity/assign-item/$orgEntityId",
            method = "GET"
        )
    }

    suspend fun getNonAssignedItems(
        orgEntityId: Long
    ): List<Item> {
        return ApiService.request(
            service = "item",
            endpoint =
                "/item/entity/non-assign/$orgEntityId",
            method = "GET"
        )
    }

    suspend fun assignItemToOrg(
        orgEntityID: Long,
        itemID: Long,
        userID: Long? = null
    ): Any? {

        val endpoint = buildString {

            append(
                "/item/entity/assign" +
                        "?orgEntityId=$orgEntityID" +
                        "&itemId=$itemID"
            )

            if (userID != null) {
                append("&userId=$userID")
            }
        }

        return ApiService.request(
            service = "item",
            endpoint = endpoint,
            method = "POST"
        )
    }

    suspend fun removeItemFromOrg(
        itemID: Long,
        orgEntityID: Long
    ) {
        ApiService.request<Unit>(
            service = "item",
            endpoint =
                "/item/entity/delete" +
                        "?itemID=$itemID" +
                        "&orgEntityID=$orgEntityID",
            method = "DELETE"
        )
    }

    /*
     * =========================================================
     * HOME / PRODUCT LIST
     * =========================================================
     *
     * This is the API that your Home screen will eventually
     * consume through ItemRepository.
     */

    suspend fun getAllItemLines(
        itemCategory1ID: Long? = null,
        itemCategory2ID: Long? = null,

    ): List<ItemDto> {

        val endpoint = buildString {

            append("/item/getItem-org")

            val params = mutableListOf<String>()

            itemCategory1ID?.let {
                params.add(
                    "itemCategory1ID=$it"
                )
            }

            itemCategory2ID?.let {
                params.add(
                    "itemCategory2ID=$it"
                )
            }

            if (params.isNotEmpty()) {
                append("?")
                append(
                    params.joinToString("&")
                )
            }
        }

        return ApiService.request(
            service = "item",
            endpoint = endpoint,
            method = "GET"
        )
    }

    /*
     * =========================================================
     * STOCK
     * =========================================================
     */

    suspend fun saveStock(
        stock: StockRequest
    ): EntityItemsLinesStocks {

        return ApiService.request(
            service = "item",
            endpoint = "/item/entity/add-stock",
            method = "POST",
            body = stock
        )
    }

    suspend fun getAllStocks(
        entityItemLineId: Long
    ): List<EntityItemsLinesStocks> {

        return ApiService.request(
            service = "item",
            endpoint =
                "/item/entity/stocks/$entityItemLineId",
            method = "GET"
        )
    }

    /*
     * =========================================================
     * ITEM DOCUMENTS
     * =========================================================
     */



    suspend fun getDocumentsByItemLine(
        entityItemLineId: Long
    ): List<ItemLineDocument> {
        return try {
            // Call your API service
            val result = ApiService.request<List<ItemLineDocument>>(
                service = "item",
                endpoint = "/api/documents/item-line/$entityItemLineId",
                method = "GET"
            )

            // Return the result (already typed as List<ItemLineDocument>)
            result ?: emptyList()

        } catch (e: Exception) {
            println("❌ API Error: ${e.message}")
            e.printStackTrace()
            emptyList()
        }
    }

}
