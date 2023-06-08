package com.example.cloudraya.ModelApiTest

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class NotificationsData(
    val title: String? = null,
    val body: String? = null,
    val vm_id: String? = null,
    val action: String? = null
): Parcelable
