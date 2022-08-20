package com.example.zeroerror.ui.CheckProduct

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.zeroerror.data.exampleDataList
import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.model.Order

class CheckProductViewModel: ViewModel() {

    var invoiceNumber= MutableLiveData<String>()

    private val mLiveProductList = MutableLiveData<ArrayList<Order>>()
    var mProductList = arrayListOf<Order>()

    val productList : LiveData<ArrayList<Order>>
        get(){
            return mLiveProductList
        }

    val productIdList = mutableListOf<String>()

    var productListTotalCount = MutableLiveData<Int>()

    val productListCheckedCount = MutableLiveData<Int>()

    val inspectItem = MutableLiveData<Inspect>()

    init{
        mProductList = ArrayList(exampleDataList.inspectList[0].orderList)
        mLiveProductList.value = mProductList
        invoiceNumber.value = exampleDataList.inspectList[0].trackingId
        productListTotalCount.value = exampleDataList.inspectList[0].totalCount
        productListCheckedCount.value = exampleDataList.inspectList[0].checkCount
        inspectItem.value = exampleDataList.inspectList[0]

        for(item in productList.value!!){
            productIdList.add(item.itemId.toString())
        }
    }

    // item별 checkCount 증가
    fun updateCount(order:Order){
        if(order.checkCount!=order.totalCount){
            order.checkCount+=1
            mLiveProductList.value = mProductList
        }
    }

    // item별로 수량에 맞게 검수 완료 시 isChecked true로 update
    fun updateIsChecked(order:Order){
        if(!order.isChecked && order.checkCount==order.totalCount){
            order.isChecked = true
            mLiveProductList.value = mProductList
        }
    }

    // 전체 검수 상품 중 검수 완료된 상품 수량 증가
    fun updateListCheckedCount(){
        inspectItem.value!!.checkCount += 1
        productListCheckedCount.value = inspectItem.value!!.checkCount
    }
}