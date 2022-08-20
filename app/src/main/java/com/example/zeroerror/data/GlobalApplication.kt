package com.example.zeroerror.data

import android.app.Application
import androidx.room.Room
import com.example.zeroerror.data.persistence.AppDatabase

class GlobalApplication : Application(){
    companion object{
        lateinit var appInstance: GlobalApplication

        lateinit var appDatabaseInstance: AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        appDatabaseInstance = Room.databaseBuilder(
            appInstance.applicationContext,
            AppDatabase::class.java, "zeroError.db"
        )
            .fallbackToDestructiveMigration()
            .allowMainThreadQueries()
            .build()
    }
}