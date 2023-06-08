package com.example.cloudraya.Model

import android.os.Parcelable
import com.google.gson.annotations.Expose
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserResponse(
    @Expose
    val code: Int? = null,
    @Expose
    val data: Data? =null,
    @Expose
    val message: String? = null,
    @Expose
    val error: String? =null
):Parcelable
