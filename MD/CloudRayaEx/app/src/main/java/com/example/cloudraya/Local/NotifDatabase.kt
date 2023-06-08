package com.example.cloudraya.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [NotifData::class],
    version = 1, exportSchema = false )
abstract class NotifDatabase : RoomDatabase(){
    companion object{
        var INSTANCE : NotifDatabase? = null

        fun getDatabase(context: Context): NotifDatabase?{
            if (INSTANCE == null){
                synchronized(NotifDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, NotifDatabase::class.java, "Notif.db").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun notifDao(): NotifDao
}