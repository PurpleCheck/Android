package com.example.zeroerror.data.persistence


import android.content.Context
import androidx.room.*
import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.model.Order
import com.google.gson.Gson

@Database(entities = [Order::class, Inspect::class], version = 1, exportSchema = false)
@TypeConverters(OrderListTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun orderDao(): OrderDao
    abstract fun InspectDao(): InspectDao

    companion object {
        private var INSTANCE : AppDatabase? = null

        fun getInstance(context: Context): AppDatabase ? {
            if(INSTANCE==null){
                INSTANCE= Room
                    .databaseBuilder(context, AppDatabase::class.java, "zero_error.db")
                    .addTypeConverter(OrderListTypeConverter(Gson()))
                    .build()
            }
            return INSTANCE
        }
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