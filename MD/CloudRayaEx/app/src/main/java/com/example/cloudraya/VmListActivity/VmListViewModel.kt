package com.example.cloudraya.VmListActivity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.Local.SiteDao
import com.example.cloudraya.Local.SiteDatabase

import com.example.cloudraya.Util.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class VmListViewModel(application: Application): AndroidViewModel(application) {

    private var siteDao : SiteDao?
    private var siteDb : SiteDatabase?

    init {
        siteDb = SiteDatabase.getDatabase(application)
        siteDao = siteDb?.SiteDao()
    }



    val listVm = MutableLiveData<NetworkResult>()

    fun getVm(apiUrl: String){
        viewModelScope.launch {
            flow {
                val response = ApiService(apiUrl).instanceRetrofit.getVmList()
                emit(response)
            }.onStart {
                listVm.value = NetworkResult.Loading(true)
            }.onCompletion {
                listVm.value = NetworkResult.Loading(false)
            }.catch {
                Log.e("Error ", it.message.toString())
            }.collect{
                listVm.value = NetworkResult.Success(it.data)

            }
        }
    }


}