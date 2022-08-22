package com.example.zeroerror.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.zeroerror.data.persistence.OrderListTypeConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "inspect")
data class Inspect(
    @PrimaryKey
    @ColumnInfo(name = "inspect_id")
    val inspectId: Long,
    @ColumnInfo(name = "order_id")
    val orderId: Long,
    @ColumnInfo(name = "tracking_id")
    val trackingId: String,
    @ColumnInfo(name = "order_list")
    @TypeConverters(OrderListTypeConverter::class)
    @SerializedName("orderItemFormList")
    val orderList: List<Order>,
    @ColumnInfo(name="total_count")
    val totalCount: Int,
    @ColumnInfo(name = "check_count")
    @SerializedName("totalCheckCount")
    var checkCount: Int
)
