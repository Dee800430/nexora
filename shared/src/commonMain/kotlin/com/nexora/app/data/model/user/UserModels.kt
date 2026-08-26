package com.nexora.app.data.model.user

import kotlinx.serialization.Serializable

@Serializable
data class Roles(
    val roleId: Long,
    val roleName: String
)
@Serializable
data class SendOtpRequest(
    val mobile: String
)

@Serializable
data class MobileLoginRequest(
    val mobile: String,
    val otp: String
)

@Serializable
data class UserLines(
    val userId: Long,
    val userName: String,
    val userIdentityType: String? = null,
    val userType: String? = null,
    val isActive: Boolean? = null,
    val email: String? = null,
    val createdDate: String? = null,
    val userAddrID: Long
)

@Serializable
data class UserAddress(
    val userAddrID: Long? = null,
    val addressTypeId: Long? = null,
    val postalCode: String? = null,
    val addressType: String? = null,
    val subAddressTypeId: Long? = null,
    val subAddressType: String? = null,
    val addressName: String,
    val address1: String? = null,
    val address2: String? = null,
    val address3: String? = null,
    val mobileNo: String? = null,
    val email: String? = null,
    val city: String,
    val cityId: Long? = null,
    val stateId: Long? = null,
    val state: String,
    val countryId: Long? = null,
    val zipCode: String? = null,
    val createdDate: String,
    val modifiedDate: String,
    val isActive: Boolean,
    val isDelete: Boolean,
    val userId: Long? = null,
    val userAddressesEmails: UserAddressesEmails? = null,
    val userAddressesMobile: UserAddressesMobile? = null
)

@Serializable
data class UserProfile(
    val firstName: String,
    val secondName: String,
    val age: String,
    val disease: String,
    val gender: String,
    val profileUpdated: Boolean,
    val isActive: Boolean,
    val isDelete: Boolean,
    val userId: Long
)

@Serializable
data class User(
    val userId: Long? = null,
    val userName: String? = null,
    val email: String? = null,
    val mobile: String? = null,
    val userType: String? = null,
    val userTypeID: Long? = null,
    val userIdentityID: Long? = null,
    val userIdentityType: String? = null,
    val userBusinessTypeID: Long? = null,
    val businessType: String? = null,
    val roles: List<Roles>? = null,
    val createdDate: String? = null,
    val isActive: Boolean? = null,
    val addressTypeID: Long? = null
)

@Serializable
data class CustomerRecord(
    val userId: Long? = null,
    val userName: String? = null,
    val mobile: String? = null,
    val mobileNo: String? = null,
    val email: String? = null,
    val address: String? = null,
    val addressName: String? = null,
    val address1: String? = null,
    val address2: String? = null,
    val city: String? = null,
    val state: String? = null
) {
    val displayMobile: String
        get() = mobile ?: mobileNo ?: ""

    val displayAddress: String
        get() = listOfNotNull(
            address,
            addressName,
            address1,
            address2,
            city,
            state
        )
            .filter { it.isNotBlank() }
            .distinct()
            .joinToString(", ")
}

@Serializable
data class Lookup(
    val lookupID: Long,
    val pkID: String,
    val categoryID: Long,
    val category: String? = null,
    val lookupParentID: Long,
    val orgEntityID: Long,
    val lookupText: String,
    val lookupValue: String,
    val codePrefix: String,
    val createdDate: String,
    val modifiedDate: String? = null,
    val modifiedBy: Long? = null,
    val isActive: Boolean,
    val isDelete: Boolean? = null,
    val siteID: Long
)

@Serializable
data class UserAddressesMobile(
    val userAddressMobileId: Long? = null,
    val pkID: String? = null,
    val userID: Long? = null,
    val addressMobileTypeID: Long? = null,
    val mobileNo: String,
    val countryId: Long? = null,
    val isPrimary: Boolean? = null,
    val isVerified: Boolean? = null,
    val verifyDateTime: String? = null,
    val addressTelTypeID: Long? = null,
    val isActive: Boolean? = null,
    val isDelete: Boolean? = null,
    val createdDate: String? = null,
    val modifiedDate: String? = null,
    val modifiedBy: Long? = null,
    val siteID: Long? = null,
    val userAddrID: Long? = null
)

@Serializable
data class UserAddressesEmails(
    val userAddrEmailID: Long? = null,
    val pkID: String? = null,
    val addressEmailTypeID: Long? = null,
    val emailId: String? = null,
    val email: String,
    val userID: Long? = null,
    val isPrimary: Boolean? = null,
    val isVerified: Boolean? = null,
    val verifyDateTime: String? = null,
    val isActive: Boolean? = null,
    val isDelete: Boolean? = null,
    val createdDate: String? = null,
    val modifiedDate: String? = null,
    val modifiedBy: Long? = null,
    val userAddrID: Long? = null
)

@Serializable
data class MobileItem(
    val id: Long? = null,
    val mobileNo: String,
    val active: Boolean? = null,
    val primary: Boolean? = null,
    val delete: Boolean? = null
)

@Serializable
data class EmailItem(
    val id: Long? = null,
    val email: String,
    val isPrimary: Boolean? = null,
    val isActive: Boolean? = null,
    val isDelete: Boolean? = null
)

@Serializable
data class TempCustomerRequest(
    val userName: String,
    val mobile: String
)
