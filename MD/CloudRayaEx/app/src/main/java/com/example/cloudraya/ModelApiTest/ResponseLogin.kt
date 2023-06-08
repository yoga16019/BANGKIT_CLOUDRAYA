package com.example.cloudraya.ModelApiTest

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseLogin(

	@field:SerializedName("detail")
	val detail: List<DetailItem?>? = null
) : Parcelable

@Parcelize
data class DetailItem(

	@field:SerializedName("msg")
	val msg: String? = null,

	@field:SerializedName("loc")
	val loc: List<String?>? = null,

	@field:SerializedName("type")
	val type: String? = null
) : Parcelable
