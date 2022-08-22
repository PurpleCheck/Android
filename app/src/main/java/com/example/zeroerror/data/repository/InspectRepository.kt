package com.example.zeroerror.data.repository

import androidx.lifecycle.LiveData
import com.example.zeroerror.data.model.Inspect
import com.example.zeroerror.data.persistence.InspectDao
import javax.inject.Inject

class InspectRepositoryImpl @Inject constructor(private val inspectDao: InspectDao) {
    suspend fun insertInspectItem(inspectItem: Inspect)= inspectDao.insertInspectItem(inspectItem)
    fun getTrackingId(): LiveData<String> = inspectDao.getTrackingId()
    fun getCheckCount(): LiveData<Int> = inspectDao.getCheckCount()
    fun getTotalCount(): LiveData<Int>  = inspectDao.getTotalCount()
    fun getInspectItem(): LiveData<Inspect> = inspectDao.getInspectItem()
    fun getCompleteYN(): Boolean = inspectDao.getCompleteYN()
    suspend fun updateCheckCount(inspectItem: Inspect) = inspectDao.updateCheckCount(inspectItem)
    suspend fun deleteInspectItem() = inspectDao.deleteInspectItem()
}