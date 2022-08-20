package com.example.zeroerror.data.repository

import com.example.zeroerror.data.GlobalApplication
import com.example.zeroerror.data.model.Inspect

class InspectRepository {
    private val appDBInstance = GlobalApplication.appDatabaseInstance.InspectDao()

    suspend fun insertInspectItem(inspectItem: Inspect) = appDBInstance.insertInspectItem(inspectItem)
    suspend fun getTrackingId() = appDBInstance.getTrackingId()
    suspend fun getTotalCount() = appDBInstance.getTotalCount()
    suspend fun getInspectItem() = appDBInstance.getInspectItem()
    fun updateCheckCount(inspectItem: Inspect) = appDBInstance.updateCheckCount(inspectItem)
}