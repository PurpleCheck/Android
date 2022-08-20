package com.example.zeroerror.data.repository

import android.content.Context
import android.util.Log
import com.example.zeroerror.data.GlobalApplication
import com.example.zeroerror.data.exampleDataList
import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.model.Order
import com.example.zeroerror.data.network.RetrofitService
import com.example.zeroerror.data.persistence.AppDatabase
import com.example.zeroerror.data.persistence.OrderDao
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository {
    private val appDBInstance = GlobalApplication.appDatabaseInstance.orderDao()

    suspend fun insertOrderList(orderList:List<Order>) = appDBInstance.insertOrderList(orderList)
    fun updateOrderItem(order: Order) = appDBInstance.updateOrderItem(order)
    fun getOrderList() = appDBInstance.getOrderList()
    fun getOrderIdList() = appDBInstance.getOrderIdList()
}