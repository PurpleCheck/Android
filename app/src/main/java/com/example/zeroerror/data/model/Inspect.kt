package com.example.zeroerror.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Inspect(
    @PrimaryKey val inspectId: Long,
    val orderId: Long,
    val trackingId: String,
    val orderList: List<Order>,
    val totalCount: Int,
    var checkCount: Int
)

data class InspectFinish(
    val finish: Boolean
)
