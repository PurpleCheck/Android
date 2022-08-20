package com.example.zeroerror.data.model

data class Inspect(
    val inspectId: Int,
    val orderId: Int,
    val trackingId: String,
    val orderList: List<Order>,
    val totalCount: Int,
    var checkCount: Int
)
