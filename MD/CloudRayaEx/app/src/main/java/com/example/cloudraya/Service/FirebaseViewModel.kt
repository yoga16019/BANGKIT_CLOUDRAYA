package com.example.cloudraya.Service

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cloudraya.API.ApiService
import com.example.cloudraya.Local.*
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

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

//    private var siteDao : SiteDao?
//    private var siteDb : SiteDatabase?
    private var notifDao : NotifDao?
    private var notifDb : NotifDatabase?

    init {
        notifDb = NotifDatabase.getDatabase(application)
        notifDao = notifDb?.notifDao()
    }

    fun addNotif(notifData: NotifData){
        viewModelScope.launch {
            notifDao?.insert(notifData)
        }
    }

}