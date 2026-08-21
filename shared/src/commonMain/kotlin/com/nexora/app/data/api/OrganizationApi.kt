package com.nexora.app.data.api

import com.nexora.app.core.network.ApiService
import com.nexora.app.data.model.orgs.OrgEntity
import com.nexora.app.data.model.orgs.OrgEntityLine

object OrganizationApi {


    /*
     * ---------------------------------------------------------
     * Organization
     * ---------------------------------------------------------
     */

    suspend fun getAllOrgs(): List<OrgEntity> {
        return ApiService.request(
            service = "organization",
            endpoint = "/org",
            method = "GET"
        )
    }

    suspend fun saveOrgs(
        data: OrgEntity
    ): OrgEntity {
        return ApiService.request(
            service = "organization",
            endpoint = "/org/save",
            method = "POST",
            body = data
        )
    }

    suspend fun deleteOrg(
        id: Long
    ) {
        ApiService.request<Unit>(
            service = "organization",
            endpoint = "/org/delete/$id",
            method = "DELETE"
        )
    }

    /*
     * ---------------------------------------------------------
     * Organization Category
     * ---------------------------------------------------------
     */

    suspend fun getOrgCategory(): List<OrgEntity> {
        return ApiService.request(
            service = "organization",
            endpoint = "/org/category",
            method = "GET"
        )
    }

    /*
     * ---------------------------------------------------------
     * Organization Lookups
     * ---------------------------------------------------------
     */

    suspend fun saveOrgLookups(
        data: Any
    ): Any {
        return ApiService.request(
            service = "organization",
            endpoint = "/org/save-lookup",
            method = "POST",
            body = data
        )
    }

    suspend fun getOrgLookups(): List<Any> {
        return ApiService.request(
            service = "organization",
            endpoint = "/org/getlookups",
            method = "GET"
        )
    }

    suspend fun deleteOrgLookup(
        id: Long
    ) {
        ApiService.request<Unit>(
            service = "organization",
            endpoint = "/org/delete-lookup/$id",
            method = "DELETE"
        )
    }

    suspend fun getOrgLookupById(
        id: Long
    ): Any {
        return ApiService.request(
            service = "organization",
            endpoint = "/org/$id",
            method = "GET"
        )
    }

    suspend fun getAllLookupsWithCategory(): List<Any> {
        return ApiService.request(
            service = "organization",
            endpoint = "/org/lookup-category",
            method = "GET"
        )
    }

    /*
     * ---------------------------------------------------------
     * Organizations + Company
     * ---------------------------------------------------------
     */

    suspend fun getAllOrgsWithCompany(): List<OrgEntityLine> {
        return ApiService.request(
            service = "organization",
            endpoint = "/org/all/orgs",
            method = "GET"
        )
    }

    suspend fun getOrganizationById(
        id: Long
    ): OrgEntityLine {
        return ApiService.request(
            service = "organization",
            endpoint = "/org/inv/$id",
            method = "GET"
        )
    }

    suspend fun getOrganizationCurrentById(): OrgEntityLine {
        return ApiService.request(
            service = "organization",
            endpoint = "/org/purchase",
            method = "GET"
        )
    }

    /*
     * ---------------------------------------------------------
     * Items By Organization
     *
     * IMPORTANT:
     * This endpoint belongs to ITEM SERVICE,
     * not ORGANIZATION SERVICE.
     * ---------------------------------------------------------
     */

    suspend fun getItemByOrganization(
        orgEntityId: Long,
        itemCategory1ID: Long? = null,
        itemCategory2ID: Long? = null
    ): Any {

        val endpoint = buildString {

            append(
                "/item/getItem-by-org?orgEntityId=$orgEntityId"
            )

            if (itemCategory1ID != null) {
                append(
                    "&itemCategory1ID=$itemCategory1ID"
                )
            }

            if (itemCategory2ID != null) {
                append(
                    "&itemCategory2ID=$itemCategory2ID"
                )
            }
        }

        return ApiService.request(
            service = "item",
            endpoint = endpoint,
            method = "GET"
        )
    }
}