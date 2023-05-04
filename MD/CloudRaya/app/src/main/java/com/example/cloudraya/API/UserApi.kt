package com.example.cloudraya.API

import com.example.cloudraya.Model.UserRequest
import com.example.cloudraya.Model.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    @Headers("Content-Type:application/json")
    @POST("v1/api/gateway/user/auth")
    fun login(
        @Body userRequest: UserRequest
    ): Call <UserResponse>
}