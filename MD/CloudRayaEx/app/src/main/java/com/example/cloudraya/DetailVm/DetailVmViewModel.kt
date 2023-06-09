package com.example.cloudraya.DetailVm

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.Model.ResponseActionVm
import com.example.cloudraya.Model.ResponseDetailVm
import com.example.cloudraya.Model.ResquestActionVm
import com.example.cloudraya.Util.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class DetailVmViewModel(application: Application): AndroidViewModel(application) {

    val responseActionVm = MutableLiveData<NetworkResult>()
    val responseDetailVm = MutableLiveData<NetworkResult>()

    //get detail vm from api
    fun getVmDetail(apiUrl: String, vmId:Int){
        viewModelScope.launch {
            flow {
                val response = ApiService(apiUrl).instanceRetrofit.getVmDetail(vmId)
                emit(response)
            }.onStart {
                responseDetailVm.value = NetworkResult.Loading(true)
            }.onCompletion {
                responseDetailVm.value = NetworkResult.Loading(false)
            }.catch {
                Log.e("Error ", it.message.toString())
            }.collect{
                responseDetailVm.value = NetworkResult.Success(it.data)
            }
        }
    }

    // start/stop vm
    fun actionVm(apiUrl: String, vmId:Int, vmAction: String){
        viewModelScope.launch {
            flow {
                val response = ApiService(apiUrl).instanceRetrofit.actionVm(vmId,vmAction)
                emit(response)
            }.onStart {
                responseActionVm.value = NetworkResult.Loading(true)
            }.onCompletion {
                responseActionVm.value = NetworkResult.Loading(false)
            }.catch {
                Log.e("Error ", it.message.toString())
            }.collect{
                responseActionVm.value = NetworkResult.Success(it)
            }
        }
    }
}