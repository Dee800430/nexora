package com.nexora.app.data.repository

import com.nexora.app.data.api.ItemApiService
import com.nexora.app.data.model.item.EntityItemsLinesStocks
import com.nexora.app.data.model.item.Item
import com.nexora.app.data.model.item.ItemCategory
import com.nexora.app.data.model.item.ItemDto
import com.nexora.app.data.model.item.ItemLine
import com.nexora.app.data.model.item.ItemLineDocument
import com.nexora.app.data.model.item.ItemLookup
import com.nexora.app.data.model.item.LookupResponse
import com.nexora.app.data.model.item.StockRequest

class ItemRepository {

    /*
     * =========================================================
     * ITEMS
     * =========================================================
     */

    suspend fun getItems(): List<Item> {
        return ItemApiService.getItems()
    }

    suspend fun getItemById(
        itemId: Long
    ): Item {
        return ItemApiService.getItemById(itemId)
    }

    suspend fun saveItem(
        item: Item
    ): Item {
        return ItemApiService.saveItem(item)
    }

    suspend fun deleteItem(
        itemId: Long
    ) {
        ItemApiService.deleteItem(itemId)
    }

    /*
     * =========================================================
     * ITEM LINES
     * =========================================================
     */

    suspend fun saveLine(
        itemLine: ItemLine
    ): ItemLine {
        return ItemApiService.saveLine(itemLine)
    }

    suspend fun getLinesByItem(
        itemId: Long
    ): List<ItemLine> {
        return ItemApiService.getLinesByItem(itemId)
    }

    suspend fun updateLine(
        itemLineId: Long,
        itemLine: ItemLine
    ): ItemLine {
        return ItemApiService.updateLine(
            itemLineId = itemLineId,
            itemLine = itemLine
        )
    }

    suspend fun deleteLine(
        itemLineId: Long
    ) {
        ItemApiService.deleteLine(itemLineId)
    }

    suspend fun getAllItemLines(
        itemCategory1Id: Long? = null,
        itemCategory2Id: Long? = null
    ): List<ItemDto> {
        return ItemApiService.getAllItemLines(
            itemCategory1ID = itemCategory1Id,
            itemCategory2ID = itemCategory2Id
        )
    }

    suspend fun getDocumentsByItemLine(
        entityItemLineId: Long
    ): List<ItemLineDocument> {
        return ItemApiService.getDocumentsByItemLine(
            entityItemLineId
        )
    }

    /*
     * =========================================================
     * CATEGORIES
     * =========================================================
     */

    suspend fun getCategories(): List<ItemCategory> {
        return ItemApiService.getCategories()
    }

    suspend fun saveCategory(
        category: ItemCategory
    ): ItemCategory {
        return ItemApiService.saveCategory(category)
    }

    /*
     * =========================================================
     * LOOKUPS
     * =========================================================
     */

    suspend fun getAllLookups(): List<ItemLookup> {
        return ItemApiService.getAllLookups()
    }

    suspend fun getLookupById(
        id: Long
    ): ItemLookup {
        return ItemApiService.getLookupById(id)
    }

    suspend fun saveLookup(
        lookup: ItemLookup
    ): ItemLookup {
        return ItemApiService.saveLookup(lookup)
    }

    suspend fun deleteItemLookup(
        id: Long
    ) {
        ItemApiService.deleteItemLookup(id)
    }

    suspend fun getLookups(
        lookupType: String
    ): LookupResponse {
        return ItemApiService.getLookups(lookupType)
    }

    suspend fun getUomList(): List<ItemLookup> {
        return ItemApiService.getUomList()
    }

    suspend fun getAllTaxes(): List<ItemLookup> {
        return ItemApiService.getAllTaxes()
    }

    /*
     * =========================================================
     * ORGANIZATION ITEMS
     * =========================================================
     */

    suspend fun getAssignableItems(
        orgEntityId: Long
    ): List<Item> {
        return ItemApiService.getAssignableItems(
            orgEntityId
        )
    }

    suspend fun getNonAssignedItems(
        orgEntityId: Long
    ): List<Item> {
        return ItemApiService.getNonAssignedItems(
            orgEntityId
        )
    }

    suspend fun assignItemToOrg(
        orgEntityId: Long,
        itemId: Long,
        userId: Long? = null
    ): Any? {
        return ItemApiService.assignItemToOrg(
            orgEntityID = orgEntityId,
            itemID = itemId,
            userID = userId
        )
    }

    suspend fun removeItemFromOrg(
        itemId: Long,
        orgEntityId: Long
    ) {
        ItemApiService.removeItemFromOrg(
            itemID = itemId,
            orgEntityID = orgEntityId
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
        return ItemApiService.saveStock(stock)
    }

    suspend fun getAllStocks(
        entityItemLineId: Long
    ): List<EntityItemsLinesStocks> {
        return ItemApiService.getAllStocks(
            entityItemLineId
        )
    }
}