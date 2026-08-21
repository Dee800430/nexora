package com.nexora.app.data.repository

import com.nexora.app.data.api.OrganizationApi
import com.nexora.app.data.model.orgs.OrgEntity
import com.nexora.app.data.model.orgs.OrgEntityLine

class OrgsRepository {

    /*
     * ---------------------------------------------------------
     * Organization
     * ---------------------------------------------------------
     */

    suspend fun getAllOrgs(): List<OrgEntity> {
        return OrganizationApi.getAllOrgs()
    }

    suspend fun saveOrg(
        data: OrgEntity
    ): OrgEntity {
        return OrganizationApi.saveOrgs(data)
    }

    suspend fun deleteOrg(
        id: Long
    ) {
        OrganizationApi.deleteOrg(id)
    }

    /*
     * ---------------------------------------------------------
     * Organization Category
     * ---------------------------------------------------------
     */

    suspend fun getOrgCategory(): List<OrgEntity> {
        return OrganizationApi.getOrgCategory()
    }

    /*
     * ---------------------------------------------------------
     * Organization Lookups
     * ---------------------------------------------------------
     */

    suspend fun saveOrgLookup(
        data: Any
    ): Any {
        return OrganizationApi.saveOrgLookups(data)
    }

    suspend fun getOrgLookups(): List<Any> {
        return OrganizationApi.getOrgLookups()
    }

    suspend fun deleteOrgLookup(
        id: Long
    ) {
        OrganizationApi.deleteOrgLookup(id)
    }

    suspend fun getOrgLookupById(
        id: Long
    ): Any {
        return OrganizationApi.getOrgLookupById(id)
    }

    suspend fun getAllLookupsWithCategory(): List<Any> {
        return OrganizationApi.getAllLookupsWithCategory()
    }

    /*
     * ---------------------------------------------------------
     * Organizations + Company
     * ---------------------------------------------------------
     */

    suspend fun getAllOrgsWithCompany(): List<OrgEntityLine> {
        return OrganizationApi.getAllOrgsWithCompany()
    }

    suspend fun getOrganizationById(
        id: Long
    ): OrgEntityLine {
        return OrganizationApi.getOrganizationById(id)
    }

    suspend fun getOrganizationCurrentById(): OrgEntityLine {
        return OrganizationApi.getOrganizationCurrentById()
    }

    /*
     * ---------------------------------------------------------
     * Items By Organization
     * ---------------------------------------------------------
     */

    suspend fun getItemByOrganization(
        orgEntityId: Long,
        itemCategory1ID: Long? = null,
        itemCategory2ID: Long? = null
    ): Any {
        return OrganizationApi.getItemByOrganization(
            orgEntityId = orgEntityId,
            itemCategory1ID = itemCategory1ID,
            itemCategory2ID = itemCategory2ID
        )
    }
}