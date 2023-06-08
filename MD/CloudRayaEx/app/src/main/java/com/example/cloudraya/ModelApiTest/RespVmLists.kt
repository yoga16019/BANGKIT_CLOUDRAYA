package com.example.cloudraya.ModelApiTest

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseVmList(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: MutableList<DataVm?>? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable

@Parcelize
data class DataVm(

	@field:SerializedName("is_active")
	val isActive: Boolean? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("state")
	val state: Boolean? = null,

	@field:SerializedName("email")
	val email: String? = null
) : Parcelable
