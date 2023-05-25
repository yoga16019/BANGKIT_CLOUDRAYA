package com.example.cloudraya.Local

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "SiteList")
data class SiteRegister(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    @ColumnInfo(name = "site_name")
    val site_name: String,
    @ColumnInfo(name = "api_url")
    val api_url: String,
    @ColumnInfo(name = "app_key")
    val app_key: String,
    @ColumnInfo(name = "secret_key")
    val secret_key: String,
    @ColumnInfo(name = "bearer_token")
    val bearer_token: String
) : Parcelable
