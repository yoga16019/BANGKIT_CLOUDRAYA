package com.example.cloudraya.Util

sealed class NetworkResult{
    data class Success<out T>(val data: T): NetworkResult()
    data class Error(val exception: Throwable): NetworkResult()
    data class Loading(val isLoading: Boolean): NetworkResult()
}