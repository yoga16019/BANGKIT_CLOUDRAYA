package com.example.cloudraya.ModelApiTest

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ResponseActionVmVerify(
	val code: Int? = null,
	val data: Boolean? = null,
	val message: String? = null
) : Parcelable
