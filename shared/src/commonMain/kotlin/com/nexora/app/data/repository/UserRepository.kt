package com.nexora.app.data.repository

import com.nexora.app.data.api.UserApi
import com.nexora.app.data.model.user.CustomerRecord
import com.nexora.app.data.model.user.LoginRequest
import com.nexora.app.data.model.user.LoginResponse
import com.nexora.app.data.model.user.MobileLoginRequest
import com.nexora.app.data.model.user.Roles
import com.nexora.app.data.model.user.SendOtpRequest
import com.nexora.app.data.model.user.TempCustomerRequest
import com.nexora.app.data.model.user.User
import com.nexora.app.data.model.user.UserAddress
import com.nexora.app.data.model.user.UserProfile
import com.nexora.app.data.model.user.WalkInCustomerDto

class UserRepository {

    /*
     * ---------------------------------------------------------
     * Users
     * ---------------------------------------------------------
     */

    suspend fun getUsers(): Any {
        return UserApi.getUsers()
    }

    suspend fun saveUser(
        data: Any
    ): Any {
        return UserApi.saveUser(data)
    }

    suspend fun loginUser(
        request:LoginRequest
    ): LoginResponse {
        return UserApi.loginUser(request)
    }

    suspend fun getCustomLookUp(): Any {
        return UserApi.getCustomLookUp()
    }

    suspend fun createTempCustomer(): CustomerRecord {
        return UserApi.createTempCustomer()
    }

    suspend fun deleteUser(
        id: Long
    ): Any {
        return UserApi.deleteUser(id)
    }

    suspend fun updateUser(
        id: Long,
        data: Any
    ): Any {
        return UserApi.updateUser(
            id = id,
            data = data
        )
    }

    suspend fun getUserById(
        id: Long
    ): Any {
        return UserApi.getUserById(id)
    }

    suspend fun ifExistLoginId(
        loginId: String
    ): Boolean {
        return UserApi.ifExistLoginId(loginId)
    }

    /*
     * ---------------------------------------------------------
     * Roles
     * ---------------------------------------------------------
     */

    suspend fun getAllRoles(): Any {
        return UserApi.getAllRoles()
    }

    suspend fun getRolesByUser(
        userId: Long
    ): Any {
        return UserApi.getRolesByUser(userId)
    }

    suspend fun assignRolesToUser(
        userId: Long,
        roles: List<Roles>
    ): Any {
        return UserApi.assignRolesToUser(
            userId = userId,
            roles = roles
        )
    }

    suspend fun deleteUserRole(
        userId: Long,
        roleId: Long
    ): Any {
        return UserApi.deleteUserRole(
            userId = userId,
            roleId = roleId
        )
    }

    /*
     * ---------------------------------------------------------
     * Addresses
     * ---------------------------------------------------------
     */

    suspend fun getAddressesByUser(
        userId: Long
    ): Any {
        return UserApi.getAddressesByUser(userId)
    }

    suspend fun saveAddress(
        data: UserAddress
    ): Any {
        return UserApi.saveAddress(data)
    }

    suspend fun deleteAddress(
        id: Long
    ): Any {
        return UserApi.deleteAddress(id)
    }

    /*
     * ---------------------------------------------------------
     * Profile
     * ---------------------------------------------------------
     */

    suspend fun saveProfile(
        data: UserProfile
    ): Any {
        return UserApi.saveProfile(data)
    }

    /*
     * ---------------------------------------------------------
     * Custom Lookup
     * ---------------------------------------------------------
     */

    suspend fun getCustomCategory(): Any {
        return UserApi.getCustomCategory()
    }

    suspend fun saveCustomLookups(
        data: Any
    ): Any {
        return UserApi.saveCustomLookups(data)
    }

    suspend fun getCustomLookups(): Any {
        return UserApi.getCustomLookups()
    }

    suspend fun deleteCustomLookup(
        id: Long
    ): Any {
        return UserApi.deleteCustomLookup(id)
    }

    suspend fun getCustomLookupById(
        id: Long
    ): Any {
        return UserApi.getCustomLookupById(id)
    }

    /*
     * ---------------------------------------------------------
     * Email
     * ---------------------------------------------------------
     */

    suspend fun saveEmail(
        data: Any
    ): Any {
        return UserApi.saveEmail(data)
    }

    suspend fun makeEmailPrimary(
        emailId: Long
    ): Any {
        return UserApi.makeEmailPrimary(emailId)
    }

    suspend fun getAllEmails(
        userAddrID: Long
    ): Any {
        return UserApi.getAllEmails(userAddrID)
    }

    suspend fun deleteEmail(
        id: Long
    ): Any {
        return UserApi.deleteEmail(id)
    }

    /*
     * ---------------------------------------------------------
     * Mobile
     * ---------------------------------------------------------
     */

    suspend fun saveMobile(
        data: Any
    ): Any {
        return UserApi.saveMobile(data)
    }

    suspend fun getAllMobiles(
        userAddrID: Long
    ): Any {
        return UserApi.getAllMobiles(userAddrID)
    }

    suspend fun deleteMobile(
        id: Long
    ): Any {
        return UserApi.deleteMobile(id)
    }

    suspend fun setPrimaryMobile(
        userAddrID: Long,
        mobileId: Long
    ): Any {
        return UserApi.setPrimaryMobile(
            userAddrID = userAddrID,
            mobileId = mobileId
        )
    }

    /*
     * ---------------------------------------------------------
     * Location
     * ---------------------------------------------------------
     */

    suspend fun getAllStates(): Any {
        return UserApi.getAllStates()
    }

    suspend fun getAllCities(
        stateId: Long
    ): Any {
        return UserApi.getAllCities(stateId)
    }

    suspend fun getCities(): Any {
        return UserApi.getCities()
    }

    /*
     * ---------------------------------------------------------
     * Customer
     * ---------------------------------------------------------
     */

    suspend fun searchCustomer(
        mobile: String
    ): CustomerRecord {
        return UserApi.searchCustomer(mobile)
    }

    suspend fun updateTempCustomer(
        userId: Long,
        body: TempCustomerRequest
    ): Any {
        return UserApi.updateTempCustomer(
            userId = userId,
            body = body
        )
    }
    suspend fun createOrUpdateWalkInCustomer(
        body: WalkInCustomerDto
    ): User {
      return UserApi.createOrUpdateWalkInCustomer(
          body
      ) 
    }


    suspend fun sendOtp(mobile: String): String {
        val request = SendOtpRequest(mobile = mobile)
        return UserApi.sendOtp(request)
    }

    suspend fun loginWithOtp(mobile: String, otp: String): LoginResponse {
        val request = MobileLoginRequest(
            mobile = mobile,
            otp = otp
        )
        return UserApi.loginWithOtp(request)
    }
}
