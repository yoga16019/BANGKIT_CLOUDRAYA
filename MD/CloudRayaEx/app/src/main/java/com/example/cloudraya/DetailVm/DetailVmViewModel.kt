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

//    fun getVmDetail(url: String, token: String, vmId: Int, refresh: SwipeRefreshLayout){
//        ApiService(url).instanceRetrofit.getVMDetail(token, vmId).enqueue(object: Callback<ResponseDetailVm>{
//            override fun onResponse(
//                call: Call<ResponseDetailVm>,
//                response: Response<ResponseDetailVm>,
//            ) {
//                if (refresh.isRefreshing){
//                    refresh.isRefreshing=false
//                }
//                val respon = response.body()
//                if (respon?.code== 200 ){
//                    responseDetailVm.postValue(respon!!)
//                    Log.e("berhasil ",response.message())
//                }else{
//                    Log.e("gagal ",response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseDetailVm>, t: Throwable) {
//                Log.e("gagal ",t.message!!)
//            }
//        })
//    }
//
//    fun deleteVm(url: String,token: String, request: ResquestActionVm){
//        ApiService(url).instanceRetrofit.actionVM(token, request).enqueue(object : Callback<ResponseActionVm> {
//            override fun onResponse(
//                call: Call<ResponseActionVm>,
//                response: Response<ResponseActionVm>,
//            ) {
//                val respon = response.body()
//                if (respon?.code == 200 ){
//                    responseActionVm.postValue(respon!!)
//                    Log.e("berhasil ",response.message())
//                }else{
//                    Log.e("gagal ",response.message())
//                }
//            }
//            override fun onFailure(call: Call<ResponseActionVm>, t: Throwable) {
//                Log.e("gagal ",t.message!!)
//            }
//
//        })
//    }
//
//    fun startVm(url: String,token: String, request: ResquestActionVm){
//        ApiService(url).instanceRetrofit.actionVM(token, request).enqueue(object: Callback<ResponseActionVm>{
//            override fun onResponse(
//                call: Call<ResponseActionVm>,
//                response: Response<ResponseActionVm>,
//            ) {
//                val respon = response.body()
//                if (respon?.code == 200 ){
//                    responseActionVm.postValue(respon!!)
//                    Log.e("berhasil ",response.message())
//                }else{
//                    Log.e("gagal ",response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseActionVm>, t: Throwable) {
//                Log.e("gagal ",t.message!!)
//            }
//
//        })
//    }
//
//    fun stopVm(url: String, token: String, request: ResquestActionVm){
//        ApiService(url).instanceRetrofit.actionVM(token, request).enqueue(object: Callback<ResponseActionVm>{
//            override fun onResponse(
//                call: Call<ResponseActionVm>,
//                response: Response<ResponseActionVm>,
//            ) {
//                val respon = response.body()
//                if (respon?.code == 200 ){
//                    responseActionVm.postValue(respon!!)
//                    Log.e("berhasil ",response.message())
//                }else{
//                    Log.e("gagal ",response.message())
//                }
//            }
//
//            override fun onFailure(call: Call<ResponseActionVm>, t: Throwable) {
//                Log.e("gagal ",t.message!!)
//            }
//
//        })
//    }
}