package com.example.cloudraya.Model

import com.google.gson.annotations.Expose

data class UserResponse(
    @Expose
    val code: Int? = null,
    @Expose
    val data: Data? =null,
    @Expose
    val message: String? = null,
    @Expose
    val error: String? =null
)
