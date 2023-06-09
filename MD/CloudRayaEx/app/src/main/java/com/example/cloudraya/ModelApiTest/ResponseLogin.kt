package com.example.cloudraya.ModelApiTest

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseLogin(

	@field:SerializedName("token")
	val token: String? = null,

	@field:SerializedName("id")
	val id: Int ? = null

) : Parcelable
