package com.example.cloudraya.Local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [SiteRegister::class],
    version = 1, exportSchema = false )
abstract class SiteDatabase : RoomDatabase(){
    companion object{
        var INSTANCE : SiteDatabase? = null

        fun getDatabase(context: Context): SiteDatabase?{
            if (INSTANCE == null){
                synchronized(SiteDatabase::class){
                    INSTANCE = Room.databaseBuilder(context.applicationContext, SiteDatabase::class.java, "Sitesss.db").allowMainThreadQueries().build()
                }
            }
            return INSTANCE
        }
    }

    abstract fun SiteDao(): SiteDao
}