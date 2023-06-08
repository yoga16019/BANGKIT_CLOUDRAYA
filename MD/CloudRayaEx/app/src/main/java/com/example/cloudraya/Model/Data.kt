package com.example.cloudraya.Model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Data(
    var bearer_token: String? = null,
    val username: String? = null
):Parcelable
