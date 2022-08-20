package com.example.zeroerror.data.persistence

import android.content.Context
import androidx.room.*
import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.model.Order
import com.google.gson.Gson

@Database(entities = [Order::class, Inspect::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {

    abstract fun orderDao(): OrderDao
    abstract fun InspectDao(): InspectDao

    companion object {
        private var instance: AppDatabase? = null
        private val gson = Gson()

        @Synchronized
        fun getInstance(context: Context): AppDatabase? {
            if (instance == null) {
                synchronized(AppDatabase::class){
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "database-name"
                    )
                        .addTypeConverter(OrderListTypeConverter(gson))
                        .addTypeConverter(OrderTypeConverter(gson))
                        .build()
                }
            }
            return instance
        }
    }
    @ProvidedTypeConverter
    class OrderListTypeConverter(private val gson: Gson) {

        @TypeConverter
        fun listToJson(value: List<Order>): String? {
            return gson.toJson(value)
        }

        @TypeConverter
        fun jsonToList(value: String): List<Order> {
            return gson.fromJson(value, Array<Order>::class.java).toList()
        }
    }

    @ProvidedTypeConverter
    class OrderTypeConverter(private val gson: Gson) {

        @TypeConverter
        fun listToJson(value: Order): String? {
            return gson.toJson(value)
        }

        @TypeConverter
        fun jsonToList(value: String): Order {
            return gson.fromJson(value, Order::class.java)
        }
    }
}