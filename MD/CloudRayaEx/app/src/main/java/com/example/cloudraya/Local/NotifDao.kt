package com.example.cloudraya.Local

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NotifDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insert(notif: NotifData)
    @Query(" SELECT * FROM NotifList ")
    fun getAllNotif(): LiveData<MutableList<NotifData>>
    @Delete
    suspend fun delete(notif: NotifData)

}