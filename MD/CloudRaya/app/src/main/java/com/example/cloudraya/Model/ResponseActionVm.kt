package com.example.cloudraya.Model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseActionVm(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable
