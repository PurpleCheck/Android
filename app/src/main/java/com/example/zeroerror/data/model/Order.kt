package com.example.zeroerror.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

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
    @SerializedName("count")
    val totalCount: Int,
    @ColumnInfo(name = "check_count")
    var checkCount: Int,
    @ColumnInfo(name = "is_checked")
    @SerializedName("checked")
    var isChecked: Boolean
)
