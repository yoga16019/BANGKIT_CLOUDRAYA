package com.example.cloudraya.Local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface SiteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(site: SiteRegister)

    @Query(" SELECT * FROM SiteList ")
    fun getAllSite(): LiveData<MutableList<SiteRegister>>

    @Delete
    suspend fun delete(site: SiteRegister)

}