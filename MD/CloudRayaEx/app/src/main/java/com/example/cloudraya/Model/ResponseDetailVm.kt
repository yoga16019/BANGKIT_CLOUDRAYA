package com.example.cloudraya.Model

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class ResponseDetailVm(

	@field:SerializedName("code")
	val code: Int? = null,

	@field:SerializedName("data")
	val data: DataVm? = null,

	@field:SerializedName("message")
	val message: String? = null
) : Parcelable {
	@Parcelize
	data class DataVm(

		@field:SerializedName("country")
		val country: String? = null,

		@field:SerializedName("public_ip")
		val publicIp: String? = null,

		@field:SerializedName("memory")
		val memory: Int? = null,

		@field:SerializedName("package")
		val jsonMemberPackage: String? = null,

		@field:SerializedName("os")
		val os: String? = null,

		@field:SerializedName("timezone")
		val timezone: String? = null,

		@field:SerializedName("cpu")
		val cpu: String? = null,

		@field:SerializedName("ostype")
		val ostype: String? = null,

		@field:SerializedName("private_ip")
		val privateIp: List<String?>? = null,

		@field:SerializedName("location_id")
		val locationId: Int? = null,

		@field:SerializedName("security_profile")
		val securityProfile: String? = null,

		@field:SerializedName("hostname")
		val hostname: String? = null,

		@field:SerializedName("disk")
		val disk: String? = null,

		@field:SerializedName("location")
		val location: String? = null,

		@field:SerializedName("currency")
		val currency: String? = null,

		@field:SerializedName("state")
		val state: String? = null,

		@field:SerializedName("launch_date")
		val launchDate: String? = null,

		@field:SerializedName("username")
		val username: String? = null,

		@field:SerializedName("status")
		val status: String? = null
	) : Parcelable
}
