package com.example.cloudraya.Notifications

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.cloudraya.Local.*

class NotificationsViewModel(application: Application): AndroidViewModel(application) {

    private var notifDao : NotifDao?
    private var notifDb : NotifDatabase?

    init {
        notifDb = NotifDatabase.getDatabase(application)
        notifDao = notifDb?.notifDao()
    }

    fun getSite(): LiveData<MutableList<NotifData>>{
        return notifDao!!.getAllNotif()
    }

}