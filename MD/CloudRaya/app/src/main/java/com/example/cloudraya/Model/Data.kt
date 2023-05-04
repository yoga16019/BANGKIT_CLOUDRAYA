package com.example.cloudraya.Model

import com.google.gson.annotations.Expose

data class Data(
    @Expose
    val bearer_token: String? = null,
    @Expose
    val username: String? = null
)
