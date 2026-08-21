package com.nexora.app.data.model.orgs;

import kotlinx.serialization.Serializable

@Serializable
data class OrgEntity(
    val orgEntityId: Long,
    val userId: Long,
    val pkId: String,
    val orgEntityName: String,
    val emailID: String,
    val mobileNumber: String,
    val displayName: String,
    val orgEntityType1Id: String? = null,
    val businessTypeId: String? = null,
    val industryTypeId: String? = null,
    val businessModelId: String? = null,
    val isActive: Boolean
)

@Serializable
data class OrgEntityLine(
    val orgEntityId: Long,
    val pkId: String? = null,
    val orgEntityIdParent: Long? = null,
    val siteId: Long? = null,
    val sourceSystem: Long? = null,
    val companyId: Long? = null,
    val emailID: String? = null,
    val mobileNumber: String? = null,
    val orgEntityType1Id: String? = null,
    val isExternal: Boolean? = null,
    val businessTypeId: String? = null,
    val industryTypeId: String? = null,
    val orgEntityName: String? = null,
    val orgEntityCode: String? = null,
    val displayName: String? = null,
    val countryId: Long? = null,
    val taxNo: String? = null,
    val userId: Long? = null,
    val isWebRegistration: Boolean? = null,
    val orgEntityIdRef: Long? = null,
    val tallyCompanyName: String? = null,
    val tallyLedgerName: String? = null,
    val createdDate: String? = null,
    val modifiedDate: String? = null,
    val orgEntityLogo: String? = null,
    val modifiedBy: Long? = null,
    val isActive: Boolean? = null,
    val isDelete: Boolean? = null
)