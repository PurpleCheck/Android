package com.example.zeroerror.data.persistence


import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.zeroerror.data.exampleDataList
import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.model.Order
import com.google.gson.Gson
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors

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
                    .databaseBuilder(context, AppDatabase::class.java, "kim_ready.db")
                    .addTypeConverter(OrderListTypeConverter(Gson()))
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)

                            Executors.newSingleThreadExecutor().execute {
                                runBlocking {
                                    INSTANCE!!.orderDao().insertOrderList(exampleDataList.inspectList[0].orderList)
                                    INSTANCE!!.InspectDao().insertInspectItem(exampleDataList.inspectList[0])
                                }
                            }
                        }
                    })
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