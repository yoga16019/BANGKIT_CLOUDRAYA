package com.example.cloudraya.DetailVm

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.Local.SiteRegister
import com.example.cloudraya.Model.ResponseActionVm
import com.example.cloudraya.Model.ResponseDetailVm
import com.example.cloudraya.Model.ResquestActionVm
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log


class DetailVmViewModel(application: Application): AndroidViewModel(application) {

    val responseActionVm = MutableLiveData<ResponseActionVm>()
    val responseDetailVm = MutableLiveData<ResponseDetailVm>()

    fun getVmDetail(url: String, token: String, vmId: Int, refresh: SwipeRefreshLayout){
        ApiService(url).instanceRetrofit.getVMDetail(token, vmId).enqueue(object: Callback<ResponseDetailVm>{
            override fun onResponse(
                call: Call<ResponseDetailVm>,
                response: Response<ResponseDetailVm>,
            ) {
                if (refresh.isRefreshing){
                    refresh.isRefreshing=false
                }
                val respon = response.body()
                if (respon?.code== 200 ){
                    responseDetailVm.postValue(respon!!)
                    Log.e("berhasil ",response.message())
                }else{
                    Log.e("gagal ",response.message())
                }
            }

            override fun onFailure(call: Call<ResponseDetailVm>, t: Throwable) {
                Log.e("gagal ",t.message!!)
            }
        })
    }

    fun deleteVm(url: String,token: String, request: ResquestActionVm){
        ApiService(url).instanceRetrofit.actionVM(token, request).enqueue(object : Callback<ResponseActionVm> {
            override fun onResponse(
                call: Call<ResponseActionVm>,
                response: Response<ResponseActionVm>,
            ) {
                val respon = response.body()
                if (respon?.code == 200 ){
                    responseActionVm.postValue(respon!!)
                    Log.e("berhasil ",response.message())
                }else{
                    Log.e("gagal ",response.message())
                }
            }
            override fun onFailure(call: Call<ResponseActionVm>, t: Throwable) {
                Log.e("gagal ",t.message!!)
            }

        })
    }

    fun startVm(url: String,token: String, request: ResquestActionVm){
        ApiService(url).instanceRetrofit.actionVM(token, request).enqueue(object: Callback<ResponseActionVm>{
            override fun onResponse(
                call: Call<ResponseActionVm>,
                response: Response<ResponseActionVm>,
            ) {
                val respon = response.body()
                if (respon?.code == 200 ){
                    responseActionVm.postValue(respon!!)
                    Log.e("berhasil ",response.message())
                }else{
                    Log.e("gagal ",response.message())
                }
            }

            override fun onFailure(call: Call<ResponseActionVm>, t: Throwable) {
                Log.e("gagal ",t.message!!)
            }

        })
    }

    fun stopVm(url: String, token: String, request: ResquestActionVm){
        ApiService(url).instanceRetrofit.actionVM(token, request).enqueue(object: Callback<ResponseActionVm>{
            override fun onResponse(
                call: Call<ResponseActionVm>,
                response: Response<ResponseActionVm>,
            ) {
                val respon = response.body()
                if (respon?.code == 200 ){
                    responseActionVm.postValue(respon!!)
                    Log.e("berhasil ",response.message())
                }else{
                    Log.e("gagal ",response.message())
                }
            }

            override fun onFailure(call: Call<ResponseActionVm>, t: Throwable) {
                Log.e("gagal ",t.message!!)
            }

        })
    }
}