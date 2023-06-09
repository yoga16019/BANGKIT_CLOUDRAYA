package com.example.cloudraya.API

import com.example.cloudraya.Model.*
import com.example.cloudraya.ModelApiTest.ResponseActionVmVerify
import com.example.cloudraya.ModelApiTest.ResponseDetailVm
import com.example.cloudraya.ModelApiTest.ResponseLogin
import okhttp3.ResponseBody

import com.example.cloudraya.ModelApiTest.ResponseVmList
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserApi {
    @POST("/v1/api/gateway/user/")
    suspend fun login(
        @Query("token") token: String
    ): ResponseLogin

    @POST("/v1/api/gateway/user/virtualmachines/action")
    suspend fun actionVm(
        @Query("vm_id") vmId: Int,
        @Query("request") action: String
    ): com.example.cloudraya.ModelApiTest.ResponseActionVm

    @GET("/v1/api/gateway/user/virtualmachines")
    suspend fun getVmList(): ResponseVmList

    @GET("/v1/api/gateway/user/virtualmachines/{id}")
    suspend fun getVmDetail(@Path("id") vmId: Int) : ResponseDetailVm

    @POST("/v1/api/gateway/user/virtualmachines/actionpc")
    suspend fun verifyAction(
        @Query("vm_id") vmId: String,
        @Query("request") action: String,
        @Query("vcode") vcode: String
    ) : ResponseActionVmVerify

    @POST("/v1/api/gateway/user/virtualmachines/verifynewvm/{vm_id}")
    suspend fun verifyNewVm(
        @Path("vm_id") id: String,
        @Query("vcode") vCode: String
    ) : String
}