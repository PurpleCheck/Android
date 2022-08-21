package com.example.zeroerror.data.repository

import androidx.lifecycle.LiveData
import com.example.zeroerror.data.model.Order
import com.example.zeroerror.data.persistence.OrderDao
import javax.inject.Inject

class OrderRepositoryImpl @Inject constructor(private val orderDao: OrderDao) {
    suspend fun insertOrderList(orderList:List<Order>) = orderDao.insertOrderList(orderList)
    suspend fun updateOrderItem(order: Order) = orderDao.updateOrderItem(order)
    fun getOrderList() = orderDao.getOrderList()
    fun getOrderIdList(): LiveData<List<Long>> = orderDao.getOrderIdList()
    suspend fun deleteAllOrderList() = orderDao.deleteAllOrderList()
}