package com.example.zeroerror.data.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.zeroerror.data.model.Order

@Dao
interface OrderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrderList(orders: List<Order>)

    @Query("SELECT * FROM `order`")
    fun getOrderList(): LiveData<List<Order>>

    @Query("SELECT item_id FROM `order`")
    fun getOrderIdList(): LiveData<List<Long>>

    @Update
    suspend fun updateOrderItem(order: Order)

    @Query("DELETE FROM `order`")
    suspend fun deleteAllOrderList()
}