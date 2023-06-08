package com.example.cloudraya.Verify

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.Util.NetworkResult
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class VerifyViewModel: ViewModel() {

    val responVerifyAction = MutableLiveData<NetworkResult>()
    val responVerifyNewVm = MutableLiveData<NetworkResult>()

    fun verifyAction(apiUrl: String, vmId: String, action: String, vCode : String){
        viewModelScope.launch {
            flow {
                val response = ApiService(apiUrl).instanceRetrofit.verifyAction(vmId, action, vCode)
                emit(response)
            }.onStart {
                responVerifyAction.value = NetworkResult.Loading(true)
            }.onCompletion {
                responVerifyAction.value = NetworkResult.Loading(false)
            }.catch {
                Log.e("Error ", it.message.toString())
            }.collect{
                responVerifyAction.value = NetworkResult.Success(it)
            }
        }
    }

    fun verifyNewVm(apiUrl: String, vmId: String, vCode : String){
        viewModelScope.launch {
            flow {
                val response = ApiService(apiUrl).instanceRetrofit.verifyNewVm(vmId, vCode)
                emit(response)
            }.onStart {
                responVerifyNewVm.value = NetworkResult.Loading(true)
            }.onCompletion {
                responVerifyNewVm.value = NetworkResult.Loading(false)
            }.catch {
                Log.e("Error ", it.message.toString())
            }.collect{
                responVerifyNewVm.value = NetworkResult.Success(it)
            }
        }
    }

}