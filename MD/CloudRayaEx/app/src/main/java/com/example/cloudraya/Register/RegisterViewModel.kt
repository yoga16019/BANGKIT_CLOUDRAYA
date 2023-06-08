package com.example.cloudraya.Register

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.Local.SiteDao
import com.example.cloudraya.Local.SiteDatabase
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.ModelApiTest.ResponseLogin
import com.example.cloudraya.Util.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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

    val registerResponse = MutableLiveData<NetworkResult>()

     fun register(token: String, apiUrl: String){
        viewModelScope.launch {
            flow {
                val response = ApiService(apiUrl).instanceRetrofit.login(token)
                emit(response)
            }.onStart {
                registerResponse.value = NetworkResult.Loading(true)
            }.onCompletion {
                registerResponse.value = NetworkResult.Loading(false)
            }.catch {
                Log.e("Error ", it.message.toString())
            }.collect{
                registerResponse.value = NetworkResult.Success(it)
            }
        }
    }

}