package com.example.zeroerror.data.model

data class Order(
    val itemId: Int,
    val brandName: String,
    val itemName: String,
    val totalCount: Int,
    var checkCount: Int,
    var isChecked: Boolean
)
