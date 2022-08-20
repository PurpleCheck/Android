package com.example.zeroerror.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.persistence.InspectDao
import javax.inject.Inject

class InspectRepositoryImpl @Inject constructor(private val inspectDao: InspectDao) {
    suspend fun insertInspectItem(inspectItem: Inspect)= inspectDao.insertInspectItem(inspectItem)
    fun getTrackingId(): LiveData<String> = inspectDao.getTrackingId()
    suspend fun getCheckCount(): Int = inspectDao.getCheckCount()
    fun getTotalCount(): LiveData<Int>  = inspectDao.getTotalCount()
    fun getInspectItem(): LiveData<Inspect> = inspectDao.getInspectItem()
    suspend fun updateCheckCount(inspectItem: Inspect) = inspectDao.updateCheckCount(inspectItem)
}