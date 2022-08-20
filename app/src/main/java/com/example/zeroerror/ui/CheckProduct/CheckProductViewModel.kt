package com.example.zeroerror.ui.CheckProduct

import androidx.lifecycle.*
import com.example.zeroerror.data.model.Order
import com.example.zeroerror.data.repository.InspectRepositoryImpl
import com.example.zeroerror.data.repository.OrderRepositoryImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CheckProductViewModel @Inject constructor(
    private val orderRepository: OrderRepositoryImpl,
    private val inspectRepository: InspectRepositoryImpl): ViewModel() {

    val productList = orderRepository.getOrderList()
    val inspectItem = inspectRepository.getInspectItem()
    val productIdList = orderRepository.getOrderIdList()
    val productListTotalCount = inspectRepository.getTotalCount()
    val productListCheckedCount= inspectRepository.getCheckCount()
    val trackingId = inspectRepository.getTrackingId()

    val restCount: LiveData<Int> =
        combine(productListTotalCount.asFlow(), productListCheckedCount.asFlow()){ total, check ->
            total - check
    }.asLiveData()

    val progress: LiveData<Int> =
        combine(productListTotalCount.asFlow(), productListCheckedCount.asFlow()){ total, check ->
            ((check.toFloat() / total.toFloat())*100.0f).toInt()
        }.asLiveData()

    // item별 checkCount 증가
    fun updateCount(order:Order){
        if(order.checkCount!=order.totalCount){
            order.checkCount+=1
            viewModelScope.launch {
                orderRepository.updateOrderItem(order)
            }
        }
    }

    // item별로 수량에 맞게 검수 완료 시 isChecked true로 update
    fun updateIsChecked(order:Order){
        if(!order.isChecked && order.checkCount==order.totalCount){
            order.isChecked = true
            viewModelScope.launch {
                orderRepository.updateOrderItem(order)
            }
        }
    }

    // 전체 검수 상품 중 검수 완료된 상품 수량 증가
    fun updateListCheckedCount(){
        inspectItem.value!!.checkCount += 1
        productListCheckedCount.value?.plus(1)
        viewModelScope.launch {
            inspectRepository.updateCheckCount(inspectItem.value!!)
        }
    }
}