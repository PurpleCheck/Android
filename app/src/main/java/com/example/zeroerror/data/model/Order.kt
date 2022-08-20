package com.example.zeroerror.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Order(
    @PrimaryKey val itemId: Long,
    val brandName: String,
    val itemName: String,
    val totalCount: Int,
    var checkCount: Int,
    var isChecked: Boolean
)
