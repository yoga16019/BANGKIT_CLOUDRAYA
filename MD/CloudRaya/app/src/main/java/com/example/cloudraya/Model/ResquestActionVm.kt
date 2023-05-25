package com.example.cloudraya.Model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResquestActionVm(
	@field:SerializedName("vm_id")
	var vmId: String? = null,

	@field:SerializedName("request")
	var request: String? = null,

	@field:SerializedName("release_ip")
	var releaseIp: Boolean? = null
) : Parcelable
