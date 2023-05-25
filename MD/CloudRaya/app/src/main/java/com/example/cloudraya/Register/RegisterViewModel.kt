package com.example.cloudraya.Register

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cloudraya.Local.SiteDao
import com.example.cloudraya.Local.SiteDatabase
import com.example.cloudraya.Local.SiteRegister
import kotlinx.coroutines.launch

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private var siteDao : SiteDao?
    private var siteDb : SiteDatabase?

    init {
        siteDb = SiteDatabase.getDatabase(application)
        siteDao = siteDb?.SiteDao()
    }

    fun addSite(site: SiteRegister){
        viewModelScope.launch {
            siteDao?.insert(site)
        }
    }
//    fun getToken(item: String) : String = siteDao?.getToken(item).toString()

}