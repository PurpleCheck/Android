package com.example.zeroerror.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order")
data class Order(
    @PrimaryKey
    @ColumnInfo(name = "item_id")
    val itemId: Long,
    @ColumnInfo(name = "brand_name")
    val brandName: String,
    @ColumnInfo(name = "item_name")
    val itemName: String,
    @ColumnInfo(name = "total_count")
    val totalCount: Int,
    @ColumnInfo(name = "check_count")
    var checkCount: Int,
    @ColumnInfo(name = "is_checked")
    var isChecked: Boolean
)
