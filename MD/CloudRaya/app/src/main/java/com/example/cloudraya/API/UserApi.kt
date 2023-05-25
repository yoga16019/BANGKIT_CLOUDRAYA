package com.example.cloudraya.API

import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.Model.*
import retrofit2.Call
import retrofit2.http.*

interface UserApi {
    @Headers("Content-Type:application/json")
    @POST("/v1/api/gateway/user/auth")
    fun login(
        @Body userRequest: UserRequest
    ): Call <UserResponse>

    @GET("/v1/api/gateway/user/virtualmachines")
    fun getVmList(@Header("Authorization") authorization: String): Call<ResponseVmList>

    @GET("/v1/api/gateway/user/virtualmachines/{id}")
    fun getVMDetail(@Header("Authorization") authorization: String, @Path("id") vmId: Int): Call<ResponseDetailVm>

    @POST("/v1/api/gateway/user/virtualmachines/action")
    fun actionVM(@Header("Authorization") authorization: String, @Body requestActionVm: ResquestActionVm) :Call<ResponseActionVm>

    @POST("/v1/api/gateway/user/auth")
    fun login2(
        @Body userRequest: UserRequest
    ): Call <SiteRegister>
}