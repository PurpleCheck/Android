package com.example.zeroerror.data.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.model.Order

@Database(entities = [Order::class, Inspect::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun orderDao(): OrderDao
    abstract fun InspectDao(): InspectDao

    companion object {
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "database-name"
                    ).build()
                }
            }
            return instance
        }
    }
}