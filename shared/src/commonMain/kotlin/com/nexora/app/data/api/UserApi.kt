package com.nexora.app.data.api

import com.nexora.app.core.network.ApiService
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

object UserApi {


    /*
     * ---------------------------------------------------------
     * Users
     * ---------------------------------------------------------
     */

    suspend fun getUsers(): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user",
            method = "GET"
        )
    }

    suspend fun saveUser(
        data: Any
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/register",
            method = "POST",
            body = data
        )
    }


    suspend fun loginUser(
        request: LoginRequest
    ): LoginResponse {

        return ApiService.request<LoginResponse, LoginRequest>(
            service = "user",
            endpoint = "/auth/login",
            method = "POST",
            body = request
        )
    }

    suspend fun getCustomLookUp(): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/getByCustomLookup",
            method = "GET"
        )
    }

    suspend fun createTempCustomer(): CustomerRecord {
        return ApiService.request(
            service = "user",
            endpoint = "/user/customer/temp",
            method = "POST"
        )
    }

    suspend fun deleteUser(
        id: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/delete/$id",
            method = "DELETE"
        )
    }

    suspend fun updateUser(
        id: Long,
        data: Any
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/update/$id",
            method = "PUT",
            body = data
        )
    }

    suspend fun getUserById(
        id: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/users/$id",
            method = "GET"
        )
    }

    suspend fun ifExistLoginId(
        loginId: String
    ): Boolean {

        return ApiService.request(
            service = "user",
            endpoint = "/user/auth/existLoginId?loginId=$loginId",
            method = "GET"
        )
    }

    /*
     * ---------------------------------------------------------
     * Roles
     * ---------------------------------------------------------
     */

    suspend fun getAllRoles(): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/details",
            method = "GET"
        )
    }

    suspend fun getRolesByUser(
        userId: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/roles/$userId",
            method = "GET"
        )
    }

    suspend fun assignRolesToUser(
        userId: Long,
        roles: List<Roles>
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/role/$userId",
            method = "POST",
            body = roles
        )
    }

    suspend fun deleteUserRole(
        userId: Long,
        roleId: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/$userId/$roleId",
            method = "DELETE"
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
        return ApiService.request(
            service = "user",
            endpoint = "/user/getAddressByUser/$userId",
            method = "GET"
        )
    }

    suspend fun saveAddress(
        data: UserAddress
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/saveAddress",
            method = "POST",
            body = data
        )
    }

    suspend fun deleteAddress(
        id: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/delete-address/$id",
            method = "DELETE"
        )
    }

    /*
     * ---------------------------------------------------------
     * Profile
     * ---------------------------------------------------------
     */

    suspend fun saveProfile(
        data: UserProfile
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/saveprofile",
            method = "POST",
            body = data
        )
    }

    /*
     * ---------------------------------------------------------
     * Custom Lookup
     * ---------------------------------------------------------
     */

    suspend fun getCustomCategory(): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/all-category",
            method = "GET"
        )
    }

    suspend fun saveCustomLookups(
        data: Any
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/save-look",
            method = "POST",
            body = data
        )
    }

    suspend fun getCustomLookups(): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/getAll-lookup",
            method = "GET"
        )
    }

    suspend fun deleteCustomLookup(
        id: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/delete-lookup/$id",
            method = "DELETE"
        )
    }

    suspend fun getCustomLookupById(
        id: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/$id",
            method = "GET"
        )
    }

    /*
     * ---------------------------------------------------------
     * Email
     * ---------------------------------------------------------
     */

    suspend fun saveEmail(
        data: Any
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/email",
            method = "POST",
            body = data
        )
    }

    suspend fun makeEmailPrimary(
        emailId: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/email/$emailId/primary",
            method = "PUT"
        )
    }

    suspend fun getAllEmails(
        userAddrID: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/all-email/$userAddrID",
            method = "GET"
        )
    }

    suspend fun deleteEmail(
        id: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/delete-email/$id",
            method = "DELETE"
        )
    }

    /*
     * ---------------------------------------------------------
     * Mobile
     * ---------------------------------------------------------
     */

    suspend fun saveMobile(
        data: Any
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/mobile",
            method = "POST",
            body = data
        )
    }

    suspend fun getAllMobiles(
        userAddrID: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/all-mobile/$userAddrID",
            method = "GET"
        )
    }

    suspend fun deleteMobile(
        id: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/delete-mobile/$id",
            method = "DELETE"
        )
    }

    suspend fun setPrimaryMobile(
        userAddrID: Long,
        mobileId: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/$userAddrID/mobiles/set-primary/$mobileId",
            method = "PUT"
        )
    }

    /*
     * ---------------------------------------------------------
     * Location
     * ---------------------------------------------------------
     */

    suspend fun getAllStates(): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/all-states",
            method = "GET"
        )
    }

    suspend fun getAllCities(
        stateId: Long
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/all-cities?stateId=$stateId",
            method = "GET"
        )
    }

    suspend fun getCities(): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/cities",
            method = "GET"
        )
    }

    /*
     * ---------------------------------------------------------
     * Customer
     * ---------------------------------------------------------
     */

    suspend fun searchCustomer(
        mobile: String
    ): CustomerRecord {
        return ApiService.request(
            service = "user",
            endpoint = "/user/customer/mobile/$mobile",
            method = "GET"
        )
    }

    suspend fun createOrUpdateWalkInCustomer(
        body: WalkInCustomerDto
    ): User {
        return ApiService.request(
            service = "user",
            endpoint = "/user/customer/temp/data",
            method = "POST",
           body = body
        )
    }
    suspend fun updateTempCustomer(
        userId: Long,
        body: TempCustomerRequest
    ): Any {
        return ApiService.request(
            service = "user",
            endpoint = "/user/customer/temp/$userId",
            method = "PUT",
            body = body
        )
    }
    suspend fun sendOtp(request: SendOtpRequest): String {
        return ApiService.request<String, SendOtpRequest>(
            service = "user",
            endpoint = "/auth/mobile/send-otp",
            method = "POST",
            body = request
        )
    }

    suspend fun loginWithOtp(request: MobileLoginRequest): LoginResponse {
        return ApiService.request<LoginResponse, MobileLoginRequest>(
            service = "user",
            endpoint = "/auth/login/mobile",
            method = "POST",
            body = request
        )
    }

}
