package com.example.cloudraya.SiteList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cloudraya.Local.SiteDao
import com.example.cloudraya.Local.SiteDatabase
import com.example.cloudraya.Local.SiteRegister

class SiteListViewModel(application: Application): AndroidViewModel(application) {

    private var siteDao : SiteDao?
    private var siteDb : SiteDatabase?

    init {
        siteDb = SiteDatabase.getDatabase(application)
        siteDao = siteDb?.SiteDao()
    }

    fun getSite(): LiveData<MutableList<SiteRegister>>{
        return siteDao?.getAllSite()!!
    }

}