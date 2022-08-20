package com.example.zeroerror.data.persistence

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.example.zeroerror.data.model.Order

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrderList(orders: List<Order>)

    @Update
    fun updateOrderItem(order: Order)

    @Query("SELECT * FROM `Order`")
    fun getOrderList(): List<Order>

    @Query("SELECT itemId FROM `Order`")
    fun getOrderIdList(): List<Int>
}